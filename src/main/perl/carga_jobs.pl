#!/usr/bin/perl

BEGIN {
	use Cwd "abs_path";

	my $directorio_script = abs_path($0);
	my $nombre_script     = $directorio_script;
	$nombre_script     =~ s/[\/\.]?.*\/([\w_]+\.pl)/$1/;
	$directorio_script =~ s/\/$nombre_script//;

	#       print "el nombre del script $nombre_script\n";
	#       print "la carptet $directorio_script\n";

	push @INC, $directorio_script;
}

use strict;
use diagnostics;
use warnings;
use Log::Log4perl;
use Log::Log4perl::Layout::XMLLayout;
use Trapper;
use DBI;
use Exception::Class::DBI;
use Scalar::MoreUtils qw(empty);
use Config::Properties::Simple;
use Text::CSV;
use Clone qw(clone);

sub carga_cancelados;
sub carga_especiales;
sub carga_retrasados;
sub terminar_carga($);
sub asegura_integridad($);
sub asegura_integridad_ignora_error($$);

use constant COMANDO_PREPARA_VARIABLES_ENTORNO => qq{
. ~RUTA_PROFILE~
};
use constant COMANDO_CONECTA_BD => qq{
db2 "connect to ~ALIAS_BD~ user ~USUARIO_BD~ using ~PASS_BD~"
};

use constant COMANDO_DESCONECTA_BD => qq{
db2 "terminate"
};

my $logger;

my $propiedades;

my $coneccion;
my $usuario_bd;
my $pass_bd;
my $alias_bd;

my $comando_prepara_variables_entorno = (COMANDO_PREPARA_VARIABLES_ENTORNO);
my $comando_conecta_bd                = (COMANDO_CONECTA_BD);
my $comando_desconecta_bd             = (COMANDO_DESCONECTA_BD);

my $directorio_trabajo;
my $nombre_script;
my $archivo_cancelados;
my $archivo_eventos;
my $archivo_holdeados;
my $archivo_liberados_borrados;
my $archivo_procesos_especiales;
my $archivo_estatus_carga;
my $archivo_procesos_retrasados;
my $archivo_finalizados;

my $ruta_profile;

my $estampa_tiempo;

#Inicialzando directorio actual de ejecución
$directorio_trabajo = abs_path($0);
$nombre_script      = $directorio_trabajo;
$nombre_script      =~ s/[\/\.]?.*\/([\w_]+\.pl)/$1/;
$directorio_trabajo =~ s/\/$nombre_script//;

#Inicializando el logger
Log::Log4perl::init( $directorio_trabajo . "/carga_jobs.properties" );

#Se atrapan los mensajes de error y se mandan a log4perl
tie *STDERR, "Trapper";
tie *STDOUT, "Trapper";
$logger = Log::Log4perl->get_logger(
	"mx::BBVA::CCR::Intranet::BitacoraProduccion::Batch::Carga");
$logger->trace("La reconcha de la madre");

#Checnado que se haya pasado correctamente la estampa de tiempo para trabajar
if ( @ARGV < 1 ) {
	die "No se proporcionó el timestamp de los archivos a utilizar";
}
$estampa_tiempo = $ARGV[0];
$logger->trace("La estampa de tiempo es $estampa_tiempo");
if (
	!(
		$estampa_tiempo =~
		m/^\d{4}\_\d{1,2}\_\d{1,2}\_\d{1,2}\_\d{1,2}\_\d{1,2}\_\d{1,6}$/
	)
  )
{
	die "La estampa de tiempo $estampa_tiempo no tiene la sintaxis correcta";
}
$logger->trace("Se utiliza la estampa de tiempo $estampa_tiempo");

#Sacando valores de archivo properties
$propiedades = Config::Properties::Simple->new(
	name     => 'app/file',
	file     => $directorio_trabajo . "/etl.properties",
	optional => 0
);
$archivo_eventos    = $propiedades->getProperty("archivoEventos");
$archivo_cancelados = $propiedades->getProperty("archivoCancelados");
$archivo_holdeados  = $propiedades->getProperty("archivoHoldeados");
$archivo_liberados_borrados =
  $propiedades->getProperty("archivoLiberadosBorrados");
$archivo_procesos_especiales =
  $propiedades->getProperty("archivoProcesosEspeciales");
$archivo_estatus_carga = $propiedades->getProperty("archivoEstatusCarga");
$archivo_procesos_especiales =
  $propiedades->getProperty("archivoProcesosEspeciales");
$archivo_procesos_retrasados =
  $propiedades->getProperty("archivoProcesosRetrasados");
$archivo_finalizados = $propiedades->getProperty("archivoFinalizados");

$ruta_profile = $propiedades->getProperty("rutaProfile");

$alias_bd   = $propiedades->getProperty("aliasBDBitacora");
$usuario_bd = $propiedades->getProperty("usuarioBDBitacora");
$pass_bd    = $propiedades->getProperty("passwordBDBitacora");

#Poniendo la ruta completa y sufijo del tiempo de la carga a los nombres de archivos
$archivo_eventos =
  $directorio_trabajo . "/" . $archivo_eventos . "_" . $estampa_tiempo . ".csv";
$archivo_cancelados =
    $directorio_trabajo . "/"
  . $archivo_cancelados . "_"
  . $estampa_tiempo . ".csv";
$archivo_holdeados =
    $directorio_trabajo . "/"
  . $archivo_holdeados . "_"
  . $estampa_tiempo . ".csv";
$archivo_liberados_borrados =
    $directorio_trabajo . "/"
  . $archivo_liberados_borrados . "_"
  . $estampa_tiempo . ".csv";
$archivo_procesos_especiales =
    $directorio_trabajo . "/"
  . $archivo_procesos_especiales . "_"
  . $estampa_tiempo . ".csv";
$archivo_estatus_carga =
    $directorio_trabajo . "/"
  . $archivo_estatus_carga . "_"
  . $estampa_tiempo . ".csv";
$archivo_procesos_retrasados =
    $directorio_trabajo . "/"
  . $archivo_procesos_retrasados . "_"
  . $estampa_tiempo . ".csv";
$archivo_finalizados =
    $directorio_trabajo . "/"
  . $archivo_finalizados . "_"
  . $estampa_tiempo . ".csv";

#Agregando al path la ruta de los programas de db2
$ENV{"PATH"} =
  $ENV{"PATH"} . ":" . $propiedades->getProperty("db2Home") . "/bin";

#Preparando el comando para setear variables de entorno db2
$comando_prepara_variables_entorno =~ s/~RUTA_PROFILE~/$ruta_profile/;

#Preparando el commando para conectarse a la bd a traves de la linea de comando
$comando_conecta_bd =~ s/~ALIAS_BD~/$alias_bd/;
$comando_conecta_bd =~ s/~USUARIO_BD~/$usuario_bd/;
$comando_conecta_bd =~ s/~PASS_BD~/$pass_bd/;

#Realizando la conección a la bd via DBI
$coneccion = DBI->connect(
	"dbi:DB2:$alias_bd",
	"$usuario_bd",
	"$pass_bd",
	{
		PrintError  => 0,
		RaiseError  => 0,
		HandleError => Exception::Class::DBI->handler
	}
);

system("$comando_prepara_variables_entorno");
system("$comando_conecta_bd");
$logger->trace("Conectando a la bd con el comando\n$comando_conecta_bd");

#Aca se cargan todos los archivillos
#Cancelados
terminar_carga("GBP001.TBP006_CANCEL_TAB");
carga_cancelados();

#Holdeados
terminar_carga("GBP001.TBP007_HOLD_TAB");
carga_holdeados();
carga_liberados();

#Especiales (hitos)
terminar_carga("GBP001.TBP005_SPE_TAB");
carga_especiales();
carga_finalizados();

#Retrasados
terminar_carga("GBP001.TBP017_PRO_RET_TAB");
carga_retrasados();

#Estado de la carga
terminar_carga("GBP001.TBP033_CAR_DAT_ERR_TAB");
carga_estatus();

system("$comando_desconecta_bd");
$coneccion->disconnect();

sub carga_cancelados {
	use constant QUERY_GENERA_IDENTIFICADORES_CANCELADOS => qq{
update GBP001.TBP006_CANCEL_TAB set cd_cancelado=generate_unique() where cd_cancelado is null
};
	use constant COMANDO_CARGA_CANCELADOS => qq{
db2 "
load client from \\"~NOMBRE_ARCH_CANCELADOS~\\" of del
modified by dateformat=\\"YYYYMMDD\\" timestampformat=\\"YYYYMMDDHHMMSS\\"
method P (1,2,3,4,5,6,7,8,9,10,13) 
messages \\"~NOMBRE_ARCH_CANCELADOS~.log\\"
insert into GBP001.TBP006_CANCEL_TAB 
(CD_EMPRESA,TM_EVENTO,FH_CICLO,CD_ESTADO,NB_GRUPO,NB_PROCESO,CD_CICLO,CD_PROCESO,CD_PASO,CD_ERROR,CD_ORIGEN)
"};

	my $query_genera_identificadores_cancelados =
	  (QUERY_GENERA_IDENTIFICADORES_CANCELADOS);
	my $sentencia_preparada_genera_identificadores_cancelados;

	my $comando_carga_cancelados = (COMANDO_CARGA_CANCELADOS);

	$comando_carga_cancelados =~
	  s/~NOMBRE_ARCH_CANCELADOS~/$archivo_cancelados/g;

	$logger->trace("Cargando datos con el comando \n$comando_carga_cancelados");
	system("$comando_carga_cancelados");

	&asegura_integridad("GBP001.TBP006_CANCEL_TAB");
	$logger->trace(
"\nGenerando identificadores de cancelados\n$query_genera_identificadores_cancelados"
	);
	$sentencia_preparada_genera_identificadores_cancelados =
	  $coneccion->prepare($query_genera_identificadores_cancelados);
	$sentencia_preparada_genera_identificadores_cancelados->execute();
	$sentencia_preparada_genera_identificadores_cancelados->finish();
}

sub carga_holdeados {
	use constant COMANDO_CARGA_HOLDEADOS => qq{
db2 "
load client from \\"~NOMBRE_ARCH_HOLDEADOS~\\" of del
modified by dateformat=\\"YYYYMMDD\\" timestampformat=\\"YYYYMMDDHHMMSS\\"
method P (1,2,3,5,6,7,8,11,12,13) 
messages \\"~NOMBRE_ARCH_HOLDEADOS~.log\\"
insert into GBP001.TBP007_HOLD_TAB
(CD_EMPRESA,TM_EVENTO,FH_CICLO,NB_GRUPO,NB_PROCESO,CD_CICLO,CD_PROCESO,CD_ESTADO,CD_USUARIO_DETIENE,CD_ORIGEN)
"};

	my $comando_carga_holdeados;
	$comando_carga_holdeados = (COMANDO_CARGA_HOLDEADOS);
	$comando_carga_holdeados =~ s/~NOMBRE_ARCH_HOLDEADOS~/$archivo_holdeados/g;

	$logger->trace("el comando para cargar holdeados $comando_carga_holdeados");
	system("$comando_carga_holdeados");

	asegura_integridad("GBP001.TBP007_HOLD_TAB");

}

sub carga_liberados {

	use constant QUERY_TRAE_HOLDEADOS_PENDIENTES => qq{
select
cd_empresa,
cd_ciclo,
cd_origen,
varchar_format(tm_evento,'YYYYMMDDHH24MISS')
from GBP001.TBP007_HOLD_TAB 
where tm_finalizacion is null	
order by tm_evento
};

	use constant QUERY_ACTUALIZA_LIBERACIONES_HOLDEADOS => qq{
update GBP001.TBP007_HOLD_TAB
set (cd_usu_finaliza,tm_finalizacion,cd_estado_final)=(?,timestamp_format(?,'YYYYMMDDHH24MISS'),?)
where cd_empresa=?
and cd_origen=?
and cd_ciclo=?
and tm_evento=timestamp_format(?,'YYYYMMDDHH24MISS')
	};

	my $csv;

	my (
		$empresa, $tiempo,  $odate,     $codigo_estado,
		$grupo,   $proceso, $orderid,   $dummy1,
		$dummy2,  $dummy3,  $operacion, $usuario,
		$origen
	);

	my $llave_liberado;
	my $llave_liberado_con_tiempo;
	my %tiempos_liberados;
	my %datos_liberados;
	my @tiempos;
	my $usuario_liberacion;
	my $estado_final;

	my $query_actualiza_liberaciones_holdeados;

	my $sentencia_preparada_holdeados;
	my $sentencia_preparada_actualiza_holdeados;

	$csv = Text::CSV->new( { binary => 0, eol => "\n" } )
	  or die "No se puede utilizar la libreria CSV: " . Text::CSV->error_diag();
	open my $handle_archivo_liberados_borrados, "<", $archivo_liberados_borrados
	  or die
"No se pudo abrir el archivo de procesos liberados $archivo_liberados_borrados: $!";

	$csv->bind_columns(
		\$empresa, \$tiempo,  \$odate,     \$codigo_estado,
		\$grupo,   \$proceso, \$orderid,   \$dummy1,
		\$dummy2,  \$dummy3,  \$operacion, \$usuario,
		\$origen
	);
	while ( $csv->getline($handle_archivo_liberados_borrados) ) {
		$llave_liberado            = "$empresa,$orderid,$origen";
		$llave_liberado_con_tiempo = "$empresa,$orderid,$origen,$tiempo";
		$logger->trace("El código del liberado es $llave_liberado");
		if ( !exists $tiempos_liberados{$llave_liberado} ) {
			$logger->trace("Inicializando fila");
			$tiempos_liberados{$llave_liberado}          = [];
			$datos_liberados{$llave_liberado_con_tiempo} = [];
		}
		$logger->trace("Empujando el tiempo $tiempo");
		push( @{ $tiempos_liberados{$llave_liberado} }, $tiempo );

		push( @{ $datos_liberados{$llave_liberado_con_tiempo} }, $usuario );
		push( @{ $datos_liberados{$llave_liberado_con_tiempo} }, $operacion );
	}
	while ( ( $llave_liberado, @tiempos ) = each %tiempos_liberados ) {
		$logger->trace("Ordenando tiempos de $llave_liberado");

		$logger->trace(
			"El arreglo es " . $tiempos_liberados{$llave_liberado}[0] );
		@{ $tiempos_liberados{$llave_liberado} } =
		  sort( @{ $tiempos_liberados{$llave_liberado} } );
		$logger->trace( "El arreglo ordenado es "
			  . join( ",", @{ $tiempos_liberados{$llave_liberado} } ) );
	}
	$logger->trace("Extrayendo la información de holdeados");
	eval {

		$sentencia_preparada_holdeados =
		  $coneccion->prepare( (QUERY_TRAE_HOLDEADOS_PENDIENTES) );

		$sentencia_preparada_actualiza_holdeados =
		  $coneccion->prepare( (QUERY_ACTUALIZA_LIBERACIONES_HOLDEADOS) );

		$sentencia_preparada_holdeados->execute();

		$logger->trace("Sentencia ejecutada");
		$sentencia_preparada_holdeados->bind_columns( \$empresa, \$orderid,
			\$origen, \$tiempo );

		while ( $sentencia_preparada_holdeados->fetch() ) {
			$llave_liberado = "$empresa,$orderid,$origen";
			$logger->trace("El código del holdeado es $llave_liberado");
			if ( exists $tiempos_liberados{$llave_liberado} ) {
				@tiempos = @{ $tiempos_liberados{$llave_liberado} };
				$logger->trace( "Los tiempos del liberado $llave_liberado son "
					  . join( ",", @tiempos ) );
				foreach my $tiempo_liberado (@tiempos) {
					$logger->trace(
"Comparando el tiempo del holdeados $tiempo con el tiempo de liberacion $tiempo_liberado"
					);
					if ( $tiempo_liberado gt $tiempo ) {
						$llave_liberado_con_tiempo =
						  $llave_liberado . "," . $tiempo_liberado;
						$usuario_liberacion =
						  $datos_liberados{$llave_liberado_con_tiempo}[0];
						$estado_final =
						  $datos_liberados{$llave_liberado_con_tiempo}[1];
						$logger->trace(
"Se encontro la liberacion del holdeado $llave_liberado,$tiempo es $tiempo_liberado y fue realizada por $usuario_liberacion con la operacion $estado_final"
						);
						$sentencia_preparada_actualiza_holdeados->execute(
							$usuario_liberacion, $tiempo_liberado,
							$estado_final, $empresa, $origen, $orderid,
							$tiempo );
						last;
					}
				}
			}
		}

		$sentencia_preparada_actualiza_holdeados->finish();
		$sentencia_preparada_holdeados->finish();
	};
	if ( my $ex = $@ ) {
		$logger->error( "Tipo de excepcion: ", ref $ex );
		$logger->error( "Error: ",             $ex->error );
		$logger->error( "Err: ",               $ex->err );
		$logger->error( "Errstr: ",            $ex->errstr );
		$logger->error( "State: ",             $ex->state );
		$logger->error( "Return Value: ", ( $ex->retval || "undef" ) );
	}

}

sub carga_estatus {
	use constant COMANDO_CARGA_ESTATUS => qq{
db2 "
load client from \\"~NOMBRE_ARCH_STATUS_CARGA~\\" of del
modified by dateformat=\\"YYYYMMDD\\" timestampformat=\\"YYYYMMDDHHMMSS\\"
method P (1,2,3) 
messages \\"~NOMBRE_ARCH_STATUS_CARGA~.log\\"
insert into GBP001.TBP003_CAR_DAT_TAB
(ST_CARGA,CD_EMPRESA,TM_INSERCION)
for exception GBP001.TBP033_CAR_DAT_ERR_TAB
"};

	use constant QUERY_BORRA_TABLA_ERRORES_STATUS =>
	  qq{delete from GBP001.TBP033_CAR_DAT_ERR_TAB where 1=1};

	my $comando_carga_estatus;

	my $sentencia_preparada_borra_tabla_errores_status;

	$comando_carga_estatus = (COMANDO_CARGA_ESTATUS);
	$comando_carga_estatus =~
	  s/~NOMBRE_ARCH_STATUS_CARGA~/$archivo_estatus_carga/g;

	$logger->trace(
		"Cargando el estado de la carga con el comando $comando_carga_estatus"
	);
	system("$comando_carga_estatus");

	asegura_integridad_ignora_error( "GBP001.TBP003_CAR_DAT_TAB",
		"GBP001.TBP033_CAR_DAT_ERR_TAB" );

	$sentencia_preparada_borra_tabla_errores_status =
	  $coneccion->prepare( (QUERY_BORRA_TABLA_ERRORES_STATUS) );
	$sentencia_preparada_borra_tabla_errores_status->execute();
	$sentencia_preparada_borra_tabla_errores_status->finish();

}

sub carga_especiales {
	use constant COMANDO_CARGA_ESPECIALES =>
	  qq{db2 "load client from \\"~NOMBRE_ARCH_PROCESOS_ESPECIALES~\\" of del
modified by dateformat=\\"YYYYMMDD\\" timestampformat=\\"YYYYMMDDHHMMSS\\"
method P (1,2,3,5,6,13) 
messages \\"~NOMBRE_ARCH_PROCESOS_ESPECIALES~.log\\"
insert into GBP001.TBP005_SPE_TAB
(CD_EMPRESA,TM_INICIO,FH_CICLO,NB_GRUPO,NB_PROCESO,CD_ORIGEN)"};

	use constant COMANDO_BORRA_INTEGRIDAD_ESPECIALES =>
	  qq{db2 "delete from  GBP001.TBP030_SPEE_TAB where 1=1"};

	my $comando_carga_especiales;
	my $comando_borra_integridad_especiales;

	$comando_carga_especiales = (COMANDO_CARGA_ESPECIALES);
	$comando_carga_especiales =~
	  s/~NOMBRE_ARCH_PROCESOS_ESPECIALES~/$archivo_procesos_especiales/g;

	$comando_borra_integridad_especiales =
	  (COMANDO_BORRA_INTEGRIDAD_ESPECIALES);

	$logger->trace(
		"el comando para cargar especiales $comando_carga_especiales");
	system("$comando_carga_especiales");

	$logger->trace("Asegurando integridad de procesos especiales");
	asegura_integridad_ignora_error( "GBP001.TBP005_SPE_TAB",
		"GBP001.TBP030_SPEE_TAB" );

	$logger->trace(
"Borrando tabla de integridad usando $comando_borra_integridad_especiales"
	);
	system("$comando_borra_integridad_especiales");
}

sub carga_retrasados {
	use constant COMANDO_CARGA_RETRASADOS =>
	  qq{db2 "load client from \\"~NOMBRE_ARCH_PROCESOS_RETRASADOS~\\" of del
modified by dateformat=\\"YYYYMMDD\\" timestampformat=\\"YYYYMMDDHHMMSS\\"
method P (1,2,3,4,5,6,7) 
messages \\"~NOMBRE_ARCH_PROCESOS_RETRASADOS~.log\\"
insert into GBP001.TBP017_PRO_RET_TAB
(CD_EMPRESA,TM_INICIO,FH_CICLO,NB_GRUPO,NB_PROCESO,CD_CICLO,CD_ORIGEN)"};

	use constant COMANDO_BORRA_INTEGRIDAD_RETRASADOS =>
	  qq{db2 "delete from GBP001.TBP035_PR_ERR_TAB where 1=1"};

	my $comando_carga_retrasados;

	$comando_carga_retrasados = (COMANDO_CARGA_RETRASADOS);
	$comando_carga_retrasados =~
	  s/~NOMBRE_ARCH_PROCESOS_RETRASADOS~/$archivo_procesos_retrasados/g;

	$logger->trace(
		"el comando para cargar retrasados $comando_carga_retrasados");
	system("$comando_carga_retrasados");

	asegura_integridad_ignora_error( "GBP001.TBP017_PRO_RET_TAB",
		"GBP001.TBP035_PR_ERR_TAB" );

	system( (COMANDO_BORRA_INTEGRIDAD_RETRASADOS) );

}

sub carga_finalizados {

	use constant QUERY_TRAE_ESPECIALES_PENDIENTES => qq{
select
cd_empresa,
cd_origen,
nb_grupo,
nb_proceso,
varchar_format(fh_ciclo,'YYYYMMDD'),
varchar_format(tm_inicio,'YYYYMMDDHH24MISS')
from GBP001.TBP005_SPE_TAB 
where tm_fin is null	
order by tm_inicio
};

	use constant QUERY_ACTUALIZA_FINALIZACIONES_ESPECIALES => qq{
update GBP001.TBP005_SPE_TAB
set (tm_fin,cd_estado_fin,cd_usuario_fin)=(timestamp_format(?,'YYYYMMDDHH24MISS'),?,?)
where cd_empresa=?
and cd_origen=?
and nb_grupo=?
and nb_proceso=?
and fh_ciclo=to_date(?,'YYYYMMDD')
and tm_inicio=timestamp_format(?,'YYYYMMDDHH24MISS')
	};

	my $csv;

	my (
		$empresa, $tiempo,  $odate,     $codigo_estado,
		$grupo,   $proceso, $orderid,   $dummy1,
		$dummy2,  $dummy3,  $operacion, $usuario,
		$origen
	);

	my $llave_finalizado;
	my $llave_finalizado_con_tiempo;
	my %tiempos_finalizados;
	my %datos_finalizados;
	my @tiempos;
	my $usuario_finalizacion;
	my $estado_final;

	my $query_actualiza_finalizaciones_especiales;

	my $sentencia_preparada_especiales;
	my $sentencia_preparada_actualiza_especiales;

	$csv = Text::CSV->new( { binary => 0, eol => "\n" } )
	  or die "No se puede utilizar la libreria CSV: " . Text::CSV->error_diag();
	open my $handle_archivo_finalizados, "<", $archivo_finalizados
	  or die
"No se pudo abrir el archivo de procesos finalizados $archivo_finalizados: $!";

	$csv->bind_columns(
		\$empresa, \$tiempo,  \$odate,     \$codigo_estado,
		\$grupo,   \$proceso, \$orderid,   \$dummy1,
		\$dummy2,  \$dummy3,  \$operacion, \$usuario,
		\$origen
	);
	while ( $csv->getline($handle_archivo_finalizados) ) {
		$llave_finalizado = "$empresa,$origen,$grupo,$proceso,$odate";
		$llave_finalizado_con_tiempo =
		  "$empresa,$origen,$grupo,$proceso,$odate,$tiempo";
		$logger->trace("El código del finalizado es $llave_finalizado");
		if ( !exists $tiempos_finalizados{$llave_finalizado} ) {
			$logger->trace("Inicializando fila");
			$tiempos_finalizados{$llave_finalizado}          = [];
			$datos_finalizados{$llave_finalizado_con_tiempo} = [];
		}
		$logger->trace("Empujando el tiempo $tiempo");
		push( @{ $tiempos_finalizados{$llave_finalizado} }, $tiempo );

		push( @{ $datos_finalizados{$llave_finalizado_con_tiempo} }, $usuario );
		push(
			@{ $datos_finalizados{$llave_finalizado_con_tiempo} },
			$operacion
		);
	}
	while ( ( $llave_finalizado, @tiempos ) = each %tiempos_finalizados ) {
		$logger->trace("Ordenando tiempos de $llave_finalizado");

		$logger->trace(
			"El arreglo es " . $tiempos_finalizados{$llave_finalizado}[0] );
		@{ $tiempos_finalizados{$llave_finalizado} } =
		  sort( @{ $tiempos_finalizados{$llave_finalizado} } );
		$logger->trace( "El arreglo ordenado es "
			  . join( ",", @{ $tiempos_finalizados{$llave_finalizado} } ) );
	}
	$logger->trace("Extrayendo la información de holdeados");
	eval {

		$sentencia_preparada_especiales =
		  $coneccion->prepare( (QUERY_TRAE_ESPECIALES_PENDIENTES) );

		$sentencia_preparada_actualiza_especiales =
		  $coneccion->prepare( (QUERY_ACTUALIZA_FINALIZACIONES_ESPECIALES) );

		$sentencia_preparada_especiales->execute();

		$logger->trace("Sentencia ejecutada");
		$sentencia_preparada_especiales->bind_columns( \$empresa, \$origen,
			\$grupo, \$proceso, \$odate, \$tiempo );

		while ( $sentencia_preparada_especiales->fetch() ) {
			$llave_finalizado = "$empresa,$origen,$grupo,$proceso,$odate";
			$logger->trace("El código del especial es $llave_finalizado");
			if ( exists $tiempos_finalizados{$llave_finalizado} ) {
				@tiempos = @{ $tiempos_finalizados{$llave_finalizado} };
				$logger->trace(
					"Los tiempos del finalizado $llave_finalizado son "
					  . join( ",", @tiempos ) );
				foreach my $tiempo_finalizado (@tiempos) {
					$logger->trace(
"Comparando el tiempo del especial $tiempo con el tiempo de finalizacion $tiempo_finalizado"
					);
					if ( $tiempo_finalizado gt $tiempo ) {
						$llave_finalizado_con_tiempo =
						  $llave_finalizado . "," . $tiempo_finalizado;
						$usuario_finalizacion =
						  $datos_finalizados{$llave_finalizado_con_tiempo}[0];
						$estado_final =
						  $datos_finalizados{$llave_finalizado_con_tiempo}[1];
						$logger->trace(
"Se encontro la finalizacion del espeical $llave_finalizado,$tiempo es $tiempo_finalizado y fue realizada por $usuario_finalizacion con la operacion $estado_final"
						);
						$sentencia_preparada_actualiza_especiales->execute(
							$tiempo_finalizado,    $estado_final,
							$usuario_finalizacion, $empresa,
							$origen,               $grupo,
							$proceso,              $odate,
							$tiempo
						);
						last;
					}
				}
			}
		}

		$sentencia_preparada_actualiza_especiales->finish();
		$sentencia_preparada_especiales->finish();
	};
	if ( my $ex = $@ ) {
		$logger->error( "Tipo de excepcion: ", ref $ex );
		$logger->error( "Error: ",             $ex->error );
		$logger->error( "Err: ",               $ex->err );
		$logger->error( "Errstr: ",            $ex->errstr );
		$logger->error( "State: ",             $ex->state );
		$logger->error( "Return Value: ", ( $ex->retval || "undef" ) );
	}

}

sub terminar_carga($) {

	use constant COMANDO_TERMINA_CARGA => qq{
db2 "
load client from /tmp/algo.csv of del
terminate into ~TABLA~
"};

	my $comando_termina_carga = (COMANDO_TERMINA_CARGA);

	my $tabla;

	$tabla = $_[0];
	if ( empty($tabla) ) {
		$logger->error("La tabla no se proporcionó, abortando");
		return;
	}
	$logger->trace("La tabla de la que se terminará la carga $tabla");

	$comando_termina_carga =~ s/~TABLA~/$tabla/g;
	$logger->trace(
		"El comando para terminar la carga es $comando_termina_carga");
	system("$comando_termina_carga");
}

sub asegura_integridad($) {
	use constant COMANDO_ASEGURA_INTEGRIDAD => qq{
db2 "
set integrity for ~TABLA~ immediate checked
"};
	my $comando_asegura_integridad = (COMANDO_ASEGURA_INTEGRIDAD);

	my $tabla;

	$tabla = $_[0];
	if ( empty($tabla) ) {
		$logger->error("La tabla no se proporcionó, abortando");
		return;
	}

	$comando_asegura_integridad =~ s/~TABLA~/$tabla/g;
	$logger->trace(
		"Asegurando integridad de $tabla usando $comando_asegura_integridad");
	system("$comando_asegura_integridad");

}

sub asegura_integridad_ignora_error($$) {
	use constant COMANDO_ASEGURA_INTEGRIDAD_IGNORA_ERRORES => qq{
db2 "
set integrity for ~TABLA~ immediate checked for exception in ~TABLA~ use ~TABLA_ERROR~
"};
	my $comando_asegura_integridad =
	  (COMANDO_ASEGURA_INTEGRIDAD_IGNORA_ERRORES);

	my ( $tabla, $tabla_error ) = @_;

	if ( empty($tabla) ) {
		$logger->error("La tabla no se proporcionó, abortando");
		return;
	}
	if ( empty($tabla_error) ) {
		$logger->error("La tabla de errores no se proporcionó, abortando");
		return;
	}

	$comando_asegura_integridad =~ s/~TABLA~/$tabla/g;
	$comando_asegura_integridad =~ s/~TABLA_ERROR~/$tabla_error/g;
	$logger->trace(
"Asegurando integridad de $tabla con tabla de error $tabla_error usando $comando_asegura_integridad"
	);
	system("$comando_asegura_integridad");

}

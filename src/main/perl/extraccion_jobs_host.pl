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
use File::Copy;
use Time::HiRes qw(gettimeofday);
use Config::Properties::Simple;
use DBI;
use Exception::Class::DBI;

my $logger;
my $propiedades;

my $identificador_origen_host;

my $tiempo;
my (
	$segundo, $minuto,     $hora,    $dia, $mes,
	$ano,     $dia_semana, $dia_ano, $horario_verano
);
my $segundos;
my $microsegundos;
my $ano_recortado;

my $estampa_tiempo;

my $directorio_trabajo;
my $nombre_script;
my $archivo_eventos_host;
my $archivo_eventos;
my $archivo_cancelados;
my $archivo_holdeados;
my $archivo_liberados_borrados;
my $archivo_procesos_especiales;
my $archivo_estatus_carga;
my $archivo_finalizados;
my $archivo_procesos_retrasados;

#Inicialzando directorio actual de ejecución
$directorio_trabajo = abs_path($0);
$nombre_script      = $directorio_trabajo;
$nombre_script      =~ s/[\/\.]?.*\/([\w_]+\.pl)/$1/;
$directorio_trabajo =~ s/\/$nombre_script//;

#Inicializando el logger
Log::Log4perl::init( $directorio_trabajo . "/extraccion_jobs_host.properties" );

#Se atrapan los mensajes de error y se mandan a log4perl
tie *STDERR, "Trapper";
tie *STDOUT, "Trapper";
$logger = Log::Log4perl->get_logger(
	"mx::BBVA::CCR::Intranet::BitacoraProduccion::Batch::Extraccion::Host");

#Seteando estampa de tiempo y variables relacionadas con el tiempo
$tiempo = time();
(
	$segundo, $minuto,     $hora,    $dia, $mes,
	$ano,     $dia_semana, $dia_ano, $horario_verano
) = localtime($tiempo);
$ano += 1900;
$mes++;
$mes = $mes < 10 ? "0" . $mes : $mes;
$dia = $dia < 10 ? "0" . $dia : $dia;
$ano_recortado = $ano;
$ano_recortado =~ s/20//;
( $segundos, $microsegundos ) = gettimeofday;

$estampa_tiempo =
    $ano . "_" 
  . $mes . "_" 
  . $dia . "_" 
  . $hora . "_" 
  . $minuto . "_" 
  . $segundo . "_"
  . $microsegundos;

#Sacando valores de archivo properties
$propiedades = Config::Properties::Simple->new(
	name     => 'app/file',
	file     => $directorio_trabajo . "/etl.properties",
	optional => 0
);
$identificador_origen_host =
  $propiedades->getProperty("identificadorOrigenHost");
$archivo_eventos_host = $propiedades->getProperty("archivoEventosHost");
$archivo_eventos      = $propiedades->getProperty("archivoEventos");
$archivo_cancelados   = $propiedades->getProperty("archivoCancelados");
$archivo_holdeados    = $propiedades->getProperty("archivoHoldeados");
$archivo_liberados_borrados =
  $propiedades->getProperty("archivoLiberadosBorrados");
$archivo_procesos_especiales =
  $propiedades->getProperty("archivoProcesosEspeciales");
$archivo_estatus_carga = $propiedades->getProperty("archivoEstatusCarga");
$archivo_finalizados   = $propiedades->getProperty("archivoFinalizados");
$archivo_procesos_retrasados =
  $propiedades->getProperty("archivoProcesosRetrasados");

#Poniendo la ruta completa y sufijo del tiempo de la carga a los nombres de archivos
$archivo_eventos_host = $directorio_trabajo . "/" . $archivo_eventos_host;
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
$archivo_finalizados =
    $directorio_trabajo . "/"
  . $archivo_finalizados . "_"
  . $estampa_tiempo . ".csv";
$archivo_procesos_retrasados =
    $directorio_trabajo . "/"
  . $archivo_procesos_retrasados . "_"
  . $estampa_tiempo . ".csv";

$logger->trace("El archivo de eventos es $archivo_eventos");

#Copiando el archivo de eventos de host a otro con el nombre de archivo de eventos
copy( $archivo_eventos_host . ".csv", $archivo_eventos );

genera_archivo_eventos();
genera_archivo_cancelados();
genera_archivo_holdeados();
genera_archivo_liberados_borrados();
genera_archivo_procesos_especiales();
genera_archivo_finalizados();

#genera_archivo_procesos_retrasados();
genera_archivo_estatus_carga();

system("$directorio_trabajo/carga_jobs.pl $estampa_tiempo");

sub genera_archivo_eventos {
	use constant COMANDO_QUITA_ESPACIOS     => qq{sed -i "s/ //g"};
	use constant COMANDO_QUITA_NULOS        => qq{sed -i "s/,,/,-,/g"};
	use constant COMANDO_QUITA_CARACTER_X0D => qq{sed -i "s/\x0d//"};
	use constant COMANDO_QUITA_NULO_FINAL   => qq{sed -i "s/,\$/,-/"};

	use constant COMANDO_ANADE_ID_ORIGEN =>
	  qq{sed -i "s/\$/,~IDENTIFICADOR_ORIGEN~/"};

	my $comando_quita_espacios     = (COMANDO_QUITA_ESPACIOS);
	my $comando_quita_nulos        = (COMANDO_QUITA_NULOS);
	my $comando_quita_caracter_x0d = (COMANDO_QUITA_CARACTER_X0D);
	my $comando_quita_nulo_final   = (COMANDO_QUITA_NULO_FINAL);
	my $comando_anade_id_origen    = (COMANDO_ANADE_ID_ORIGEN);

	$comando_anade_id_origen =~
	  s/~IDENTIFICADOR_ORIGEN~/$identificador_origen_host/;

	system("$comando_quita_espacios $archivo_eventos");
	system("$comando_quita_nulos $archivo_eventos");
	system("$comando_quita_nulos $archivo_eventos");
	system("$comando_quita_nulos $archivo_eventos");
	system("$comando_quita_caracter_x0d $archivo_eventos");
	system("$comando_quita_nulo_final $archivo_eventos");
	system("$comando_anade_id_origen $archivo_eventos");
}

sub genera_archivo_cancelados {
	use constant CODIGOS_CANCELACION => qw(
	  SEL207E
	  SEL206W
	  SEL211W
	  SEL210E
	  SEL212W
	  SEL213W
	  SEL216W
	);
	use constant COMANDO_FILTRA_CANCELADOS =>
qq{grep -E "~FILTRO_REGISTROS_CANCELADOS~" ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_CANCELADOS~};

	my $comando_filtra_cancelados   = (COMANDO_FILTRA_CANCELADOS);
	my @codigos_cancelacion         = (CODIGOS_CANCELACION);
	my $filtro_registros_cancelados = join( "|", @codigos_cancelacion );

	$comando_filtra_cancelados =~
	  s/~FILTRO_REGISTROS_CANCELADOS~/$filtro_registros_cancelados/;
	$comando_filtra_cancelados =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_filtra_cancelados =~
	  s/~NOMBRE_ARCH_CANCELADOS~/$archivo_cancelados/;

	$logger->trace("stunt 101 $comando_filtra_cancelados");
	system("$comando_filtra_cancelados");
}

sub genera_archivo_holdeados {
	use constant COMANDO_FILTRA_HOLDEADOS =>
qq{grep -P ",H[oO][lL][dD]," ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_HOLDEADOS~};

	my $comando_filtra_holdeados;
	$comando_filtra_holdeados = (COMANDO_FILTRA_HOLDEADOS);
	$comando_filtra_holdeados =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_filtra_holdeados =~ s/~NOMBRE_ARCH_HOLDEADOS~/$archivo_holdeados/;

	$logger->trace(
		"Extrayendo los holdeados con el comando $comando_filtra_holdeados");
	system("$comando_filtra_holdeados");
}

sub genera_archivo_liberados_borrados {
	use constant COMANDO_FILTRA_LIBERADOS_BORRADOS =>
qq{grep -P ",(F[rR][eE][eE]|D[eE][lL][eE][tT][eE])," ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_LIBERADOS_BORRADOS~};

	my $comando_filtra_liberados;
	$comando_filtra_liberados = (COMANDO_FILTRA_LIBERADOS_BORRADOS);
	$comando_filtra_liberados =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_filtra_liberados =~
	  s/~NOMBRE_ARCH_LIBERADOS_BORRADOS~/$archivo_liberados_borrados/;

	$logger->trace(
		"Extrayendo los liberados con el comando $comando_filtra_liberados");
	system("$comando_filtra_liberados");
}

sub genera_archivo_procesos_especiales {
	use constant COMANDO_FILTRA_PROCESOS_ESPECIALES =>
qq{grep -P "JOB511I" ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_PROCESOS_ESPECIALES~};

	my $comando_filtra_especiales;
	$comando_filtra_especiales = (COMANDO_FILTRA_PROCESOS_ESPECIALES);
	$comando_filtra_especiales =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_filtra_especiales =~
	  s/~NOMBRE_ARCH_PROCESOS_ESPECIALES~/$archivo_procesos_especiales/;

	$logger->trace(
		"Extrayendo los especiales con el comando $comando_filtra_especiales");
	system("$comando_filtra_especiales");
}

sub genera_archivo_estatus_carga {
	use constant COMANDO_GENERA_STATUS_CARGA =>
qq{cut -d, -f1 ~NOMBRE_ARCH_EVENTOS~|sort|uniq -c | perl -p -e "s/(\\d+) /1,/"|sed "s/\$/,~ESTAMPA_TIEMPO_COMPACTA~/" > ~NOMBRE_ARCH_STATUS_CARGA~};

	my $comando_genera_status_carga;
	my $estampa_tiempo_compacta;

	$estampa_tiempo_compacta = $ano . $mes . $dia . $hora . $minuto . $segundo;

	$comando_genera_status_carga = (COMANDO_GENERA_STATUS_CARGA);
	$comando_genera_status_carga =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_genera_status_carga =~
	  s/~NOMBRE_ARCH_STATUS_CARGA~/$archivo_estatus_carga/;
	$comando_genera_status_carga =~
	  s/~ESTAMPA_TIEMPO_COMPACTA~/$estampa_tiempo_compacta/;
	$logger->trace(
"Generando el estado de la carga con el comando $comando_genera_status_carga"
	);
	system("$comando_genera_status_carga");

}

sub genera_archivo_finalizados {
	use constant COMANDO_FILTRA_FINALIZADOS =>
qq{grep -E ",(SEL208I|D[Ee][Le][Ee][Tt][Ee])," ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_FINALIZADOS~};

	my $comando_filtra_finalizados = (COMANDO_FILTRA_FINALIZADOS);

	$comando_filtra_finalizados =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_filtra_finalizados =~
	  s/~NOMBRE_ARCH_FINALIZADOS~/$archivo_finalizados/;

	$logger->trace("colando finalizados con $comando_filtra_finalizados");
	system("$comando_filtra_finalizados");
}

sub genera_archivo_procesos_retrasados {
	use constant QUERY_EVENTOS_PROCESOS_RETRASADOS => qq{
select 
a.LPAR

||','||

a.START_TIME

||','||

'20'||a.ODATE

||','||


a.GROUP_NAME

||','||

case
	when a.JOB_NAME is null	then 
		a.MEMNAME
	when REGEXP_LIKE(a.JOB_NAME,' +') then
		a.MEMNAME
	else
		a.JOB_NAME
end

||','||

a.ORDER_ID

from A~YYMMDDXXX~_AJOB a
where 
EXTRACT(day FROM (LOCALTIMESTAMP-TO_TIMESTAMP(a.START_TIME, 'yyyymmddhh24miss')))*24*60*60+
EXTRACT(hour FROM (LOCALTIMESTAMP-TO_TIMESTAMP(a.START_TIME, 'yyyymmddhh24miss')))*60*60+
EXTRACT(minute FROM (LOCALTIMESTAMP-TO_TIMESTAMP(a.START_TIME, 'yyyymmddhh24miss')))*60+
EXTRACT(second FROM (LOCALTIMESTAMP-TO_TIMESTAMP(a.START_TIME, 'yyyymmddhh24miss'))) >
cast(SUBSTR(a.AVG_RUNTIME, 1, 2) as number)*60*60+
cast(SUBSTR(a.AVG_RUNTIME, 3, 2) as number)*60 + 
cast(SUBSTR(a.AVG_RUNTIME, 5, 2) as number)
and a.status='Executing'
and a.START_TIME is not NULL 
and LENGTH(a.START_TIME)>1
and a.AVG_RUNTIME is not null
and LENGTH(a.AVG_RUNTIME)>1
and (a.LPAR is not null and LENGTH(a.LPAR)>1)
and (a.CPU_ID is null or LENGTH(a.CPU_ID)<2)
};

	use constant CODIGO_DATA_CENTER => qw(
	  004
	  008
	  009
	  010
	  012
	  013
	  014
	  015
	  050
	  055
	  056
	  101
	  998
	);

	use constant COMANDO_QUITA_COMAS_CON_ESPACIO => qq{sed -i "s/, \\+,/,,/g"};

	my @codigos_data_center = (CODIGO_DATA_CENTER);
	my $codigo_data_center;
	my $comando_quita_comas_con_espacio = (COMANDO_QUITA_COMAS_CON_ESPACIO);
	my $comando_quita_nulos             = (COMANDO_QUITA_NULOS);

	my $identificador_tabla;
	my $query_eventos;

	my $coneccion;
	my $usuario_bd;
	my $pass_bd;
	my $ip_bd;
	my $sid_bd;
	my $sentencia_preparada_eventos;
	my $informacion_eventos;

	$usuario_bd         = $propiedades->getProperty("usuarioBDControlM");
	$pass_bd            = $propiedades->getProperty("passwordBDControlM");
	$ip_bd              = $propiedades->getProperty("ipBDControlM");
	$sid_bd             = $propiedades->getProperty("sidBDControlM");
	$ENV{"ORACLE_HOME"} = $propiedades->getProperty("oracleHome");

	$logger->trace("Conectando a la bd de control-m");
	$coneccion = DBI->connect(
		"dbi:Oracle:host=$ip_bd;sid=$sid_bd",
		$usuario_bd,
		$pass_bd,
		{
			PrintError  => 0,
			RaiseError  => 0,
			HandleError => Exception::Class::DBI->handler
		}
	  )
	  or die
	  "No se pudo realizar la coneccion con la bd de oracte $DBI::errstr\n";
	$logger->trace("Coneccion a la bd de control-m exitosa");

	open( ARCHEVENTOSPROCRET, ">" . $archivo_procesos_retrasados );
	foreach $codigo_data_center (@codigos_data_center) {
		$logger->trace("El codigo actual es $codigo_data_center");
		$identificador_tabla = "$ano_recortado$mes$dia$codigo_data_center";
		$logger->trace("El identificador de la tabla es $identificador_tabla");
		$query_eventos = (QUERY_EVENTOS_PROCESOS_RETRASADOS);
		$query_eventos =~ s/~YYMMDDXXX~/$identificador_tabla/g;
		$logger->trace("El query que se ejecutara $query_eventos");
		eval {
			$sentencia_preparada_eventos = $coneccion->prepare($query_eventos);
			$sentencia_preparada_eventos->execute();
			$sentencia_preparada_eventos->bind_columns( \$informacion_eventos );

			while ( $sentencia_preparada_eventos->fetch() ) {

				$informacion_eventos =~ s/^MEX.,/M,/;
				$informacion_eventos =~ s/^CHLP,/L,/;
				$informacion_eventos =~ s/^COLP,/C,/;
				$informacion_eventos =~ s/^PERP,/P,/;
				$informacion_eventos =~ s/^PRCP,/R,/;
				$informacion_eventos =~ s/^USAD,/W,/;
				print ARCHEVENTOSPROCRET
				  "$informacion_eventos,$identificador_origen_host\n";
			}
			$sentencia_preparada_eventos->finish();
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
	close(ARCHEVENTOSPROCRET);

	$coneccion->disconnect();

	system("$comando_quita_comas_con_espacio $archivo_procesos_retrasados");
	system("$comando_quita_nulos $archivo_procesos_retrasados");
	system("$comando_quita_nulos $archivo_procesos_retrasados");

}

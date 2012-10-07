#!/usr/bin/perl

=pod
=head1 NAME
extraccion_jobs_distribuido.pl - Extre la información de control-m em de su BD. 
=head1 DESCRIPTION
Ejecuta 2 querys para extraer y dar formato a la información necesaria para la bitácora. Uno es para sacar la información de los eventos de control-m.
El otro es para sacar la info de las acciones del operador. 
=head1 CONFIGURATION AND ENVIROMENT
Se utilizan los siguientes archivos de configuración:
=over 4
	=item * F<etl.properties> 
	Debe estar en el mismo directorio y contiene varios datos comunes entre los scripts de ETL de la bitácora.
	=item * F<extraccion_jobs_distribudo.properties> 
	Debe estar en el mismo directorio y contiene la configuración para L<Log::Log4perl>. Si no se esta utilizando apache chainsaw 
	(L<http://logging.apache.org/chainsaw/index.html>), es necesario que este script tenga desactivada la configuración para dicho programa, ya que
	de lo contrario, no corre por que no puede conectarse. 
=back
=head1 DEPENDENCIES
=over 4
Todos estas librerias no son estandar de la instalación de perl, se recomienda añadirlas usando cpan (L<http://www.cpan.org/modules/INSTALL.html>):
	=item * L<Log::Log4perl>
	Se utiliza para mantener un registro del proceso de ETL o su debuggeo durante desarrollo.
	=item * L<Log::Log4perl::Layout::XMLLayout>
	Permite conectarse con la interfaz gráfica apache chainsaw.
	=item * L<Trapper>
	Libreria que sobreescribe la funcionalidad nativa de manejo de errores y advertencias, de manera que en vez de enviarlas hacia la consola, las envia
	a L<Log::Log4perl>.
	=item * L<Exception::Class::DBI>
	Permite hacer un manejo más sencillo y verboso de las excepciones en DBI.
	=item * L<Time::HiRes>
	Permite obtener el tiempo actual con precisión de microsegundos.
	=item * L<Config::Properties::Simple>
	Permite leer archivos *.properties, de manera que se pueden utilizar facilmente los valores contenidos en esos archivos.
	=item * L<Cwd>
	Permite obtener datos relacionados a la ruta donde se ejecuta el script.
	=item * L<Date::Calc>
	Libreria que facilita la aritmética de tiempo.
=back
=head1 AUTHOR
Hiram Ernesto Alvarado Gaspar
=cut

=for Rationale:
Se intercepta el inicio de la ejecución del script, de manera que se obtiene la ruta absoluta de ejecución del script y se agrega a la variable
C<@INC>. De esa forma, el módulo L<Trapper> puede ser encontrado y utilizado cuando este script se ejecuta por crontab o control-m. 
=cut

BEGIN {
	use Cwd "abs_path";
	my $directorio_script = abs_path($0);
	my $nombre_script     = $directorio_script;
	$nombre_script     =~ s/[\/\.]?.*\/([\w_]+\.pl)/$1/;
	$directorio_script =~ s/\/$nombre_script//;
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
use Time::HiRes qw(gettimeofday);
use Config::Properties::Simple;
use Date::Calc qw(Add_Delta_Days);

#Prototipos
sub formatea_tiempo($);
sub genera_estampas_tiempo($);

my $logger;
my $propiedades;
my $tiempo;
my (
	$segundo, $minuto,     $hora,    $dia, $mes,
	$ano,     $dia_semana, $dia_ano, $horario_verano
);
my $segundos;
my $microsegundos;
my $ano_recortado;

my $identificador_origen_distribuido;

my $directorio_trabajo;
my $nombre_script;
my $archivo_cancelados;
my $archivo_eventos;
my $archivo_holdeados;
my $archivo_liberados_borrados;
my $archivo_procesos_especiales;
my $archivo_estatus_carga;
my $archivo_procesos_retrasados;

my $tiempo_inicio_ejecucion;
my $tiempo_fin_ejecucion;
my $segundos_ejecucion;
my $estampa_tiempo;

#Inicialzando directorio actual de ejecución
$directorio_trabajo = abs_path($0);
$nombre_script      = $directorio_trabajo;
$nombre_script      =~ s/[\/\.]?.*\/([\w_]+\.pl)/$1/;
$directorio_trabajo =~ s/\/$nombre_script//;

#Inicializando logger
Log::Log4perl::init(
	$directorio_trabajo . "/extraccion_jobs_distribuido.properties" );

#Se atrapan los mensajes de error y se mandan a log4perl
tie *STDERR, "Trapper";
$logger = Log::Log4perl->get_logger(
"mx::BBVA::CCR::Intranet::BitacoraProduccion::Batch::Extraccion::Distribuido"
);
$logger->trace("La reconcha de la madre");

#Seteando estampa de tiempo y otras variables de tiempo
$tiempo = time();
( $ano, $ano_recortado, $mes, $dia, $hora, $minuto, $segundo, $microsegundos ) =
  formatea_tiempo($tiempo);

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
$identificador_origen_distribuido =
  $propiedades->getProperty("identificadorOrigenDistribuido");
$archivo_eventos    = $propiedades->getProperty("archivoEventos");
$archivo_cancelados = $propiedades->getProperty("archivoCancelados");
$archivo_holdeados  = $propiedades->getProperty("archivoHoldeados");
$archivo_liberados_borrados =
  $propiedades->getProperty("archivoLiberadosBorrados");
$archivo_procesos_especiales =
  $propiedades->getProperty("archivoProcesosEspeciales");
$archivo_estatus_carga = $propiedades->getProperty("archivoEstatusCarga");
$archivo_procesos_retrasados =
  $propiedades->getProperty("archivoProcesosRetrasados");

#Seteando la variable de entorno ORACLE_HOME
$ENV{"ORACLE_HOME"} = $propiedades->getProperty("oracleHome");
$logger->trace( "El oracle home " . $ENV{"ORACLE_HOME"} );

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

$tiempo_inicio_ejecucion = time();

$logger->trace("El año en 2 digitos $ano_recortado");
$logger->trace("La fecha de mierda $ano-$mes-$dia");

genera_archivo_eventos();
genera_archivo_cancelados();
genera_archivo_holdeados();
genera_archivo_liberados_borrados();
genera_archivo_procesos_especiales();
genera_archivo_procesos_retrasados();
genera_archivo_estatus_carga();

system("$directorio_trabajo/carga_jobs.pl $estampa_tiempo");

$tiempo_fin_ejecucion = time();
$segundos_ejecucion   = ( $tiempo_fin_ejecucion - $tiempo_inicio_ejecucion );

$logger->trace("El script se ejecuto en $segundos_ejecucion segundos");

#
#
#
#######################################################################################################
# Usage			: genera_archivo_eventos()
# Purpose		: Hace el query para obtener la información de los eventos de control-m, lo deja en un archivo y
#					le quita las cadenas vacias y nulas, sustituyendolas por "-", ademas al final de cada linea archivo pone
#					el identificador del origen.
# Returns		: Nada
# Parameters	: Ninguno
# Throws		: n/a
# Comments		: Las constantes usadas son las siguientes:
#					* CODIGO_DATA_CENTER: No es claro si realmente es el código que se asigna a cada data center, pero crea una tabla cada día
#					para cada uno de estos códigos.
#					* QUERY_EVENTOS: Es el query que extrae la información de las tablas pertinentes en la bd de control-m.
#					Contiene los siguientes patrones, que se sustituyen para poder formar el query:
#						* YYMMDDXXX: Es el año (YY), mes (MM), día (DD) y código de la tabla (XXX).
#					Es importante mencionar a detalle su funcionamiento. Control-m guarda los datos de los eventos en tablas creadas dinámicamente,
#					una para cada día aparentemente, la sintaxis del nombre de dichas tablas es la siguiente:
#						* Para la tabla de eventos: A<YYMMDDXXX>_AJOB, donde YYMMDDXXX es el patrón descrito anteriormente. La parte <XXX>
#						es un misterio, se llena con los valores de CODIGO_DATA_CENTER, se hace un query por cada elemento de ese arreglo.
#						* Para la tabla de detalle de cancelación: A<YYMMDDXXX>_AON, donde YYMMDDXXX es el patrón descrito anteriormente. De esta
#						tabla solo se extrae, en teoria, el paso y el código de cancelación, sin embargo, en los procesos distribuidos, esta información
#						no se registra.
#					* COMANDO_QUITA_COMAS_CON_ESPACIO: Comando utilizado para quitar espacios en blanco entre 2 comas.
#					* COMANDO_QUITA_NULOS: Comando utilizado para que las comas sin espacio entre ellas, tengan un "-", de manera que
#					no se ingresen nulos.
# See Also		:
sub genera_archivo_eventos {
	use constant QUERY_EVENTOS => qq{
select 

case
	when a.JOB_NAME is null then 
		substr(a.MEMNAME,1,1)
	when REGEXP_LIKE(a.JOB_NAME,' +') then
		substr(a.MEMNAME,1,1)
	else 
		substr(a.JOB_NAME,1,1)
	end
	
||','||

a.ORDER_TIME

||','||

'20'||a.ODATE

||','||

a.status

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

||','||

a.JOB_ID

||','||

c.pgm_step

||','||

c.code

||

',-,-'

from A~YYMMDDXXX~_AJOB a
left join DEF_VER_TABLES d
on (d.SCHED_TABLE=a.ORDER_TABLE)
LEFT JOIN A~YYMMDDXXX~_AON c
on (c.ISN_=a.isn_)
where d.DATA_CENTER in ('CTM_CTRLMMX','CTM_CTRLMCCR')

union

select 
case
	when a.JOB_NAME is null then 
		substr(a.MEM_NAME,1,1)
	when REGEXP_LIKE(a.JOB_NAME,' +') then
		substr(a.MEM_NAME,1,1)
	else 
		substr(a.JOB_NAME,1,1)
	end
||','||
to_char(a.TIMESTAMP,'yyyymmddhhmi')
||','||
a.ORDER_DATE
||','||
a.OPERATION
||','||
a.GROUP_NAME
||','||
case
	when a.JOB_NAME is null then 
		a.MEM_NAME
	when REGEXP_LIKE(a.JOB_NAME,' +') then
		a.MEM_NAME
	else
		a.JOB_NAME
end
||','||
a.ORDER_ID
||',-,-,-,'||
a.OPERATION
||','||
a.USERNAME
from AUDIT_ACTIVITIES a
};

	use constant CODIGO_DATA_CENTER => qw(
	  001
	  004
	  008
	  010
	  014
	  015
	  040
	  050
	  056
	  101
	);

	use constant COMANDO_QUITA_COMAS_CON_ESPACIO => qq{sed -i "s/, \\+,/,,/g"};
	use constant COMANDO_QUITA_NULOS             => qq{sed -i "s/,,/,-,/g"};

	my @codigos_data_center = (CODIGO_DATA_CENTER);
	my $codigo_data_center;
	my $comando_quita_comas_con_espacio = (COMANDO_QUITA_COMAS_CON_ESPACIO);
	my $comando_quita_nulos             = (COMANDO_QUITA_NULOS);

	my @estampas_tiempo;
	my ( $ano_recortado, $mes, $dia );

	my $identificador_tabla;
	my $query_eventos;

	my $coneccion;
	my $usuario_bd;
	my $pass_bd;
	my $ip_bd;
	my $sid_bd;
	my $schema_bd;
	my $sentencia_preparada_cambiar_schema;
	my $sentencia_preparada_eventos;
	my $informacion_eventos;

	$usuario_bd = $propiedades->getProperty("usuarioBDControlM");
	$pass_bd    = $propiedades->getProperty("passwordBDControlM");
	$ip_bd      = $propiedades->getProperty("ipBDControlM");
	$sid_bd     = $propiedades->getProperty("sidBDControlM");
	$schema_bd  = $propiedades->getProperty("schemaBDControlM");

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
	@estampas_tiempo = genera_componentes_fechas($tiempo);

	$sentencia_preparada_cambiar_schema =
	  $coneccion->prepare( "alter session set current_schema=" . $schema_bd );
	$sentencia_preparada_cambiar_schema->execute();
	$logger->trace("Coneccion a la bd de control-m exitosa");

	open( ARCHEVENTOS, ">" . $archivo_eventos );
	for my $i ( 0 .. $#estampas_tiempo ) {
		foreach $codigo_data_center (@codigos_data_center) {
			( $ano_recortado, $mes, $dia ) = @{ $estampas_tiempo[$i] };
			$logger->trace( "El mes " . $estampas_tiempo[$i][1] );
			$logger->trace("El codigo actual es $codigo_data_center");
			$identificador_tabla = "$ano_recortado$mes$dia$codigo_data_center";
			$logger->info(
				"El identificador de la tabla es $identificador_tabla");
			$query_eventos = QUERY_EVENTOS;
			$query_eventos =~ s/~YYMMDDXXX~/$identificador_tabla/g;
			$logger->trace("El query que se ejecutara $query_eventos");
			eval {
				$sentencia_preparada_eventos =
				  $coneccion->prepare($query_eventos);
				$sentencia_preparada_eventos->execute();
				$sentencia_preparada_eventos->bind_columns(
					\$informacion_eventos );

				while ( $sentencia_preparada_eventos->fetch() ) {
					print ARCHEVENTOS
"$informacion_eventos,$identificador_origen_distribuido\n";
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
	}
	close(ARCHEVENTOS);

	$coneccion->disconnect();

	system("$comando_quita_comas_con_espacio $archivo_eventos");
	system("$comando_quita_nulos $archivo_eventos");
	system("$comando_quita_nulos $archivo_eventos");

}

sub genera_archivo_cancelados {
	use constant COMANDO_FILTRA_CANCELADOS =>
	  qq{grep -P ".*Not OK.*" ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_CANCELADOS~};
	$logger->trace("archivo de eventos $archivo_eventos");
	$logger->trace("archivo de cancelados $archivo_cancelados");

	my $comando_filtra_cancelados;
	$comando_filtra_cancelados = (COMANDO_FILTRA_CANCELADOS);
	$comando_filtra_cancelados =~ s/~NOMBRE_ARCH_EVENTOS~/$archivo_eventos/;
	$comando_filtra_cancelados =~
	  s/~NOMBRE_ARCH_CANCELADOS~/$archivo_cancelados/;

	$logger->trace(
		"Extrayendo los cancelados con el comando $comando_filtra_cancelados");
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
qq{grep -P -v ".*(E[nN][dD][eE][dD]).*" ~NOMBRE_ARCH_EVENTOS~ > ~NOMBRE_ARCH_PROCESOS_ESPECIALES~};

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

sub genera_archivo_procesos_retrasados {
	use constant QUERY_EVENTOS_PROCESOS_RETRASADOS => qq{
select 

case
	when a.JOB_NAME is null then 
		substr(a.MEMNAME,1,1)
	when REGEXP_LIKE(a.JOB_NAME,' +') then
		substr(a.MEMNAME,1,1)
	else 
		substr(a.JOB_NAME,1,1)
	end
	
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
and (a.LPAR is null or LENGTH(a.LPAR)<2)
and (a.CPU_ID is not null and LENGTH(a.CPU_ID)>1)
};

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

	$usuario_bd = $propiedades->getProperty("usuarioBDControlM");
	$pass_bd    = $propiedades->getProperty("passwordBDControlM");
	$ip_bd      = $propiedades->getProperty("ipBDControlM");
	$sid_bd     = $propiedades->getProperty("sidBDControlM");

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
				print ARCHEVENTOSPROCRET
				  "$informacion_eventos,$identificador_origen_distribuido\n";
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

#######################################################################################################
# Usage			: formatea_tiempo($tiempo)
# Purpose		: Darle formato a la epoca regresada por la función time().
# Returns		: Un arreglo con los siguientes elementos en el orden en que se enumeran:
#					* Año a 4 dígitos
#					* Año a 2 dígitos
#					* Número de mes, tomando enero como 1
#					* Día del mes
#					* Hora del día a 2 dígitos
#					* Minuto del día a 2 dígitos
#					* Segundo del día a 2 dígitos
#					* Microsegundo de la epoca
# Parameters	: $tiempo Época regresada por la función time().
# Throws		: n/a
# Comments		: none
# See Also		: time(), gettimeofday(), localtime()
sub formatea_tiempo ($) {
	my $tiempo = $_[0];
	my (
		$segundo, $minuto,     $hora,    $dia, $mes,
		$ano,     $dia_semana, $dia_ano, $horario_verano
	);
	my $segundos;
	my $microsegundos;
	my $ano_recortado;

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

	$logger->trace("Los microsegundos sson $microsegundos");
	return ( $ano, $ano_recortado, $mes, $dia, $hora, $minuto, $segundo,
		$microsegundos );
}

#######################################################################################################
# Usage			: genera_componentes_fechas($tiempo)
# Purpose		: Generar los componentes de la fecha del día representado por la epoca y de los 2 anteriores.
# Returns		: Un arreglo con los componentes de la fecha, con la posición 0 con el día representado por $tiempo y en las subsecuentes
#					posiciones con ese día menos el número de días de posición.
# Parameters	: $tiempo Época regresada por la función time().
# Throws		: n/a
# Comments		: none
# See Also		: Add_Delta_Days
sub genera_componentes_fechas($) {
	my $tiempo     = $_[0];
	my $DIAS_ATRAS = 30;
	my ( $ano, $ano_recortado, $mes, $dia, $hora, $minuto, $segundo,
		$microsegundos );
	my ( $ano_res, $mes_res, $dia_res );
	my $estampa_tiempo;
	my @estampas_tiempo = ();
	for ( my $i = 0 ; $i <= $DIAS_ATRAS ; $i++ ) {
		(
			$ano, $ano_recortado, $mes, $dia, $hora, $minuto, $segundo,
			$microsegundos
		) = formatea_tiempo($tiempo);
		( $ano_res, $mes_res, $dia_res ) =
		  Add_Delta_Days( $ano, $mes, $dia, -$i );
		$ano_res =~ s/20//;
		$mes_res = $mes_res < 10 ? "0" . $mes_res : $mes_res;
		$dia_res = $dia_res < 10 ? "0" . $dia_res : $dia_res;
		$logger->trace("El año, mes y dia son $ano_res,$mes_res,$dia_res");
		push( @estampas_tiempo, [ $ano_res, $mes_res, $dia_res ] );
	}
	return @estampas_tiempo;
}

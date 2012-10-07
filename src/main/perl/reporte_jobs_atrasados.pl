#!/usr/bin/perl

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

my $tiempo;
my (
	$segundo, $minuto,     $hora,    $dia, $mes,
	$ano,     $dia_semana, $dia_ano, $horario_verano
);
my $segundos;
my $microsegundos;
my $ano_recortado;

my $identificador_origen_distribuido;

my $archivo_procesos_retrasados;

my $tiempo_inicio_ejecucion;
my $tiempo_fin_ejecucion;
my $segundos_ejecucion;
my $estampa_tiempo;

#Seteando estampa de tiempo y otras variables de tiempo
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

$archivo_procesos_retrasados = "reporte_retrasados.csv";
$ENV{"ORACLE_HOME"} =
  "/programas/instalados/oracle/app/ernesto/product/11.2.0/dbhome_1";

genera_archivo_procesos_retrasados();

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

||','||
a.LPAR
||','||
a.CPU_ID
||','||
a.AVG_RUNTIME

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
	use constant COMANDO_QUITA_NULOS             => qq{sed -i "s/,,/,-,/g"};

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

	$usuario_bd = "emuser";
	$pass_bd    = "manager";
	$ip_bd      = "150.250.40.179";
	$sid_bd     = "bctbp001";

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

	open( ARCHEVENTOSPROCRET, ">" . $archivo_procesos_retrasados );
	foreach $codigo_data_center (@codigos_data_center) {
		$identificador_tabla = "$ano_recortado$mes$dia$codigo_data_center";
		$query_eventos       = (QUERY_EVENTOS_PROCESOS_RETRASADOS);
		$query_eventos =~ s/~YYMMDDXXX~/$identificador_tabla/g;
#		print "El query que se va a tirar es $query_eventos\n";
		eval {
			$sentencia_preparada_eventos = $coneccion->prepare($query_eventos);
			$sentencia_preparada_eventos->execute();
			$sentencia_preparada_eventos->bind_columns( \$informacion_eventos );

			while ( $sentencia_preparada_eventos->fetch() ) {
				print ARCHEVENTOSPROCRET "$informacion_eventos\n";
			}
			$sentencia_preparada_eventos->finish();
		};
		if ( my $ex = $@ ) {
			print "Hubo un error con la extraccion de datos\n";
		}

	}
	close(ARCHEVENTOSPROCRET);

	$coneccion->disconnect();

	system("$comando_quita_comas_con_espacio $archivo_procesos_retrasados");
	system("$comando_quita_comas_con_espacio $archivo_procesos_retrasados");
	system("$comando_quita_comas_con_espacio $archivo_procesos_retrasados");
	system("$comando_quita_nulos $archivo_procesos_retrasados");
	system("$comando_quita_nulos $archivo_procesos_retrasados");
	system("$comando_quita_nulos $archivo_procesos_retrasados");

}

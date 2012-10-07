#!/bin/sh
while true
do
	echo "Sacando caca"
	/usr/bin/perl /home/ernesto/desarrollo/batch/src/main/perl/extraccion_jobs_distribuido.pl
	echo "Cargando caca"
	/usr/bin/perl /home/ernesto/desarrollo/batch/src/main/perl/carga_jobs_distribuido.pl
	rm -fv /home/ernesto/desarrollo/batch/src/main/perl/info_eventos*
#	env
	sleep 300
done

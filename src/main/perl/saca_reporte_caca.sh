#!/bin/sh
echo caca
while true
do
	./reporte_jobs_atrasados.pl
	scp reporte_retrasados.csv m909381@150.100.214.222:/viejo/flaco/mauricio/
	sleep 300
done

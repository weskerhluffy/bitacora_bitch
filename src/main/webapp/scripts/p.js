var popupStatus = 0;
var isScheduled = false;
var tituloCalendario;
var fecha = gup("fecha");
var fechaActual;
var codigoEmpresa = gup("codigo_empresa");
var nombreServletReportes = "Liberaciones";
var rutaServlet = nombreServletReportes + "?codigo_empresa=" + codigoEmpresa
+ "&" + "fecha=" + fecha + "&tipo_reporte=";
var urlComponentesXAplicacionDia = rutaServlet + "0&";
var urlComponentesXAplicacionMes = rutaServlet + "1&";
var urlComponentesXTipoComponenteDia = rutaServlet + "2&";
var urlComponentesXTipoComponenteMes = rutaServlet + "3&";
var urlComponentesTocadosAnual = rutaServlet + "4&componente=.*&";
var urlPaquetesXAplicacionDia = rutaServlet + "5&";
var urlPaquetesXAplicacionMes = rutaServlet + "6&";
var urlPaquetesXTipoComponenteDia = rutaServlet + "7&";
var urlPaquetesXTipoComponenteMes = rutaServlet + "8&";
var urlCanceladosCicloAnterior = rutaServlet + "9&componente=.*&";
var urlAbendsCicloAnterior = rutaServlet + "10&";
var popUpBgCalendar = "#backgroundPopupCalendar";
var popUpCntCalendar = "#popupContactCalendar";
var popUpBackgroundDet = "#backgroundPopupDetalles";
var popUpContentDet = "#popupContactDetalles";
var popUpBackgroundSchDay = "#backgroundPopup";
var popUpContentSchDay = "#popupContact";
var cambiosTocados;
$j(document)
.ready(
		function() {
			GraficaCambiosService
			.getSumasCambios(
					codigoEmpresa,
					fecha,
					function datosListos(datos) {
//						Objeto anonimo con los parámetros que
//						se pasarán al reporte
						var parametrosDetalles = {
								urlReporte : "",
								graficaId : ""
						};
						var parametrosGrafica = {
								color : ""
						};
						var cambiosXAplicacion = JSONQuery(
								"[?id.hora='" + fecha + "']",
								datos.cambiosComponenteXAplicacion);
						var cambiosXTipoPrograma = JSONQuery(
								"[?id.hora='" + fecha + "']",
								datos.cambiosComponenteXTipoPrograma);
						var cambiosPqtXAplicacion = JSONQuery(
								"[?id.hora='" + fecha + "']",
								datos.cambiosPaqueteXAplicacion);
						logger.trace("La puta madre");
						var cambiosPqtXTipoPrograma = JSONQuery(
								"[?id.hora='" + fecha + "']",
								datos.cambiosPaqueteXTipoPrograma);
						logger.trace("Mierda");
						var cambiosPqtXPlataforma = JSONQuery(
								"[?id.hora='" + fecha + "']",
								datos.cambiosPaqueteXPlataforma);
						var cambiosCmpModificados = datos.componentesTocados;
						cambiosTocados = datos.cambios;
						var crearGrafica;
						var cambiosCancelaciones = datos.cancelacions;
						muestraInformacion(
								cambiosCancelaciones,
								datos.abendsLinea);
						/*---CAMBIOS POR COMPONENTES---*/
						logger
						.info("**********COMP-APLS-HOY**********");
						var tipo = ".cmpAplDia";
						parametrosDetalles.urlReporte = urlComponentesXAplicacionDia;
						parametrosDetalles.graficaId = 0;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#9342AA";
						crearGrafica = new Grafica(
								"#graficaXAplicacion",
								cambiosXAplicacion.splice(0, 9),
								"grupo", "valor",
								parametrosGrafica);
						logger
						.info("********COMP-APLS-MENSUAL********");
//						El query para agrupar y sumar por
//						grupos
						var datosCompAplMens = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosComponenteXAplicacion,
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".cmpAplMes";
						parametrosDetalles.urlReporte = urlComponentesXAplicacionMes;
						parametrosDetalles.graficaId = 6;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						crearGrafica = new Grafica(
								"#graficaXApl",
								datosCompAplMens.splice(0, 9),
								"agrupacion", "sum_valor", {
									color : "#2187C5"
								});
						logger
						.info("********COMP-TIPO-PROGRAMA-HOY********");
						/* Datos para mostrar detalles */
						var tipo = ".cmpPrgDia";
						parametrosDetalles.urlReporte = urlComponentesXTipoComponenteDia;
						parametrosDetalles.graficaId = 1;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#D76009";
						crearGrafica = new Grafica(
								"#graficaXTipoPrograma",
								cambiosXTipoPrograma.splice(0,
										9), "grupo", "valor",
										parametrosGrafica);
						logger
						.info("********COMP-TIPO-PROGRAMA-MENSUAL********");
						var datosCompTCMens = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosComponenteXTipoPrograma,
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".cmpPrgMes";
						parametrosDetalles.urlReporte = urlComponentesXTipoComponenteMes;
						parametrosDetalles.graficaId = 7;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#B421A3";
						crearGrafica = new Grafica(
								"#graficaXProgramaMensual",
								datosCompTCMens.splice(0, 9),
								"agrupacion", "sum_valor",
								parametrosGrafica);
						logger
						.info("********COMP-PLATAFORMA-HOY********");
						var datosCompPlatDia = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosComponenteXPlataforma,
							           Where : function() {
							        	   return this.tiempo == fecha;
							           },
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						var tipo = ".cmpPltDia";
						parametrosDetalles.urlReporte = urlComponentesXAplicacionDia;
						parametrosDetalles.graficaId = 2;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#D7CD53";
						crearGrafica = new Grafica(
								"#graficaXPltDia",
								datosCompPlatDia, "agrupacion",
								"sum_valor", parametrosGrafica);
						logger
						.info("********COMP-PLATAFORMA-MENSUAL********");
						var datosCompPlatMes = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosComponenteXPlataforma,
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".cmpPltMes";
						parametrosDetalles.urlReporte = urlComponentesXAplicacionMes;
						parametrosDetalles.graficaId = 8;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#A78E38";
						crearGrafica = new Grafica(
								"#graficaXPltMensual",
								datosCompPlatMes, "agrupacion",
								"sum_valor", parametrosGrafica);
						/* -----CAMBIOS POR PAQUETES----- */
						logger
						.info("********PAQUETES-APLS-HOY********");
						var pqtAplOrdenado = JSONQuery(
								"[\\valor]",
								cambiosPqtXAplicacion);
						/* Datos para mostrar detalles */
						var tipo = ".pqtAplDia";
						parametrosDetalles.urlReporte = urlPaquetesXAplicacionDia;
						parametrosDetalles.graficaId = 3;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#9342AA";
						crearGrafica = new Grafica(
								"#graficaPqtXAplicacion",
								pqtAplOrdenado.splice(0, 9),
								"grupo", "valor",
								parametrosGrafica);
						logger
						.info("*********PAQUETES-APLS-MENSUAL**************");
						var datosPaqAplMens = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosPaqueteXAplicacion,
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".pqtAplMes";
						parametrosDetalles.urlReporte = urlPaquetesXAplicacionMes;
						parametrosDetalles.graficaId = 9;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#2187C5";
						crearGrafica = new Grafica(
								"#graficaPqtXAplicacionMensual",
								datosPaqAplMens.splice(0, 9),
								"agrupacion", "sum_valor",
								parametrosGrafica);
						logger
						.info("*********PAQUETES-TIPO-PROGRAMA-HOY*********");
						var pqtPrgOrdenado = JSONQuery(
								"[\\valor]",
								cambiosPqtXTipoPrograma);
						/* Datos para mostrar detalles */
						var tipo = ".pqtPrgDia";
						parametrosDetalles.urlReporte = urlPaquetesXTipoComponenteDia;
						parametrosDetalles.graficaId = 4;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#D76009";
						crearGrafica = new Grafica(
								"#graficaPqtXPrograma",
								pqtPrgOrdenado.splice(0, 9),
								"grupo", "valor",
								parametrosGrafica);
						logger
						.info("*********PAQUETES-TIPO-PROGRAMA-MENSUAL*********");
						var datosPaqTCMens = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosPaqueteXTipoPrograma,
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".pqtPrgMes";
						parametrosDetalles.urlReporte = urlPaquetesXTipoComponenteMes;
						parametrosDetalles.graficaId = 10;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#B421A3";
						crearGrafica = new Grafica(
								"#graficaPqtXPrgMensual",
								datosPaqTCMens.splice(0, 9),
								"agrupacion", "sum_valor",
								parametrosGrafica);
						logger
						.info("*********PAQUETES-PLATAFORMA-HOY*********");
						var datosPaqPlatDia = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosPaqueteXPlataforma,
							           Where : function() {
							        	   return this.tiempo == fecha;
							           },
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".pqtPltDia";
//						TODO Hacer referencia a algun reporte
						parametrosDetalles.urlReporte = "";
						parametrosDetalles.graficaId = 5;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#D7CD53";
						crearGrafica = new Grafica(
								"#graficaPqtXPlt",
								datosPaqPlatDia, "agrupacion",
								"sum_valor", parametrosGrafica);
						logger
						.info("*********PAQUETES-PLATAFORMA-MENSUAL*********");
						var datosPaqPlatMes = SQLike
						.q({
							Select : [ 'agrupacion',
							           '|sum|', 'valor' ],
							           From : datos.cambiosPaqueteXPlataforma,
							           GroupBy : [ 'agrupacion' ],
							           OrderBy : [ 'sum_valor',
							                       '|desc|' ]
						});
						/* Datos para mostrar detalles */
						var tipo = ".pqtPltMes";
						parametrosDetalles.urlReporte = "";
						parametrosDetalles.graficaId = 11;
						verDetalles(tipo, parametrosDetalles);
						/* Creando grafica */
						parametrosGrafica.color = "#A78E38";
						crearGrafica = new Grafica(
								"#graficaPqtXPltMensual",
								datosPaqPlatMes, "agrupacion",
								"sum_valor", parametrosGrafica);
						logger
						.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
						/* -----Incidentes de crap----- */
						logger
						.info("******Incidentessss******");
						/*
						 * Se obtienen valores de las
						 * plataformas
						 */
						var arregloCmbPlt = datos.cancelacions;
						parametrosDetalles.urlReporte = urlCanceladosCicloAnterior;
						parametrosDetalles.graficaId = 13;
						verDetalles(".incidentes",
								parametrosDetalles);
						var cancelacionesBatch = arregloCmbPlt.length;
						var cancelaBatch = {
								plt : "BATCH",
								valor : cancelacionesBatch
						};
						var cancelaLinea = {
								plt : "LINEA",
								valor : datos.abendsLinea
						}
						logger.info("Cancelaciones Batch: "
								+ cancelacionesBatch);
						logger.info("Abends Linea: "
								+ cancelaLinea.valor);
						/* Creando grafica */
						crearGrafica = new Grafica(
								"#graficaCambiosPlt", [
cancelaBatch,
cancelaLinea ], "plt",
"valor", "#8B3466");
						/* -----CAMBIOS POR PAQUETES----- */
						logger
						.info("*********CAMBIOS ANUAL*********");
						/* Cambios del anio en curso */
						var arregloCmpCambios = obtieneValoresXFechas(
								"id.grupo",
								datos.componentesTocados);
						var valoresCmpCambios = arregloCmpCambios
						.map(function(item, indice) {
							return [ indice, item.valor ];
						});
						var labelsCmpCambios = arregloCmpCambios
						.map(function(item, indice) {
							return [ indice, item.apl ];
						});
						parametrosDetalles.urlReporte = urlComponentesTocadosAnual;
						parametrosDetalles.graficaId = 12;
						verDetalles(".cmpAnual",
								parametrosDetalles);
						var graficaCmpCambios = $j
						.plot(
								$j("#graficaCmpCambios"),
								[ valoresCmpCambios ],
								{
									series : {
										color : "#5572C5",
										bars : {
											show : true,
											align : "center"
										}
									},
									xaxis : {
										ticks : labelsCmpCambios
									},
									grid : {
										borderWidth : 0,
										hoverable : true,
										clickable : true
									}
								});
						$j("#calendario").hide();
						$j("#graficasMensual").hide();
						$j("#graficaCmpCambios")
						.bind(
								"plotclick",
								function(event, pos,
										item) {
									if (item) {
										var arregloTmp = cambiosCmpModificados[item.dataIndex].id.hora;
										scheduledDays = arregloTmp
										.split(",");
										logger
										.trace("item: "
												+ scheduledDays.length);
										tituloCalendario = item.series.xaxis.ticks[item.dataIndex].label;
										verCalendario(tituloCalendario);
										$j(
										"#datepicker")
										.datepicker(
												{
													closeText : 'Fermer',
													monthNames : [
'Enero',
'Febrero',
'Marzo',
'Abril',
'Mayo',
'Junio',
'Julio',
'Agosto',
'Septiembre',
'Octubre',
'Noviembre',
'Diciembre' ],
dayNamesMin : [
'Do',
'Lu',
'Ma',
'Mi',
'Ju',
'Vi',
'Sa' ],
numberOfMonths : [
3,
4 ],
maxDate : '+0m',
minDate : '-365d',
beforeShowDay : setScheduledDays,
showButtonPanel : false,
hideIfNoPrevNext : true,
onSelect : onSelectDayMsg
												});
										$j(
										"#datepicker")
										.datepicker(
										"refresh");
										$j(
										"#calendario")
										.show();
										var tituloMeses = $j(".ui-datepicker-title");
										obtenerFechaOnclick(tituloMeses);
									}
								});
					});
			$j("#encabezadoLibMesActual").click(function() {
				$j("#graficasMensual").toggle();
			});
		});
/**
 *
 * @param datos
 * @param tipoAgrupacion
 * @return Regresa la suma de los valores de un atributo de los objetos del
 * arreglo <code>datos[]</code>
 */
function sumaPorGrupos(datos, tipoAgrupacion) {
	var gruposTMP = JSONQuery("[=" + tipoAgrupacion + "]", datos);
	var grupos = [];
	grupos.combine(gruposTMP);
	var arreglo = grupos.map(function(item, index) {
		var valoresGrupos = JSONQuery("[?" + tipoAgrupacion + " = '" + item
				+ "']", datos);
		logger.trace(" Grupo: " + (index + 1) + " = " + item);
		var valorGrupos = 0;
		valoresGrupos.each(function(item, index) {
			valorGrupos = valorGrupos + item.valor;
		});
		return {
			apl : item,
			valor : valorGrupos
		};
	});
//	TODO parametrizar el atributo de ordenamiento
	var arregloOrdenado = JSONQuery("[\\valor]", arreglo);
	logger.info("**********Arreglo ordenado**************");
	/* Aplicaciones con valores en forma descendente */
	arregloOrdenado.each(function(item, index) {
		logger.debug(index + " = " + introspect("apls", item));
	});
	return arregloOrdenado;
}
/**
 *
 * @param tipoAgrupacion
 * @param datos
 * @return Regresa la suma de los valores de un atributo de los objetos del
 * arreglo con las fechas correspondientes<code>datos[]</code>
 */
function obtieneValoresXFechas(tipoAgrupacion, datos) {
	var gruposTMP = JSONQuery("[=" + tipoAgrupacion + "]", datos);
	logger.debug("Todos las plataformas: " + tipoAgrupacion + " " + gruposTMP);
	var grupos = [];
	grupos.combine(gruposTMP);
	logger.info("Agrupando...");
	logger.debug("Grupos por: " + tipoAgrupacion + " " + grupos);
	var arreglo = grupos.map(function(item, index) {
		/* plataformas */
		var valoresGrupos = JSONQuery("[?" + tipoAgrupacion + " = '" + item
				+ "']", datos);
		logger.trace("Grupo: " + (index + 1) + " = " + item);
		/* valores */
		var valor = 0;
		valoresGrupos.each(function(item, index) {
			valor = valor + item.valor;
		});
		/* fechas */
		var fechaTMP = JSONQuery("[=id.hora]", valoresGrupos);
		var fechas = [];
		fechas.combine(fechaTMP);
		return {
			apl : item,
			valor : valor,
			fecha : fechas
		};
		logger.debug(arreglo);
	});
	var arregloOrdenadoConFechas = JSONQuery("[\\valor]", arreglo);
	logger.info("Ordenando por valores de grupos con fechas...");
	arregloOrdenadoConFechas.each(function(item, index) {
		logger.debug(index + " = " + introspect("plts", item));
	});
	return arregloOrdenadoConFechas;
}
/**
 *
 * @param tipoAgrupacion
 * @param datos
 * @return Regresa la suma de los valores de un atributo de los objetos del
 * arreglo con las fechas correspondientes<code>datos[]</code>
 */
function obtieneXFechas(tipoAgrupacion, datos) {
	var gruposTMP = JSONQuery("[=" + tipoAgrupacion + "]", datos);
	logger.debug("Todos las plataformas: " + tipoAgrupacion + " " + gruposTMP);
	var grupos = [];
	grupos.combine(gruposTMP);
	logger.info("Agrupando...");
	logger.debug("Grupos por: " + tipoAgrupacion + " " + grupos);
	var arreglo = grupos.map(function(item, index) {
		/* plataformas */
		var valoresGrupos = JSONQuery("[?" + tipoAgrupacion + " = '" + item
				+ "']", datos);
		logger.trace("Grupo: " + (index + 1) + " = " + item);
		/* valores */
		var valor = 0;
		valor = valoresGrupos.length;
		/* fechas */
		var fechaTMP = JSONQuery("[=id.fecha]", valoresGrupos);
		var fechas = [];
		fechas.combine(fechaTMP);
		return {
			apl : item,
			valor : valor,
			fecha : fechas
		};
//		logger.debug(arreglo);
	});
	var arregloOrdenadoConFechas = JSONQuery("[\\valor]", arreglo);
	logger.info("Ordenando por valores de grupos con fechas...");
	arregloOrdenadoConFechas.each(function(item, index) {
		logger.debug(index + " = " + introspect("plts", item));
	});
	return arregloOrdenadoConFechas;
}
/**
 * Funcion que asigna un estilo css a un conjunto de fechas.
 *
 * @param date
 * @return Regresa verdadero si las fechas estan agendadas y el estilo asignado.
 */
function setScheduledDays(date) {
	var scheduleStatus = "";
	/* Verifica d�as agendados */
	for (i = 0; i < scheduledDays.length; i++) {
		logger.trace("Dia: " + scheduledDays[i]);
		var mes = parseInt(scheduledDays[i].split("-")[1], 10) - 1;
		var dia = parseInt(scheduledDays[i].split("-")[2], 10);
		var anio = parseInt(scheduledDays[i].split("-")[0]);
		logger.trace("mes: " + mes + " dia: " + dia + " anio: " + anio);
		if (date.getMonth() == mes && date.getDate() == dia
				&& date.getFullYear() == anio) {
			isScheduled = true;
			scheduleStatus = "ui-state-default ui-state-active";
		} else {
			isScheduled = false;
		}
	}
	return [ true, scheduleStatus ];
}
/**
 * Funcion que muestra un popUp al ser elegido un dia agendado
 *
 * @param dateText
 * @param inst
 * @return
 */
function onSelectDayMsg(dateText, inst) {
	/* Obtiene el dia, mes y anio seleccionados por el usuario */
	var mesSeleccionado = parseInt(dateText.split("/")[0], 10);
	var diaSeleccionado = parseInt(dateText.split("/")[1], 10);
	var anioSeleccionado = parseInt(dateText.split("/")[2]);
	var fecha = dateText;
	var componente = tituloCalendario;
	logger.trace("El dia seleccionado es: " + dateText);
	/* Deshabilita el popUp del calendario */
	disablePopup(popUpBgCalendar, popUpCntCalendar);
//	alert("Entrando a detalles, deshabilitando calendario");
	/*
	 * Obtiene las fechas agendadas y las compara con la fecha seleccionada, si
	 * la fecha seleccionada esta agendada, muestra un popUp
	 */
	for (i = 0; i < scheduledDays.length; i++) {
		var tmp = scheduledDays[i].split("-");
		var mes = parseInt(tmp[1], 10);
		var dia = parseInt(tmp[2], 10);
		var anio = parseInt(tmp[0]);
		logger.trace("Dia agendado: " + mes + "/" + dia + "/" + anio);
		logger.trace("El dia de shit es: " + tmp[2]);
		if (mesSeleccionado == mes && diaSeleccionado == dia
				&& anioSeleccionado == anio) {
//			logger.trace("Dia seleccionado: " + diaSeleccionado + " = " + dia
//			+ "?");
//			logger.trace("Mes seleccionado: " + mesSeleccionado + " = " + mes
//			+ "?");
//			logger.trace("Anio seleccionado: " + anioSeleccionado + " = " +
//			anio + "?");
//			codigo JSON
			var arregloPaquetes = cambiosTocados
			.filter(function(item, index) {
				if (item.id.componente == componente
						&& item.id.fechaInstalacion.getDate() == diaSeleccionado
						&& (item.id.fechaInstalacion.getMonth() + 1) == mesSeleccionado
						&& item.id.fechaInstalacion.getFullYear() == anioSeleccionado) {
//					logger.info("El componente es: " + componente + "
//					del dia " + diaSeleccionado+ " del mes " +
//					mesSeleccionado);
					return true;
				}
			});
			var titulo = "#popUpTitle";
			var tabla = "#table_id";
			muestraComponentes(titulo, tabla, arregloPaquetes);
//			TODO Funcionalidad a tabla
			/*
			 * $j('#table_id').dataTable( { "bPaginate": false, "bLengthChange":
			 * false, "bFilter": false, "bSort": false, "bInfo": true,
			 * "bAutoWidth": true } );
			 */
			/* Centrado del popUp */
			centerPopup(popUpBackgroundSchDay, popUpContentSchDay);
			/* Carga el popUp */
			loadPopup(popUpBackgroundSchDay, popUpContentSchDay);
			/* Cierra el popUp */
			$j('#popupContactClose').click(function() {
				disablePopup(popUpBackgroundSchDay, popUpContentSchDay);
			});
			/* Deshabilita el popUp */
			$j(popUpBackgroundSchDay).click(function() {
				disablePopup(popUpBackgroundSchDay, popUpContentSchDay);
			});
			/* Evento al dar click al boton 'Regresar' */
			$j('.regresarCalendario').click(function() {
				$j("#popupContact").hide();
				verCalendario(componente);
			});
			/* Evento al dar click al boton 'Salir' */
			$j('.cerrarDetalle').click(function() {
				disablePopup(popUpBackgroundSchDay, popUpContentSchDay);
			});
			isScheduled = true;
		} else {
			isScheduled = false;
		}
	}
}
/**
 * Funcion que muestra detalles de la grafica seleccionada
 *
 * @return
 */
function verDetalles(grafica, parametros) {
	/* Evento al dar click al boton 'Ver detalles' */
	var urlReporte = parametros.urlReporte;
	var graficaId = parametros.graficaId;
	$j(grafica)
	.click(
			function() {
				$j("#popUpTitleDetalles").text("Detalles");
				descGrafica(graficaId);
				if (graficaId != 13) {
					$j("#html")
					.html(
							"<a target='_blank' href=\""
							+ escaparURI(urlReporte)
							+ "tipo_salida="
							+ 0
							+ "\"><img border='0' src='images/htmlIcon.png'/></a><br/>");
					$j("#pdf")
					.html(
							"<a href=\""
							+ escaparURI(urlReporte)
							+ "tipo_salida="
							+ 1
							+ "\"><img border='0' src='images/adobeIcon.png'/></a><br/>");
					$j("#excel")
					.html(
							"<a href=\""
							+ escaparURI(urlReporte)
							+ "tipo_salida="
							+ 2
							+ "\"><img border='0' src='images/excelIcon.png'/></a><br/>");
				} else {
					$j("#html")
					.html(
							"Batch:<a target='_blank' href=\""
							+ escaparURI(urlReporte)
							+ "tipo_salida="
							+ 0
							+ "\"><img border='0' src='images/htmlIcon.png'/></a><br/>");
					$j("#html")
					.append(
							"Linea:<a target='_blank' href=\""
							+ escaparURI(urlAbendsCicloAnterior)
							+ "tipo_salida="
							+ 0
							+ "\"><img border='0' src='images/htmlIcon.png'/></a><br/>");
					$j("#pdf")
					.html(
							"Batch:<a href=\""
							+ escaparURI(urlReporte)
							+ "tipo_salida="
							+ 1
							+ "\"><img border='0' src='images/adobeIcon.png'/></a><br/>");
					$j("#pdf")
					.append(
							"Linea:<a href=\""
							+ escaparURI(urlAbendsCicloAnterior)
							+ "tipo_salida="
							+ 1
							+ "\"><img border='0' src='images/adobeIcon.png'/></a><br/>");
					$j("#excel")
					.html(
							"Batch:<a href=\""
							+ escaparURI(urlReporte)
							+ "tipo_salida="
							+ 2
							+ "\"><img border='0' src='images/excelIcon.png'/></a><br/>");
					$j("#excel")
					.append(
							"Linea:<a href=\""
							+ escaparURI(urlAbendsCicloAnterior)
							+ "tipo_salida="
							+ 2
							+ "\"><img border='0' src='images/excelIcon.png'/></a><br/>");
				}
				/* Centra el popUp */
				centerPopup(popUpBackgroundDet, popUpContentDet);
				/* Carga el popUp */
				loadPopup(popUpBackgroundDet, popUpContentDet);
			});
	/* Cierra el popUp */
	$j("#popupContactCloseDetalles").click(function() {
		disablePopup(popUpBackgroundDet, popUpContentDet);
	});
	/* Deshabilita el popUp */
	$j(popUpBackgroundDet).click(function() {
		disablePopup(popUpBackgroundDet, popUpContentDet);
	});
}
/* FUNCION PARA ASIGNAR TEXTOS.-............... PENDIENTE */
function descGrafica(grafica) {
	var tipoGrafica = grafica;
	var textosGraficas = new Array();
	textosGraficas[0] = "APLICACIONES con modificaci" + "\u00f3"
	+ "n para el ciclo ACTUAL";
	textosGraficas[1] = "Modificaci" + "\u00f3"
	+ "n por TIPO DE COMPONENTE para el ciclo ACTUAL";
	textosGraficas[2] = "Modificaci" + "\u00f3"
	+ "n por PLATAFORMA para el ciclo ACTUAL";
	textosGraficas[3] = "APLICACIONES con modificaci" + "\u00f3"
	+ "n para el ciclo ACTUAL";
	textosGraficas[4] = "Modificaci" + "\u00f3"
	+ "n por TIPO DE COMPONENTE para el ciclo ACTUAL";
	textosGraficas[5] = "Modificaci" + "\u00f3"
	+ "n por PLATAFORMA para el ciclo ACTUAL";
	textosGraficas[6] = "APLICACIONES con modificaci" + "\u00f3"
	+ "n para el mes ACTUAL";
	textosGraficas[7] = "Modificaci" + "\u00f3"
	+ "n por TIPO DE COMPONENTE para el mes ACTUAL";
	textosGraficas[8] = "Modificaci" + "\u00f3"
	+ "n por PLATAFORMA para el mes ACTUAL";
	textosGraficas[9] = "APLICACIONES con modificaci" + "\u00f3"
	+ "n para el mes ACTUAL";
	textosGraficas[10] = "Modificaci" + "\u00f3"
	+ "n por TIPO DE COMPONENTE para el mes ACTUAL";
	textosGraficas[11] = "Modificaci" + "\u00f3"
	+ "n por PLATAFORMA para el mes ACTUAL";
	textosGraficas[12] = "Comportamiento HIST" + "\u00D3"
	+ "RICO por COMPONENTE con mayor n" + "\u00FA"
	+ "mero de liberaciones durante el A" + "\u00D1" + "O";
	textosGraficas[13] = "INCIDENTES relacionados con LIBERACIONES del ciclo anterior";
	$j("#popUpDetalles").text(textosGraficas[tipoGrafica]);
}
/**/
/**
 * Funcion que muestra el calendario con detalles del dia seleccionado
 *
 * @return
 */
function verCalendario(titulo) {
	var tituloMeses = $j(".ui-datepicker-title");
	var urlComponentesAnualCmp = rutaServlet + "4&componente=" + titulo + "&";
	$j("#backgroundPopupMonth").hide();
	$j("#backgroundPopup").hide();
	disablePopup(popUpBackgroundSchDay, popUpContentSchDay);
	$j("#popUpTitleCalendar").text("Componente " + titulo);
	$j("#textCalendar").text(
			"Fechas en las que el componente " + titulo + " present" + "\u00f3"
			+ " modificaci" + "\u00f3" + "n" + " en Producci"
			+ "\u00f3" + "n.");
	$j("#htmlCmp")
	.html(
			"<a target='_blank' href=\""
			+ escaparURI(urlComponentesAnualCmp)
			+ "tipo_salida="
			+ 0
			+ "\"><img border='0' src='images/htmlIcon.png'/></a><br/>");
	$j("#pdfCmp")
	.html(
			"<a target='_blank' href=\""
			+ escaparURI(urlComponentesAnualCmp)
			+ "tipo_salida="
			+ 1
			+ "\"><img border='0' src='images/adobeIcon.png'/></a><br/>");
	$j("#excelCmp")
	.html(
			"<a target='_blank' href=\""
			+ escaparURI(urlComponentesAnualCmp)
			+ "tipo_salida="
			+ 2
			+ "\"><img border='0' src='images/excelIcon.png'/></a><br/>");
//	$j("#ayuda").html("<a target='_blank'><img border='0'
//	src='images/ayuda.png'/></a><br/>");
	/* Centra el popUp */
	centerPopup(popUpBgCalendar, popUpCntCalendar);
	/* Carga el popUp */
	loadPopup(popUpBgCalendar, popUpCntCalendar);
	/* Cierra el popUp */
	$j("#popupContactCloseCalendar").click(function() {
		disablePopup(popUpBgCalendar, popUpCntCalendar);
	});
	/* Deshabilita el popUp */
	$j(popUpBgCalendar).click(function() {
		disablePopup(popUpBgCalendar, popUpCntCalendar);
	});
	/* Evento al dar click al boton 'Regresar' */
	$j(".cerrarCalendario").click(function() {
		disablePopup(popUpBgCalendar, popUpCntCalendar);
	});
	obtenerFechaOnclick(tituloMeses);
}
/**
 * Funcion que obtiene el mes y a�o seleccionado por el usuario en el calendario
 *
 * @param titulo
 * @return
 */
function obtenerFechaOnclick(titulo) {
	titulo.click(function() {
		disablePopup(popUpBgCalendar, popUpCntCalendar);
		var mesSeleccionado = this.getFirst();
		var anioSeleccionado = this.getLast();
		var backgroundDetalleMes = "#backgroundPopupMonth";
		var contentDetalleMes = "#popupContactMonth";
		var componente = tituloCalendario;
		logger.trace("El mes seleccionado es: " + mesSeleccionado.textContent);
		logger
		.trace("El anio seleccionado es: "
				+ anioSeleccionado.textContent);
		logger.trace("El componente es: " + componente);
		/*
		 * Obtiene las fechas agendadas y las compara con la fecha seleccionada,
		 * si la fecha seleccionada esta agendada, muestra un popUp
		 */
		for (i = 0; i < scheduledDays.length; i++) {
			var mes = parseInt(scheduledDays[i].split("-")[1], 10);
			var dia = parseInt(scheduledDays[i].split("-")[2], 10);
			var anio = parseInt(scheduledDays[i].split("-")[0]);
			var arregloMes = [ '', 'Enero', 'Febrero', 'Marzo', 'Abril',
			                   'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre',
			                   'Octubre', 'Noviembre', 'Diciembre' ];
//			logger.trace("Mes agendado: " + mes + " Dia: " + dia + " Anio: "
//			+ anio);
			if (anioSeleccionado.textContent == anio
					&& mesSeleccionado.textContent == arregloMes[mes]) {
				logger.info("Dentro de if..");
//				codigo JSON
				var arregloPaquetes = cambiosTocados.filter(function(item,
						index) {
					if (item.id.componente == componente
							&& (item.id.fechaInstalacion.getMonth() + 1) == mes
							&& item.id.fechaInstalacion.getFullYear() == anio) {
						return true;
					}
				});
				logger.info("CAMBIOS EN EL MES: " + arregloPaquetes.length);
				var titulo = "#popUpTitleMonth";
				var tabla = "#table_idMonth";
				muestraComponentes(titulo, tabla, arregloPaquetes);
				/* Centra el popUp */
				centerPopup(backgroundDetalleMes, contentDetalleMes);
				/* Carga el popUp */
				loadPopup(backgroundDetalleMes, contentDetalleMes);
//				muestraComponentes(titulo,tabla,arreglo);
				/* Cierra el popUp */
				$j("#popupContactCloseMonth").click(
						function() {
							disablePopup("#backgroundPopupMonth",
							"#popupContactMonth");
						});
				/* Deshabilita el popUp */
				$j("#backgroundPopupMonth").click(
						function() {
							disablePopup("#backgroundPopupMonth",
							"#popupContactMonth");
						});
				/* Evento al dar click al boton 'Regresar' */
				$j('.regresarCalendario').click(function() {
					$j("#popupContactMonth").hide();
					verCalendario(componente);
				});
				/* Evento al dar click al boton 'Salir' */
				$j('.cerrarDetalle').click(
						function() {
							disablePopup("#backgroundPopupMonth",
							"#popupContactMonth");
						});
				isScheduled = true;
			} else {
				isScheduled = false;
			}
		}
	});
}
function muestraComponentes(idTitulo, idTabla, arreglo) {
	$j(idTitulo).text("Componente " + tituloCalendario);
	logger.trace("Antes de dibujar tabla");
	$j(idTabla).html("");
	logger.trace("Despues de aplicar html");
	$j(idTabla)
	.append(
	"<thead><tr><th width='100px'><b>Fecha</b></th><th width='80px' height='30'><b>Hora</b></th><th width='100px'><b>Clave</b></th><th width='100px'><b>Paquete</b></th></tr></thead>");
	logger.trace("Se dibujo cabecera");
	var contenido = "<tbody>";
	arreglo.each(function(item, index) {
		logger.trace("Dibujando rows");
		contenido = contenido + "<tr>" + "<td>"
		+ item.id.fechaInstalacion.getDate() + "/"
		+ (item.id.fechaInstalacion.getMonth() + 1) + "/"
		+ item.id.fechaInstalacion.getFullYear() + "</td>" + "<td>"
		+ item.id.horaInstalacion.getHours() + ":"
		+ item.id.horaInstalacion.getMinutes() + "</td>" + "<td>"
		+ item.id.codigoAplicacion + "</td>" + "<td>"
		+ item.id.codigoAplicacion + "@" + item.id.folio + "</td>"
		+ "</tr>";
		logger.trace("Rows dibujadas");
	});
	$j(idTabla).append(contenido + "</tbody>");
	logger.trace("Mostrando info");
}
/**
 * Funcion que carga el popUp solo si esta deshabilitado
 *
 * @return
 */
function loadPopup(popUpBackground, popUpContent) {
	/* Carga el popUp */
	if (popupStatus == 0) {
		$j(popUpBackground).css({
			'opacity' : '0.7'
		});
		$j(popUpBackground).fadeIn('slow');
		$j(popUpContent).fadeIn('slow');
		popupStatus = 1;
	}
}
/**
 * Funcion que deshabilita el popUp solo si esta habilitado
 *
 * @return
 */
function disablePopup(popUpBackground, popUpContent) {
	/* Deshabilita el popUp */
	if (popupStatus == 1) {
		$j(popUpBackground).fadeOut('slow');
		$j(popUpContent).fadeOut('slow');
		popupStatus = 0;
	}
}
/**
 * Funcion que centra el popUp con respecto a la pagina actual
 *
 * @return
 */
function centerPopup(popUpBackground, popUpContent) {
	logger.info("en center...");
	/* Se obtienen datos de la pagina */
	var windowWidth = document.documentElement.clientWidth;
	var windowHeight = document.documentElement.clientHeight;
	var popupHeight = $j(popUpContent).height();
	var popupWidth = $j(popUpContent).width();
	logger.info("PopUp Content es..." + popUpContent);
	/* Centrando popUp */
	$j(popUpContent).css({
		'position' : 'absolute',
		'top' : windowHeight / 2 - popupHeight / 2,
		'left' : windowWidth / 2 - popupWidth / 2
	});
	/* Unicamente para IE6 */
	$j(popUpBackground).css({
		'height' : windowHeight
	});
}
/*
 * Funcion que muestra empresa y fecha seleccionados por el usuario, si no
 * existen cancelaciones relacionados con los cambios se muestra la leyenda
 * Sitio en Construccion
 */
function muestraInformacion(cancelaciones, numAbends) {
	var nombreEmpresa;
	var empresas = [];
	var anio = fecha.split("-")[0];
	var mes = parseInt(fecha.split("-")[1], 10);
	var dia = parseInt(fecha.split("-")[2], 10);
	var meses = new Array("Enero", "Febrero", "Marzo", "Abril", "Mayo",
			"Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
	"Diciembre");
	fechaActual = meses[mes - 1] + " " + dia + ", " + anio;
	empresas[0] = "Mexico";
	empresas[1] = "Puerto Rico";
	empresas[2] = "Chile";
	empresas[3] = "Peru";
	empresas[4] = "Colombia";
	empresas[5] = "AFP Mexico";
	empresas[6] = "AFP Chile";
	empresas[7] = "AFP Peru";
	empresas[8] = "AFP Colombia";
	/* Muestra el nombre y bandera del pais seleccionado */
	switch (codigoEmpresa) {
	case "AFPCL":
		nombreEmpresa = "AFP CHILE";
		$j("#banderaEmpresa").html("<img border='0' src='images/chile.gif'/>");
		break;
	case "AFPCO":
		nombreEmpresa = "AFP COLOMBIA";
		$j("#banderaEmpresa").html(
		"<img border='0' src='images/colombia.gif'/>");
		break;
	case "AFPMX":
		nombreEmpresa = "AFP M" + "\u00C9" + "XICO";
		$j("#banderaEmpresa").html("<img border='0' src='images/mexico.gif'/>");
		break;
	case "AFPPE":
		nombreEmpresa = "AFP PER" + "\u00DA";
		$j("#banderaEmpresa").html("<img border='0' src='images/peru.gif'/>");
		break;
	case "CL":
		nombreEmpresa = "CHILE";
		$j("#banderaEmpresa").html("<img border='0' src='images/chile.gif'/>");
		break;
	case "CO":
		nombreEmpresa = "COLOMBIA";
		$j("#banderaEmpresa").html(
		"<img border='0' src='images/colombia.gif'/>");
		break;
	case "MX":
		nombreEmpresa = "M" + "\u00C9" + "XICO";
		$j("#banderaEmpresa").html("<img border='0' src='images/mexico.gif'/>");
		break;
	case "PE":
		nombreEmpresa = "PER" + "\u00DA";
		$j("#banderaEmpresa").html("<img border='0' src='images/peru.gif'/>");
		break;
	case "PR":
		nombreEmpresa = "PUERTO RICO";
		$j("#banderaEmpresa").html(
		"<img border='0' src='images/puertoRico.gif'/>");
		break;
	}
	$j("#nombreEmpresa").text(nombreEmpresa);
	$j("#fechaActual").html("<b>" + fechaActual + "</b>");
	/* Si no existen incidentes mostr� la leyenda Sitio en Construccion */
	if (cancelaciones.length < 1 && numAbends < 1) {
		$j(".incidentes").hide();
		$j("#graficaCambiosPlt").hide();
		$j("#graficaNoDisp").show();
	}
}
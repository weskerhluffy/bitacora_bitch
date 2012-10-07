	/**
 * 
 */
var htmlInputHiddenCanceladoDocumentacion = "<input type='hidden' name=\"canceladosDocumentacionsSel[{numFila}].{propiedad}\" value=\"{valor}\"/>";
$(function() {
	// Cada que cambia el área seleccionada se cambia el valor
	// del
	// hidden que contiene el valor del id del area seleccionada
	// y se
	// publica el evento para que se recarge la lista de
	// problemas
	$("#slcAreas").change(function() {
		$("#hdnAreaSel").val($(this).val());
		$.publish("recargarProblemas");
	});

	$.subscribe("enviarForma", enviarForma);

});

function fecCicloPick(elem) {
	$(elem).datepicker({
		dateFormat : "yy-mm-dd",
		constrainInput : false
	});
	$('#ui-datepicker-div').css("z-index", 2000);
};

/**
 * Antes de enviar la forma, convierte cada fila de la tabla en un monton de
 * inputs que hacen referencia a un objeto <code>DocumentacionCancelado</code>.
 * Los atributos que se envian para cada objeto son:
 * <ul>
 * <li> fhCiclo
 * <li> nbGrupo
 * <li> nbProceso
 * <li> cdPaso
 * </ul>
 */
function enviarForma(event, data) {
	logger.trace("Enviado forma");
	var idsRegistros = $("#tblCanceladoDocumentacions").getDataIDs();
	var idRegistro = null;
	var registro = null;
	var fhCiclo = null;
	var nbGrupo = null;
	var nbProceso = null;
	var cdPaso = null;
	var htmlInputHiddenCanceladoDocumentacionSus = "";
	var i = 0;
	for (i = 0; i < idsRegistros.length; i++) {
		idRegistro = idsRegistros[i];
		registro = $("#tblCanceladoDocumentacions").getRowData(idRegistro);
		logger.trace("El registro es " + introspect("shit", registro, "-", 1));

		fhCiclo = registro["fhCiclo"];
		nbGrupo = registro["nbGrupo"];
		nbProceso = registro["nbProceso"];
		cdPaso = registro["cdPaso"];

		htmlInputHiddenCanceladoDocumentacionSus = htmlInputHiddenCanceladoDocumentacion
				.substitute({
					numFila : i,
					propiedad : "fhCiclo",
					valor : fhCiclo + "T00:00:00"
				});
		logger.trace("El html es " + htmlInputHiddenCanceladoDocumentacionSus);
		$("#divCanceladoDocumentacions").append(
				htmlInputHiddenCanceladoDocumentacionSus);

		htmlInputHiddenCanceladoDocumentacionSus = htmlInputHiddenCanceladoDocumentacion
				.substitute({
					numFila : i,
					propiedad : "nbGrupo",
					valor : nbGrupo
				});
		logger.trace("El html es " + htmlInputHiddenCanceladoDocumentacionSus);
		$("#divCanceladoDocumentacions").append(
				htmlInputHiddenCanceladoDocumentacionSus);

		htmlInputHiddenCanceladoDocumentacionSus = htmlInputHiddenCanceladoDocumentacion
				.substitute({
					numFila : i,
					propiedad : "nbProceso",
					valor : nbProceso
				});
		logger.trace("El html es " + htmlInputHiddenCanceladoDocumentacionSus);
		$("#divCanceladoDocumentacions").append(
				htmlInputHiddenCanceladoDocumentacionSus);

		htmlInputHiddenCanceladoDocumentacionSus = htmlInputHiddenCanceladoDocumentacion
				.substitute({
					numFila : i,
					propiedad : "cdPaso",
					valor : cdPaso
				});
		logger.trace("El html es " + htmlInputHiddenCanceladoDocumentacionSus);
		$("#divCanceladoDocumentacions").append(
				htmlInputHiddenCanceladoDocumentacionSus);

	}
	$.blockUI({
		message : "Procesando, espere por favor"
	});
	$("#frmDocumentarAdelantado").submit();
}
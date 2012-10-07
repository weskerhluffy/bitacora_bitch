/**
 * 
 */
var opcionesSubmitFormaDocumentacion = null;
var estadoSel = null;
var htmlTablaRecuperacion = "<tr><td>{hora}</td><td>{usuario}</td><td>{recuperacion}</td></tr>";
var problemasDisponibles = {};
jQuery(document).ready(
		function() {
			// Se crea el objeto de opciones para submit ajax de la
			// forma de documentación
			opcionesSubmitFormaDocumentacion = {
				dataType : "text",
				success : procesarRespuestaValidacionForma,
				beforeSubmit : function(formData, jqForm, options) {
					// Antes de submitir, se limpian los errores de la
					// forma
					$.publish("removeErrors");
				}
			};
			// La lógica de enviar documentación
			$.subscribe("enviarDocumentacion", enviarDocumentacion);
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
			$.subscribe("problemaSeleccionado", refrescaProblema);

			$.subscribe("enviarDocumentacionRecuperacion",
					enviarDocumentacionRecuperacion);

			// Al publicar el evento "removeErrors" los marcadores de
			// error de la forma se borran
			$.subscribe("removeErrors", function(event, data) {
				$(".errorLabel").html("").removeClass("errorLabel");
				$("#formerrors").html("");
			});

			dwr.engine.setActiveReverseAjax(true);
		});

/**
 * Pinta los errores regresados por el servidor de manera asíncrona
 * 
 * @param form
 *            El objeto de la forma envuelto en jquery
 * @param errors
 *            La lista de errores regresada por el servidor
 */
function customeValidation(form, errors) {

	// Lista de errores
	var list = $("#formerrors");

	// Mostrar errores de acción
	if (errors.errors) {
		$.each(errors.errors, function(index, value) {
			list.append("<li>" + value + "</li>");
		});
	}

	// Mostrar errores de campos
	if (errors.fieldErrors) {
		$.each(errors.fieldErrors, function(index, value) {
			var partesNombre = index.split(".");
			if (partesNombre.length > 1) {
				index = partesNombre[partesNombre.length - 1];
			}
			logger.trace("El index del error " + index + " y su valor "
					+ value[0]);
			var elem = $("#" + index + "Error");
			if (elem) {
				$(elem).addClass("errorLabel");
				for ( var int = 0; int < value.length; int++) {
					var msgError = value[int];
					$(elem).append("<br/>" + msgError);
				}
			}
		});
	}
};

/**
 * Incluye en el objeto de opciones de submit ajax (de jquery.form)
 * <code>opcionesSubmit</code> los elementos de la forma <code>idForma</code>
 * que por alguna razón no se van a incluir en el submit.
 */
function completarDatosForma(idForma, opcionesSubmit) {
	var inputs = $("#" + idForma + " input");
	var selects = $("#" + idForma + " select");
	var areasTexto = $("#" + idForma + " textarea");
	for ( var int = 0; int < selects.length; int++) {
		var select = selects[int];
		inputs.push(select);
	}
	for ( var int = 0; int < areasTexto.length; int++) {
		var areaTexto = areasTexto[int];
		inputs.push(areaTexto);
	}
	var nombresInputs = [];
	var valores = {};
	var nombreInput = "";
	var elementosForm = "";
	var valorInput = "";
	logger.trace("Se encontraron " + inputs.length + " inputs en la forma "
			+ idForma);
	for ( var int = 0; int < inputs.length; int++) {
		nombreInput = $(inputs[int]).attr("name");
		nombresInputs.include(nombreInput);
		valores[nombreInput] = $("[name='" + nombreInput + "']").fieldValue().length > 0 ? $(
				"[name='" + nombreInput + "']").fieldValue()[0]
				: "";
		logger.trace("El valor de " + nombreInput + " es "
				+ valores[nombreInput]);
	}
	elementosForm = $("#" + idForma + "").formSerialize();
	opcionesSubmit.data = {};
	logger.trace("Los elementos del form son " + elementosForm);
	for ( var int2 = 0; int2 < nombresInputs.length; int2++) {
		nombreInput = nombresInputs[int2];
		valorInput = valores[nombreInput];
		logger.trace("buscando El elemento " + nombreInput);
		if (!elementosForm.contains(nombreInput)) {
			logger.trace("No se encontro");
			opcionesSubmit.data[nombreInput] = valorInput;
			logger.trace("Poniendo a el valor " + " " + valorInput);
		}
	}
};

/**
 * Refresca el listado de acciones de recuperación de acuerdo a lo que se tenga
 * en ese momento en el servidor
 * 
 * @param recuperaciones
 *            Los objetos <code>Documentacion</code> correspondientes al grupo
 *            de cancelados actuales
 */
function refrescarRecuperaciones(recuperaciones) {
	logger.trace("LLegaron " + recuperaciones + " recuperaciones");
	var recuperacion = null;
	var htmlTablaRecuperacionSustituido = "";
	var domTablaRecuperacion = $("#tbdRecuperaciones");
	domTablaRecuperacion.empty();
	for ( var int = 0; int < recuperaciones.length; int++) {
		recuperacion = recuperaciones[int];
		logger.trace("Iterando en la recuperación "
				+ recuperacion.tmDocumentacion);
		htmlTablaRecuperacionSustituido = htmlTablaRecuperacion.substitute({
			hora : recuperacion.tmDocumentacion.format("%Y-%m-%d %H:%M:%S"),
			usuario : recuperacion.usuario.cdLdap,
			recuperacion : recuperacion.txRecuperacion
		});
		domTablaRecuperacion.append(htmlTablaRecuperacionSustituido);
	}
};

function refrescaProblema(event, data) {
	var idProblemaSel;
	var problemaSel;
	if (event.originalEvent.data) {
		problemasDisponibles = event.originalEvent.data.problemasDisponibles;
		logger.trace("Llegaron " + problemasDisponibles.length
				+ " nuevos problemas");
	}
	idProblemaSel = $("#slcProblemas").val();
	logger.trace("El problema seleccionado es " + idProblemaSel);
	for ( var int = 0; int < problemasDisponibles.length; int++) {
		if (problemasDisponibles[int].cdProblema == idProblemaSel) {
			problemaSel = problemasDisponibles[int];
			logger.trace("Se encontro el problema " + problemaSel.nbProblema);
			$("#lblResponsabilidad").html(problemaSel.txResponsabilidad);
			$("#lblCorrige").text(problemaSel.txCorrige);
		}
	}
};

/**
 * Se envia la forma de documentación para su validación, aunque solo se tomara
 * el campo de recuperación. Se valida antes de enviar:
 * <ul>
 * <li> Si es la primera documentación, si ese es el caso, se restringe el uso
 * de esta capacidad.
 * <li> Que no haya errores ortográficos, si los hay, se le pregunta al usuario
 * si quiere enviar con dichos errores la forma.
 * </ul>
 * 
 * @param event
 * @param data
 */
function enviarDocumentacionRecuperacion(event, data) {

	logger.debug("Enviando documentación para validación");
	if (!revisarErroresOrtograficosRecuperacion()) {
		if (!confirm("La recuperación contiene errores de Ortografia,\n¿esta seguro de lo que hace, realmente no le importa?")) {
			return;
		}
	}

	$.blockUI({
		message : "Procesando, espere por favor"
	});
	// Se completan los datos de la
	// forma
	completarDatosForma("frmDocumentarCancelado",
			opcionesSubmitFormaDocumentacion);
	// Se clona el objeto de
	// configuración de la petición ajax
	var opcionesSubmitFormaDocumentacionRecuperacion = Object
			.clone(opcionesSubmitFormaDocumentacion);
	// Si los cancelados aún no han sido
	// documentados por
	// primera
	// vez, se solicita al usuario usar el
	// botón de guardar cambios.
	if ($("#hdnCanceladoDocumentado").val() == "true") {
		logger.trace("Ya se documento una vez");
		opcionesSubmitFormaDocumentacionRecuperacion.url = $("#hdnRutaContexto")
				.val()
				+ "/documentacion!anadirRecuperacion";
		opcionesSubmitFormaDocumentacionRecuperacion.success = procesarRespuestaValidacionAnadirRecuperacion;

		logger.debug("Submitiendo con ajax la forma ");

		$("#frmDocumentarCancelado").ajaxSubmit(
				opcionesSubmitFormaDocumentacionRecuperacion);
	} else {
		$("#lnkEnviarRecuperacion")
				.feedback(
						"Este es el primer registro de documentación para estos cancelados, por favor, utilice el botón de aceptar.",
						{
							duration : 3000,
							right : true
						});
		$.unblockUI();
	}
};

/**
 * Se procesa la respuesta de la validación del servidor. Si es exitosa, se
 * envia de nuevo la forma sin las opciones de validación, de manera que se
 * procese normalmente la petición. Si es fallida, se envia un mensaje de error.
 * 
 * @param responseText
 * @param statusText
 * @param xhr
 * @param $form
 *            El wrapper jquery para la forma que se envió.
 */
function procesarRespuestaValidacionAnadirRecuperacion(responseText,
		statusText, xhr, $form) {
	logger.debug("En la logica de envio de recuperacion");
	var respuesta = responseText.replace("/*", "");
	respuesta = respuesta.replace("*/", "");
	var errores = JSON.decode(respuesta);
	if (!errores.errors && !errores.fieldErrors) {
		// Si no hay errores y solo
		// se
		// validaron, se
		// reenvian sin las banderas
		// de solo
		// validacion. Si
		// no hay errores y no solo
		// se
		// valido, se
		// rehabilitan las banderas
		// de solo
		// validación y se
		// desbloquea la interfaz

		$("#hdnValidarSolamente").attr("disabled", "disabled");
		$("#hdnValidacionJSON").attr("disabled", "disabled");
		$("#frmDocumentarCancelado").ajaxSubmit(
				{
					url : $("#hdnRutaContexto").val()
							+ "/documentacion!anadirRecuperacion",
					dataType : "text",
					success : function() {
						$("#hdnValidarSolamente").removeAttr("disabled");
						$("#hdnValidacionJSON").removeAttr("disabled");
						estadoSel = $("[name='model.cdEstado']:checked").val();
						logger.trace("El valor del estado es " + estadoSel);
						$.unblockUI();
					}
				});
	} else {
		customeValidation($("#frmDocumentarCancelado"), errores);
		$("#lnkEnviarRecuperacion").feedback(
				"Por favor, ingrese los campos indicados de manera correcta", {
					duration : 3000,
					right : true
				});
		$.unblockUI();
	}
};

/**
 * Revisa los errores ortográficos del ckeditor de recuperación.
 * 
 * @returns {Boolean} True si no hay ningún error el texto de recuperación,
 *          falso en caso contrario.
 */
function revisarErroresOrtograficosRecuperacion() {
	var iframe;
	var erroresOrtograficos;
	var sinErrores = true;
	logger.trace("Revisando ortografia");
	iframe = $("#cke_txaDocumentacionRecuperacion iframe[userdictionaryname]");

	erroresOrtograficos = iframe.contents().find("span[data-scaytid]");
	logger.trace("llegare a la meta " + iframe.attr("title"));
	logger.trace("en tus ojos hay " + erroresOrtograficos.length);
	if (erroresOrtograficos && erroresOrtograficos
			&& erroresOrtograficos.length > 0) {
		sinErrores = false;
	}
	return sinErrores;
};

/**
 * Se envia via ajax la forma de documentación para su validación.
 * 
 * @param event
 * @param data
 */
function enviarDocumentacion(event, data) {
	if (!revisarErroresOrtograficos()) {
		if (!confirm("La documentación contiene errores de ORTOgrafia,\n¿esta seguro de lo que hace, realmente no le importa?")) {
			return;
		}
	}
	completarDatosForma("frmDocumentarCancelado",
			opcionesSubmitFormaDocumentacion);
	$.blockUI({
		message : "Procesando, espere por favor"
	});
	// Se submite la forma de manera
	// asincrona
	$("#frmDocumentarCancelado").ajaxSubmit(opcionesSubmitFormaDocumentacion);

};

/**
 * Se verifica la validación regresada por el servidor, si contiene errores, se
 * muestran en pantalla, si no, se remueven las banderas de solo validación para
 * submitir la forma de manera normal.
 * 
 * @param responseText
 * @param statusText
 * @param xhr
 * @param $form
 */
function procesarRespuestaValidacionForma(responseText, statusText, xhr, $form) {
	var respuesta = responseText.replace("/*", "");
	respuesta = respuesta.replace("*/", "");
	var errores = JSON.decode(respuesta);
	logger.trace("La cadena para convertir en JSON es " + respuesta);
	// Si no hubo ningun error, se submite la forma
	if (!errores.errors && !errores.fieldErrors) {
		$("#hdnValidarSolamente").remove();
		$("#hdnValidacionJSON").remove();
		$("#frmDocumentarCancelado").submit();
	} else {
		customeValidation($("#frmDocumentarCancelado"), errores);
		$.unblockUI();
	}
};

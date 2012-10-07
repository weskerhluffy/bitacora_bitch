function generarEnviarFormaAjax(idForma, opcionesSubmit) {
	var funcionEnviar = function() {
		logger.trace("Enviando forma " + idForma + " de manera asíncrona");
		$("#" + idForma).ajaxSubmit(opcionesSubmit);
	};
	return funcionEnviar;
};

function validaErrores(responseText, statusText, xhr, $form) {
	var respuesta = evaluaRespuestaStruts(responseText);
	if (respuesta && !respuesta.errors && !respuesta.fieldErrors) {
		validacionExitosa($form, respuesta);
	} else {
		despliegueErroresValidacion($form, respuesta);
	}
};

function evaluaRespuestaStruts(data) {
	var respuesta = data.replace("/*", "");
	respuesta = respuesta.replace("*/", "");
	var errores = JSON.decode(respuesta);
	return errores;
};


$(document).ready(function() {
logger.trace("cargando...");
	$("a").click(function() {
		logger.trace("cargado el efecto");
		$(this).show("explode", 450);

	}
	
	);

});
var diasOrg = new Array();// DiasInhabiles

var diasSel = new Array();// DiasSeleccionados

/**
 * Funcion para insertar fechas en el arreglo
 */
function copiarFechas() {
	var arrTemporal=new Array();
	var fechaCaca;
	
	
	for(var i=0;i<$(".fechasOriginales").length;i++){
		fechaCaca=$($(".fechasOriginales")[i]);
		diasOrg.push(fechaCaca.val());
	}

}

function crearHiddens(arr) {
	for ( var i = 0; i < arr.length; i++) {
		var input = "<input name='fechasSel' type='hidden' value='" + arr[i]
		+ "' />";
		$("#frmDiaInhabil").append(input);
	}
	return true;
}

/**
 * Funcion que transforma el formato de date a yy-mm-dd de DatePicker
 */
function formatoFecha(date) {
	var current;
	var d;
	var m;
	var y = date.getFullYear();

	if (date.getMonth() < 9) {
		m = '0' + (date.getMonth() + 1);
	} else {
		m = (date.getMonth() + 1);
	}

	if (date.getDate() < 10) {
		d = '0' + date.getDate();
	} else {
		d = date.getDate();
	}

	current = y + '-' + m + '-' + d;
	return current;
}

/**
 * Funcion para revisar dias inhabiles
 */
function diasInhabiles(date) {

	var current = formatoFecha(date);

	return jQuery.inArray(current, diasSel) == -1 ? [ true, '' ] : [ true,
	                                                                 'ui-state-default ui-state-active' ];

}

/**
 * Funcion para deshabilitar los dias
 */
function marcarDiasInhabiles(date) {
	var noWeekend = jQuery.datepicker.noWeekends(date);
	return noWeekend[0] ? diasInhabiles(date) : noWeekend;
}

function verificarDiasInhabiles(dateText, inst) {
	if (jQuery.inArray(dateText, diasSel) == -1) {
		diasSel.push(dateText);
		return [ true, 'ui-state-default ui-state-active' ];
	} else {
		diasSel.splice(diasSel.indexOf(dateText), 1);
		return [ true, '' ];

	}
}

$(document).ready(
		function() {
			var fechaActual = new Date();

			var ultimoDiaAnio = new Date(fechaActual.getFullYear(), 11, 31);
			var primerDiaAnio = new Date(fechaActual.getFullYear(), 0, 1);


			copiarFechas(); 
			diasSel=diasOrg;



			/* Configuracion inicial DatePicker */
			$("#datepicker").datepicker(
					{
						dateFormat : 'yy-mm-dd',
						monthNames : [ 'Enero', 'Febrero', 'Marzo', 'Abril',
						               'Mayo', 'Junio', 'Julio', 'Agosto',
						               'Septiembre', 'Octubre', 'Noviembre',
						               'Diciembre' ],
						               minDate : primerDiaAnio, // '-0m',
						               maxDate : ultimoDiaAnio,
						               numberOfMonths : [ 3, 4 ],
						               dayNamesMin : [ 'Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi',
						                               'Sa' ],
						                               hideIfNoPrevNext : true,
						                               beforeShowDay : marcarDiasInhabiles,
						                               onSelect : verificarDiasInhabiles
					});
			$("#datepicker").datepicker("refresh");

			$("#anio").change(
					function() {

						$("#datepicker").datepicker("option", "maxDate",
								new Date($(this).val(), 11, 31));
						$("#datepicker").datepicker("option", "minDate",
								new Date($(this).val(), 0, 1));
					});

			$("#frmDiaInhabil").submit(function(e) {
				//e.preventDefault();
				
				crearHiddens(diasSel);
			});
			
			$("#rdoEmpresas").change(function() {
				location.href=$("#hdnRutaContexto").val()+"/catalogo-dias-inhabiles?empSel="+$(this).val();
			});

		});
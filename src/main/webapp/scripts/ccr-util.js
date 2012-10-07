/**
 * Funciones y constantes javascript de utilidad común.

 * 
 * @autor Hiram Ernesto Alvarado Gaspar
 * 
 * Fuentes: 
 * http://javascript.crockford.com/remedial.html
 * http://www.syger.it/Tutorials/JavaScriptIntrospector.html
 */
/**
 * Objeto que modifica los controles del plugin de DataTables de jQuery
 */
var localizacionDataTables = {
	oLanguage : {
		oPaginate : {
			sFirst : "Primera página",
			sNext : "Última página",
			sNext : "Última página",
			sPrevious : "Página previa"
		},
		sProcessing : "Procesando al máximo",
		sZeroRecords : "No hay records para mostrar",
		sLengthMenu : "Mostrar <select>" + '<option value="10">10</option>'
				+ '<option value="20">20</option>'
				+ '<option value="30">30</option>'
				+ '<option value="40">40</option>'
				+ '<option value="50">50</option>'
				+ '<option value="-1">todos</option>' + '</select> registros'
	}
};

;
/**
 * Encuentra el tipo de entidad de javascript de <code>value</code>. La
 * capacidad es la misma que la funcion <code>typeof</code>, pero añade la
 * capacidad de detectar arreglos.
 * 
 * @param value
 *            Entidad de javascript a evaluar
 * @return El tipo de dato de la entidad
 */
function typeOf(value) {
	var s = typeof value;
	if (s === 'object') {
		if (value) {
			if (typeof value.length === 'number'
					&& !(value.propertyIsEnumerable('length'))
					&& typeof value.splice === 'function') {
				s = 'array';
			}
		} else {
			s = 'null';
		}
	}
	return s;
};

function isEmpty(o) {
	var i, v;
	if (typeOf(o) === 'object') {
		for (i in o) {
			v = o[i];
			if (v !== undefined && typeOf(v) !== 'function') {
				return false;
			}
		}
	}
	return true;
};

String.prototype.entityify = function() {
	// alert("Dentro de entitify");
	var CARACTERES_ESPECIALES = new Hash({
		"Á" : "&Aacute;",
		"É" : "&Eacute;",
		"Í" : "&Iacute;",
		"Ó" : "&Oacute;",
		"Ú" : "&Uacute;",
		"á" : "&aacute;",
		"é" : "&eacute;",
		"í" : "&iacute;",
		"ó" : "&oacute;",
		"ú" : "&uacute;",
		"Ñ" : "&Ntilde;",
		"ñ" : "&ntilde;",
		"Ü" : "&Uuml;",
		"ü" : "&uuml;",
		"¿" : "&iquest;",
		"?" : "&#63;"
	});

	var thisTmp = this;
	CARACTERES_ESPECIALES.each(function(entidadHTML, caracter) {
		alert("Reemplazando " + caracter + " por " + entidadHTML + " en "
				+ thisTmp);
		thisTmp = thisTmp.replace(/Á/g, "entidadHTML");
	});
	// alert("La cadena a regresar "+thisTmp);
	return thisTmp.replace(/&/g, "&amp;").replace(/</g, "&lt;");
	// "");
};

String.prototype.quote = function() {
	var c, i, l = this.length, o = '"';
	for (i = 0; i < l; i += 1) {
		c = this.charAt(i);
		if (c >= ' ') {
			if (c === '\\' || c === '"') {
				o += '\\';
			}
			o += c;
		} else {
			switch (c) {
			case '\b':
				o += '\\b';
				break;
			case '\f':
				o += '\\f';
				break;
			case '\n':
				o += '\\n';
				break;
			case '\r':
				o += '\\r';
				break;
			case '\t':
				o += '\\t';
				break;
			default:
				c = c.charCodeAt();
				o += '\\u00' + Math.floor(c / 16).toString(16)
						+ (c % 16).toString(16);
			}
		}
	}
	return o + '"';
};

String.prototype.supplant = function(o) {
	return this.replace(/{([^{}]*)}/g, function(a, b) {
		var r = o[b];
		return typeof r === 'string' || typeof r === 'number' ? r : a;
	});
};

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g, "");
};

/**
 * Checa si la propiedad <code>name</code> es del tipo <code>type</code> en
 * el objeto <code>obj</code>.
 * 
 * @param obj
 *            El objeto a introspeccionar
 * @param name
 *            El nombre de la propiedad
 * @param type
 *            El tipo de la propiedad (opcional, el default is "function").
 * @returns true Si la propiedad existe y es del tipo dado, de otro modo false
 */

function existePropiedad(obj, name, type) {
	type = type || "function";
	return (obj ? this.typeOf(obj[name]) : "null") === type;
};

/**
 * Introspects an object.
 * 
 * @param name
 *            the object name.
 * @param obj
 *            the object to introspect.
 * @param indent
 *            the indentation (optional, defaults to "").
 * @param levels
 *            the introspection nesting level (defaults to 1).
 * @returns a plain text analysis of the object.
 */

function introspect(name, obj, indent, levels) {
	indent = indent || "";
	if (this.typeOf(levels) !== "number")
		levels = 1;
	var objType = this.typeOf(obj);
	var result = [ indent, name, " ", objType, " :" ].join('');
	if (objType === "object") {
		if (levels > 0) {
			indent = [ indent, "  " ].join('');
			for (prop in obj) {
				var prop = this.introspect(prop, obj[prop], indent, levels - 1);
				result = [ result, "\n", prop ].join('');
			}
			return result;
		} else {
			return [ result, " ..." ].join('');
		}
	} else if (objType === "null") {
		return [ result, " null" ].join('');
	}
	return [ result, " ", obj ].join('');
};

/**
 * Revisa los errores ortográficos de los ckeditor que haya en la página.
 * 
 * @returns {Boolean} True si no hay ningún error en ningún ckeditor, falso en
 *          caso contrario.
 */
function revisarErroresOrtograficos() {
	var iframes;
	var iframe;
	var erroresOrtograficos;
	var sinErrores = true;
	logger.trace("Revisando ortografia");
	iframes = $("iframe[userdictionaryname]");

	for ( var int = 0; int < iframes.length; int++) {
		iframe = $(iframes[int]);

		erroresOrtograficos = iframe.contents().find("span[data-scaytid]");
		logger.trace("llegare a la meta " + iframe.attr("title"));
		logger.trace("en tus ojos hay " + erroresOrtograficos.length);
		if (erroresOrtograficos && erroresOrtograficos
				&& erroresOrtograficos.length > 0) {
			sinErrores = false;
			break;
		}
	}
	return sinErrores;
};

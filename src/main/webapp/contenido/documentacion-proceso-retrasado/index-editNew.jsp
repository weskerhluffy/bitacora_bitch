<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Documentar proceso retrasado</title>

</head>
<body>
	<h3>Documentar proceso retrasado</h3>
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" action="bitacora" includeContext="true" />
	<s:form id="frmDocumentacionProcesoRetrasado"
		action="%{#request.contextPath}/documentacion-proceso-retrasado"
		method="post" theme="simple" acceptcharset="UTF-8">
		<h4>Datos de los procesos retrasados:</h4>
		<table class="tablaHoldeado">
			<thead>
				<th>Id</th>
				<th><s:text name="nbOrigenCampo" /></th>
				<th><s:text name="cdCicloCampo" /></th>
				<th><s:text name="nbProcesoCampo" /></th>
			</thead>

			<tbody>

				<s:iterator
					value="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@PROCESOS_RETRASADOS]">
					<tr>
						<td>${id}</td>
						<td><s:property value="origen.nbOrigen" />
						</td>
						<td><s:property value="cdCiclo" />
						</td>
						<td><s:property value="nbProceso" />
						</td>
					</tr>
				</s:iterator>

			</tbody>
		</table>
		<br />
		<sj:checkboxlist
			list="#{'true':'¿Cerrar documentación de proceso retrasado?'}"
			name="cerrarDocumentacionProcesoRetrasado" value="false" />

		<s:url id="urlConfigEditor" value="/META-INF/lib/alex.jar"
			includeContext="true" />
		<sjr:ckeditor id="txaDocumentacionProcesoRetrasado"
			name="model.txDocumentacion" customConfig="%{urlConfigEditor}"
			editorLocal="es" width="920"
			value="%{#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@PROCESOS_RETRASADOS].isEmpty?'':#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@PROCESOS_RETRASADOS][0].ultimaDocumentacionProcesoRetrasado.txDocumentacion}"></sjr:ckeditor>

		<sj:a button="true" buttonIcon="ui-icon-star" key="aceptar"
			formIds="frmDocumentacionProcesoRetrasado">Aceptar</sj:a>
		<sj:a button="true" buttonIcon="ui-icon-cancel"
			href="javascript:void(0);" onclick="location.href='%{urlCancelar}'"
			requestType="GET">Cancelar</sj:a>
	</s:form>
</body>
	</html>
</jsp:root>
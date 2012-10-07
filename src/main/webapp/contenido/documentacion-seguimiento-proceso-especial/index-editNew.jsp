<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Documentar seguimiento proceso especial</title>

</head>
<body>
	<h3>Documentar seguimiento proceso especial</h3>
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" action="bitacora" includeContext="true" />
	<s:form id="frmDocumentacionSeguimientoProcesoEspecial"
		action="%{#request.contextPath}/documentacion-seguimiento-proceso-especial"
		method="post" theme="simple" acceptcharset="UTF-8">
		<h4>Datos del proceso retrasado:</h4>
		<table class="tablaHoldeado">
			<tr>
				<td><label><s:text name="nbOrigenCampo" /> </label>:</td>
				<td><s:property value="seguimientoProcesoEspecial.procesoEspecial.origen.nbOrigen" /></td>
			</tr>
			<tr>
				<td><label><s:text name="nbProcesoCampo" /> </label>:</td>
				<td><s:property value="seguimientoProcesoEspecial.id.nbProceso" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="tmInicioCampo" /> </label>:</td>
				<td><s:date name="seguimientoProcesoEspecial.id.tmInicio"
						format="yyyy-MM-dd hh:mm:ss" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="tmFinCampo" /> </label>:</td>
				<td><s:date name="seguimientoProcesoEspecial.tmFin"
						format="yyyy-MM-dd hh:mm:ss" />
				</td>
			</tr>
		</table>
		<br />
		<label>Cerrar documentacion de seguimiento de proceso especial:</label>
		<input type="checkbox" name="cerrarDocumentacionSeguimientoProcesoEspecial" value="true"/>

		<s:url id="urlConfigEditor" value="/META-INF/lib/alex.jar"
			includeContext="true" />
		<sjr:ckeditor id="txaDocumentacionSeguimientoProcesoEspecial"
			name="model.txDocumentacion" customConfig="%{urlConfigEditor}"
			editorLocal="es" width="920"
			value="%{seguimientoProcesoEspecial.ultimaDocumentacionSeguimientoProcesoEspecial.txDocumentacion}"></sjr:ckeditor>

		<sj:a button="true" buttonIcon="ui-icon-star" key="aceptar"
			formIds="frmDocumentacionSeguimientoProcesoEspecial">Aceptar</sj:a>
		<sj:a button="true" buttonIcon="ui-icon-cancel"
			href="javascript:void(0);" onclick="location.href='%{urlCancelar}'"
			requestType="GET">Cancelar</sj:a>
	</s:form>
</body>
	</html>
</jsp:root>
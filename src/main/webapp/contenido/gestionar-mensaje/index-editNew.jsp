<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/gestionar-mensaje"
		includeContext="true" />
	Crear nuevo mensaje de
	<s:property value="tipoMensajeSel.nbTipoMsg" />
	<s:form id="frmMensaje"
		action="%{#request.contextPath}/gestionar-mensaje" method="post"
		theme="simple" acceptcharset="UTF-8">
		<s:url id="urlConfigEditor" value="/META-INF/lib/alex.jar"
			includeContext="true" />
		<label> <s:text name="txDocumentacionCampo" />: </label>
		<sjr:ckeditor id="txaDocumentacionMensaje" name="txMensaje"
			customConfig="%{urlConfigEditor}" editorLocal="es" width="920"
			height="100" deferredLoading="true"></sjr:ckeditor>

	</s:form>
	<sj:a id="lnkAgregarMensaje" formIds="frmMensaje" button="true"
		buttonIcon="ui-icon-gear" indicator="indicator" href="#"
		onCompleteTopics="accionTerminadaMensaje">Aceptar</sj:a>
	<sj:a id="lnkCancelar" href="javascript:void(0);"
		onclick="location.href='%{urlCancelar}'" button="true"
		buttonIcon="ui-icon-close">Cancelar</sj:a>
</body>
	</html>
</jsp:root>
<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Eliminar usuario ${txNombre} ${txAp} ${txAp}</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformdelete">
<span class="psdg-bold">ELIMINAR USUARIO</span>
<P />
<div id="psdg-light">Catalogo de Usuarios - Bitácora de Producción</div>
<BR />
<BR />
<BR />
<BR />
	<s:url id="urlCancelar" value="/catalogo-usuario" includeContext="true" />
	<s:form id="frmUsuario" action="%{#request.contextPath}/catalogo-usuario/%{id}"
		method="post" theme="simple" acceptcharset="UTF-8">
		<s:hidden name="_method" value="delete" />
		<div id="formulafont">
		<label><s:text name="cdLdapCampo" /> </label>
		<s:textfield name="cdLdap" disabled="true" cssClass="campo" />
		<label class="centralbl"><s:text name="txNombreCampo" /> </label>
		<s:textfield name="txNombre" disabled="true" cssClass="campo" />
		<label><s:text name="txApCampo" /> </label>
		<s:textfield name="txAp" disabled="true" cssClass="campo" />
		<label class="centralbl1" ><s:text name="txAmCampo" /> </label>
		<s:textfield name="txAm" label="Apellido materno" disabled="true"
			cssClass="campo" />
<BR />
<BR />
<BR />
<BR />
<BR />
<BR />
<BR />
		<sj:a id="button1" button="true" key="aceptar"
			formIds="frmUsuario">Aceptar</sj:a>
		<sj:a id="button2" button="true" href="#"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
			</div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
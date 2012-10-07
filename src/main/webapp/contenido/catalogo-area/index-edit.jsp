<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Editar area</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformproblem">
<span class="psdg-bold">EDITAR ÁREA</span>
<P />
<div id="psdg-light">Catalogo de Áreas - Bitácora de Producción</div>
<BR />
<BR />
<BR />
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/catalogo-area" includeContext="true" />
	<s:form id="frmArea"
		action="%{#request.contextPath}/catalogo-area/%{id}" method="post"
		theme="simple" acceptcharset="UTF-8">
		<s:hidden name="_method" value="put" />
		<div id="formulafont">
		<div class="spacer0">
		<label><s:text name="nombreCampo"/> </label>
		<s:textfield name="nbArea" cssClass="campo" /></div>

		<sj:a id="button1" button="true"  key="aceptar"
			formIds="frmArea">Aceptar</sj:a>
		<sj:a id="button2" button="true"  href="javascript:void(0);"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
			</div>
	</s:form>
	</div>
	</div>
	</body>
	</html>
</jsp:root>
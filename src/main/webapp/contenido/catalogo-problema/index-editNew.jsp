<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Nuevo problema</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformproblem">
<span class="psdg-bold">NUEVO PROBLEMA</span>
<P />
<div id="psdg-light">Catalogo de problemas - Bitácora de Producción</div>
<BR />
<BR />
<BR />
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:form action="%{#request.contextPath}/catalogo-problema"
		method="post" theme="simple" acceptcharset="UTF-8">
		<div id="formulafont">
		<div class="spacer">
		<label>Nombre</label>
		<s:textfield name="nbProblema" cssClass="campo" /></div>
		<div class="spacer2">
		<label>Responsabilidad</label>
		<s:select id="rdoAreas" name="txResponsabilidad" list="areasDisponibles"
			listKey="nbArea" listValue="nbArea" buttonset="true" cssClass="campo" /></div>
		<div class="spacer2">
		<label>Corrige</label>
		<s:select id="rdoAreas" name="txCorrige" list="areasDisponibles"
			listKey="nbArea" listValue="nbArea" buttonset="true" cssClass="campo" /></div>
		<div class="spacer2">
		<label>Área</label>
		<s:select id="rdoAreas" name="cdArea" list="areasDisponibles"
			listKey="id" listValue="nbArea" buttonset="true" cssClass="campo" /></div>
		<sj:submit id="button1" value="Aceptar"  button="true"  />
		<sj:submit id="button2" value="Cancelar"
			action="%{#request.contextPath}/catalogo-problema}" button="true"
			 /></div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
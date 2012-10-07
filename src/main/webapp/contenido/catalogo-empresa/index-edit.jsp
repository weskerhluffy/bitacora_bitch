<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Editar empresa</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformproblem">
<span class="psdg-bold">EDITAR EMPRESA</span>
<P />
<div id="psdg-light">Catalogo de Empresas - Bitácora de Producción</div>
<BR />
<BR />
<BR />
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/catalogo-empresa" includeContext="true" />
	<s:form id="frmEmpresa"
		action="%{#request.contextPath}/catalogo-empresa/%{id}" method="post"
		theme="simple" acceptcharset="UTF-8">
		<s:hidden name="_method" value="put" />
		<div id="formulafont">	
		<label><s:text name="cdEmpresaCampo" /> </label>
		<s:textfield name="cdEmpresaMutable" cssClass="campo" />
		<div class="spacer">
		<label><s:text name="nombreCampo" /> </label>
		<s:textfield name="nbEmpresa" cssClass="campo" /></div>
		<div class="spacer">
		<label><s:text name="cdGsiCampo" /> </label>
		<s:textfield name="cdGsi" cssClass="campo" /></div>
		<div class="spacer">
		<label><s:text name="nbGsiCampo" /> </label>
		<s:textfield name="nbGsi" cssClass="campo" /></div>
		<div class="spacer0">
		<label><s:text name="cdEvidenciaCampo" /> </label>
		<s:textfield name="cdEvidencia" cssClass="campo" /></div>
		<div class="spacer0">
		<label><s:text name="tmDifHorariaCampo" /> </label>
		<sj:datepicker timepicker="true" name="hmDifHoraria"
			timepickerOnly="true" timepickerFormat="hh:mm" cssClass="campo" /></div>
			<div class="spacer0">
		<label><s:text name="stDifHorariaPosCampo" /> </label>
		<sj:radio id="rdoStDifHorariaPos"
			list="#{true: 'Positiva',false:'Negativa'}" name="stDifHorariaPos" /></div>
		<sj:a id="button1" button="true"  key="aceptar"
			formIds="frmEmpresa">Aceptar</sj:a>
		<sj:a id="button2" button="true"  href="#"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
			</div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
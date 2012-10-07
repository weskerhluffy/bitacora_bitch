<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Nuevo Proceso Especial</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
<jsp:text>
	<![CDATA[ 
			<script src='${pageContext.request.contextPath}/dwr/interface/CatalogoProcesoEspecial.js' type='text/javascript'></script>
			<script src="${pageContext.request.contextPath}/scripts/catalogo-proceso-especial.js" type="text/javascript"></script>
		 ]]>
</jsp:text>
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformnewespeciales">
<span class="psdg-bold">AGREGAR NUEVO PROCESO CRÍTICO</span>
<P />
<div id="psdg-light">Catalogo de procesos Críticos -Bitacora Batch</div>
<br/>
<br/>
<br/>
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/catalogo-proceso-especial"
		includeContext="true">
		<s:param name="empSel">
			<s:property
				value="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@EMP_SEL].cdEmpresa" />
		</s:param>
	</s:url>
	<s:form id="frmProcesoEspecial"
		action="%{#request.contextPath}/catalogo-proceso-especial"
		method="post" theme="simple" acceptcharset="UTF-8">
		<div id="formulafont">
		<div class="spacer">
		<label><s:text name="nbProcesoCampo" /> </label>
		<s:textfield cssClass="campo" name="nbProceso" /></div>
		<div class="spacer">
		<label><s:text name="nbGrupoCampo" /> </label>
		<s:textfield cssClass="campo" name="nbGrupo" /></div>
		<div class="spacer0">
		<label><s:text name="nbProcEspCampo" /> </label>
		<s:textarea cssClass="campo" name="nbProcEsp" cols="30" rows="5" /></div>
		<div class="spacer2">
		<label><s:text name="cdTipProEspCampo" /> </label>
		<s:select name="cdTipProEsp" list="tiposProcesoEspecialDisponibles"
			listKey="id" listValue="nbTipProEsp" buttonset="true"
			cssClass="campo" /></div>
			<div class="spacer2">
		<label><s:text name="nbOrigenCampo" /> </label>
		<sj:radio name="cdOrigen" list="origenesDisponibles"
			listKey="cdOrigen" listValue="nbOrigen" buttonset="true"
			cssClass="campo" /></div>
			<div class="spacer">
		<label><s:text name="hmTecnicaCampo" /> </label>
		<sj:datepicker timepicker="true" name="hmTecnica"
			timepickerOnly="true" timepickerFormat="hh:mm" cssClass="campo" /></div>
			<div class="spacer">
		<label><s:text name="hmServicioCampo" /> </label>
		<sj:datepicker timepicker="true" name="hmServicio"
			timepickerOnly="true" timepickerFormat="hh:mm" cssClass="campo" /></div>
			<div class="spacer">
		<label><s:text name="hmTecnicaFmCampo" /> </label>
		<sj:datepicker timepicker="true" name="hmTecnicaFinDeMes"
			timepickerOnly="true" timepickerFormat="hh:mm" cssClass="campo" /></div>
			<div class="spacer1">
		<label><s:text name="hmServicioFmCampo" /> </label>
		<sj:datepicker timepicker="true" name="hmServicioFinDeMes"
			timepickerOnly="true" timepickerFormat="hh:mm" cssClass="campo" /></div>
			<div class="spacer0">
		<label><s:text name="stFhIniDiaSigCampo" /> </label>
		<sj:radio id="rdoStFhIniDiaSig" list="#{true: 'Si',false:'No'}"
			name="stFhIniDiaSig" /></div>
		<br />
		<sj:a id="button1" button="true"  key="aceptar"
			formIds="frmProcesoEspecial">Aceptar</sj:a>
		<sj:a id="button2" button="true"  href="#"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
			</div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
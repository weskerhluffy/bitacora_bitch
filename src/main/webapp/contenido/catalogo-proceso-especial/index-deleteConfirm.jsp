<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Eliminar proceso especial </title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformnewespeciales">
<span class="psdg-bold">ELIMINAR PROCESO CRÍTICO</span>
<P />
<div id="psdg-light">Catalogo de procesos Críticos -Bitacora Batch</div>
<br />
<br />
<br />
	<s:url id="urlCancelar" value="/catalogo-proceso-especial" includeContext="true">
		<s:param name="empSel">
			<s:property value="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@EMP_SEL].cdEmpresa" />
		</s:param>
	</s:url>
	<s:form id="frmProcesoEspecial" action="%{#request.contextPath}/catalogo-proceso-especial/%{id}"
		method="post" theme="simple" acceptcharset="UTF-8">
		<div id="formulafont">
		<s:hidden name="_method" value="delete" />
		<div class="spacer">
		<label><s:text name="nbProcesoCampo" /> </label>
		<s:textfield name="nbProceso" disabled="true" cssClass="campo" /></div>
		<div class="spacer">
		<label><s:text name="nbGrupoCampo" /> </label>
		<s:textfield name="nbGrupo" disabled="true" cssClass="campo" /></div>
		<div class="spacer">
		<label><s:text name="cdTipProEspCampo" /> </label>
		<s:textfield name="cdTipProEsp" value="%{tipoProcesoEspecial.nbTipProEsp}" disabled="true" cssClass="campo" /></div>
		<div class="spacer1">
				<label><s:text name="nbOrigenCampo" /> </label>
		<s:textfield name="cdOrigen" value="%{origen.nbOrigen}"  disabled="true" cssClass="campo" /></div>

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
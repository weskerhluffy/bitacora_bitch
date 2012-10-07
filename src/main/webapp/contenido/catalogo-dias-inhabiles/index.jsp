<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/catalogo-dias-inhabiles.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Administración de días inhabiles</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformhabiles">
<span class="psdg-bold">Catalogo de días Habiles</span><br />
			<p><div id="psdg-light">Bitácora de Producción</div></p>
	<div id="datepicker">
		<s:actionerror theme="jquery" />
		<s:fielderror theme="jquery" />
		<s:url id="urlCancelar" value="/catalogo-dias-inhabiles"
			includeContext="true" />
		<s:form id="frmDiaInhabil"
			action="%{#request.contextPath}/catalogo-dias-inhabiles"
			method="post" theme="simple" acceptcharset="UTF-8">


			<s:iterator value="list">
				<s:hidden cssClass="fechasOriginales" name="fechasOrg"
					value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil@getFechaFormateadaCorta(id.fhInhabil)}"></s:hidden>
			</s:iterator>

			<label><s:text name="labelUsu"></s:text> </label>
			<s:property
				value="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].cdLdap" />
			<br />

			<br />
			<br />
			<s:select
				list="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].empresas"
				id="rdoEmpresas" name="empSel" listKey="id" listValue="nbEmpresa"
				buttonset="true" cssClass="campo" label="Seleccionar empresa" />

			<br />
			<br />
			<div class="spacer2">
			<label id="labelAnio">Seleccionar Año:</label>
			<select id="anio">
				<option value="2012">2012</option>
				<option value="2013">2013</option>
				<option value="2014">2014</option>
				<option value="2015">2015</option>
				<option value="2016">2016</option>
			</select>
			<br /></div>
<div class="spacer2">
			<sj:a id="button1" button="true" 
				key="aceptar" formIds="frmDiaInhabil">Aceptar</sj:a>
			<sj:a id="button2" button="true"  href="#"
				onclick="location.href='%{urlCancelar}'">Cancelar</sj:a></div>
				
			<s:text name="tituloDiasIn">Seleccionar dias inhabiles de cada empresa </s:text>

		</s:form>

	</div>
</div>
</div>
</body>
	</html>
</jsp:root>
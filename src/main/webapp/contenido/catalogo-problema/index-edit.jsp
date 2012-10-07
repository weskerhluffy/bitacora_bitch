<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
	</jsp:text>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Editar problema</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformproblem">
<span class="psdg-bold">EDITAR PROBLEMA</span>
<P />
<div id="psdg-light">Catalogo de problemas - Bitácora de Producción</div>
<BR />
<BR />
<BR />
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/catalogo-problema"
		includeContext="true" />
	<s:form id="frmProblema"
			action="%{#request.contextPath}/catalogo-problema/%{id}" method="post"
		theme="simple" acceptcharset="UTF-8">
		<s:hidden name="_method" value="put" />
		<div id="formulafont">
		<label><s:text name="nombreCampo" /> </label>		
		<s:textfield name="nbProblema" cssClass="campo" />
			<div class="spacer2">
		<label> <s:text name="txResponsabilidadCampo" /> </label>
		<s:select id="rdoAreas" name="txResponsabilidad" list="areasDisponibles"
			listKey="nbArea" listValue="nbArea" buttonset="true" cssClass="campo" /></div>
			<div class="spacer2">
		<label> <s:text name="txCorrigeCampo" /> </label>
		<s:select id="rdoAreas" name="txCorrige" list="areasDisponibles"
			listKey="nbArea" listValue="nbArea" buttonset="true" cssClass="campo" /></div>
		<div class="spacer2">
		<label><s:text name="cdAreaCampo" /> </label>
		<s:select id="rdoAreas" name="cdArea" list="areasDisponibles"
			listKey="id" listValue="nbArea" buttonset="true" cssClass="campo"
			disabled="%{!areaEditable}" /></div>
		<sj:a id="button1" button="true"  key="aceptar"
			formIds="frmProblema">Aceptar     </sj:a>
		<sj:a id="button2" button="true"  href="#"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
			</div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
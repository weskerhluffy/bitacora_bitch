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
<title>Eliminar problema</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformproblem">
<span class="psdg-bold">ELIMINAR PROBLEMA</span>
<P />
<div id="psdg-light">Catalogo de problemas - Bitácora de Producción</div>
<BR />
<BR />
<BR />
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:form action="%{#request.contextPath}/catalogo-problema/%{id}"
		method="post" theme="simple" acceptcharset="UTF-8">
		<s:hidden name="_method" value="delete" />
		<div id="formulafont">
		<label>Nombre</label>
		<s:textfield name="nbProblema" cssClass="campo" disabled="true" />
		<div class="spacer">
				<label>Responsabilidad</label>
		<s:textfield name="txResponsabilidad" cssClass="campo" disabled="true" /></div>
		<div class="spacer">
		<label>Corrige</label>
		<s:textfield name="txCorrige" cssClass="campo" disabled="true" /></div>
		<div class="spacer0">
		<label>Área</label>
		<s:select id="rdoAreas" name="cdArea" list="areasDisponibles"
			listKey="id" listValue="nbArea" buttonset="true" cssClass="campo"
			disabled="true" /></div>
		<sj:submit id="button1" value="Aceptar" button="true"  />
		<sj:submit id="button2" value="Cancelar"
			action="%{#request.contextPath}/catalogo-problema}" button="true" />
			</div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
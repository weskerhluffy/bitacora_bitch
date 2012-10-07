<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>



<title>Documentar holdeado</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />
	
</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="editorholdeados2">
<span class="psdg-bold">Documentar holdeado</span>
<P />
<div id="psdg-light">Documentación de Proceso Holdeado - Bitácora de Producción</div>
<BR />
<BR />
<BR />
<div id="formulafont">
		<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" action="bitacora" includeContext="true" />
	<s:form id="frmDocumentacionHoldeado"
		action="%{#request.contextPath}/documentacion-holdeado" method="post"
		theme="simple" acceptcharset="UTF-8">
		<h4>Datos del proceso holdeado:</h4>
		
		<table style="text-align: left;">
			
			<tr>
			
				<td><label><s:text name="nbProcesoCampo" /> </label>:</td>
				<td><s:property value="holdeado.nbProceso" /></td>
			
			</tr>
			
			
			<tr>
			
				<td><label><s:text name="Usuario que detiene" /> </label>:</td>
				<td><s:if test="holdeado.usuarioDetiene">
							
						<s:property value="holdeado.usuarioDetiene.txNombre"/><p></p>
						 <s:property value="holdeado.usuarioDetiene.txAp" /><p></p>
						  <s:property value="holdeado.usuarioDetiene.txAm" />  
						</s:if> <s:else> 
							<s:property value="holdeado.cdUsuarioDetiene" />
						
						</s:else>
						
						</td>
				
			</tr>
			
			<tr>
				<td><label><s:text name="nbOrigenCampo" /> </label>:</td>
				<td><s:property value="holdeado.origen.nbOrigen" /></td>
			</tr>
			<tr>
				<td><label><s:text name="cdCicloCampo" /> </label>:</td>
				<td><s:property value="holdeado.id.cdCiclo" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="tmHoldCampo" /> </label>:</td>
				<td><s:date name="holdeado.id.tmEvento"
						format="yyyy-MM-dd hh:mm:ss" />
				</td>
			</tr>
		</table>
		<br />

		<s:url id="urlConfigEditor" value="/META-INF/lib/alex.jar"
			includeContext="true" />
		<sjr:ckeditor id="txaDocumentacionHoldeado"
			name="model.txDocumentacion" customConfig="%{urlConfigEditor}"
			editorLocal="es" width="920"
			value="%{holdeado.ultimaDocumentacionHoldeado.txDocumentacion}"></sjr:ckeditor>
<BR />
<BR />
		<sj:a id="button1" button="true"  key="aceptar"
			formIds="frmDocumentacionHoldeado">Aceptar</sj:a>
		<sj:a id="button2" button="true" 
			href="javascript:void(0);" onclick="location.href='%{urlCancelar}'"
			requestType="GET">Cancelar</sj:a>
	</s:form>
	</div>	
	</div>
	</div>
</body>
	</html>
</jsp:root>
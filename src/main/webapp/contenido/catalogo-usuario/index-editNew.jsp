<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Nuevo usuario</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
<jsp:text>
	<![CDATA[ 
			<script src='${pageContext.request.contextPath}/dwr/interface/CatalogoUsuarioController.js' type='text/javascript'></script>
			<script src="${pageContext.request.contextPath}/scripts/catalogo-usuario.js" type="text/javascript"></script>
		 ]]>
</jsp:text>
</head>
<body>

<div id="bodyedituser" >
<div id="stylized" class="myform">
<span class="psdg-bold">NUEVO USUARIO</span>
<P />
<div id="psdg-light">Catalogo de problemas - Bitácora de Producción</div>
<BR />
<BR />
<BR />
<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/catalogo-usuario" includeContext="true" />
	<s:form id="frmUsuario"  class="signin"
		action="%{#request.contextPath}/catalogo-usuario" method="post"
		theme="simple" acceptcharset="UTF-8">
		<div id="formulafont">
		<div class="spacer">
		<label> <s:text name="cdLdapCampo" /> </label>
		<s:textfield name="cdLdap" cssClass="campo"/></div>
		<div class="spacer">
		<label> <s:text name="txNombreCampo" /> </label>
		<s:textfield name="txNombre" cssClass="campo" /></div>
		<div class="spacer">
		<label><s:text name="txApCampo" /> </label>
		<s:textfield name="txAp" cssClass="campo" /></div>
		<div class="spacer">
		<label><s:text name="txAmCampo" /> </label>
		<s:textfield name="txAm" cssClass="campo" /></div>
		<div class="spacer2">
		<label><s:text name="cdAreaCampo" /> </label>
		<s:select id="rdoAreas" name="cdArea" list="areasDisponibles"
			listKey="id" listValue="nbArea" buttonset="true" cssClass="campo" /></div>
			<br />
			<br />
			<br />
			<br />
<table id="tablausuario">
	<TR> 
   <TH >PERFILES</TH> 
   <TH>PERMISOS</TH> 
   <TH>EMPRESA</TH> 
</TR> 
	<TR>
		<TD >
			
			<sj:radio id="rdoPerfiles" name="cdPerfil" list="perfilesDisponibles"
				listKey="id" listValue="nbPerfil" buttonset="true"
				onChangeTopics="perfilCambiado" cssClass="campo"/>
				
				</TD>
		<TD>	
		
		<sj:checkboxlist id="rdoPermisos" name="permisosSel"
			list="permisosDisponibles" listKey="id" listValue="nbPermiso"
			buttonset="true" cssClass="campo" />
		</TD>
		<TD>
		
		<sj:checkboxlist id="chkbxEmpresas" name="empresasSel"
			list="empresasDisponibles" listKey="cdEmpresa" listValue="nbEmpresa"
			buttonset="true" cssClass="campo" />
		</TD>
	</TR>
</table>
	<br />
			<br />
			<br />
			<br />
	<sj:a   id="button1" button="true" key="aceptar"
			formIds="frmUsuario" >Aceptar</sj:a>
			
		<sj:a  id="button2"  button="true"  href="#"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
			</div>
	</s:form>
	</div>
	</div>
</body>
	</html>
</jsp:root>
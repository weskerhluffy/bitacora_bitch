<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Gestionar delegación</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />

</head>
<body>
<div id="bodyedituser" >
<div id="stylized" class="myformproblem">
<span class="psdg-bold">Delegar Bitácora</span>
<P />
<div id="psdg-light">Documentación de Proceso Holdeado - Bitácora de Producción</div>
<BR />
<BR />
<BR />
<div id="formulafontdelega">
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<s:url id="urlCancelar" value="/bitacora" includeContext="true" />
	<s:form id="frmBitacora"
		action="%{#request.contextPath}/bitacora/%{id}" method="post"
		theme="simple" acceptcharset="UTF-8">
		<s:hidden name="_method" value="put" />
		<table class="tablaNormal">
			<tr>
				<td><s:text name="fhCicloCampo" />:</td>
				<td><s:date name="fhCicloBitacora" format="yyyy-MM-dd" /></td>
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="nbTurnoCampo" />:</td>
				<td><s:property value="turno.nbTurno" />
				</td>
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="nbEmpresaCampo" />:</td>
				<td><s:property value="empresa.nbEmpresa" />
				</td>
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="nbUsuarioDelegaCampo" />:</td>
				<td><s:property value="usuarioDelega.txNombre" /></td>
			</tr>
			<tr>
				<td><br/></td>	
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="cdLdapUsuarioRecibeCampo" />:</td>
				<td><div class="formimput"><s:textfield name="ldapRecibe" /></div></td>
				
				
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="passwordRecibeCampo" />:</td>
				<td><div class="formimput"><s:textfield name="passRecibe" /></div>
				</td>
			</tr>
			<tr>
				<td><s:text name="cdLdapUsuarioAutorizaCampo" />:</td>
				<td><div class="formimput"><s:textfield name="ldapAutoriza" /></div>
				</td>
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="passwordAutorizaCampo" />:</td>
				<td><div class="formimput"><s:textfield name="passAutoriza" /></div>
				</td>
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
			<tr>
				<td><s:text name="retornarCicloProductivoCampo" />:</td>
				<td><div class="formimput1"><s:checkbox name="retornaCicloProductivo" /></div>
				</td>
			</tr>
			<tr>
				<td><br/></td>
				<td><br/></td>
			</tr>
		</table>


		<sj:a id="button1" button="true"  key="aceptar"
			formIds="frmBitacora">Aceptar</sj:a>
		<sj:a id="button2" button="true"  href="#"
			onclick="location.href='%{urlCancelar}'">Cancelar</sj:a>
	</s:form>
	</div>
	</div></div>
</body>
	</html>
</jsp:root>
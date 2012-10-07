<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />
	
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	
		<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/superfish.css" />

<jsp:text>
	<![CDATA[ 
		
		<script	src="${pageContext.request.contextPath}/scripts/catalogo-usuario.js" type="text/javascript"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>  
		
		
		]]>
</jsp:text>
<title>Administración de usuarios</title>

  </head>
<body>

<div id="imagenewuser">
</div>
<a id="newuser" href="${pageContext.request.contextPath}/catalogo-usuario/new">
 <input   type="submit" id="newuser"  class="button3" value="Agregar Usuario" />
	  	  </a>
	  	  <BR />
	  	   <BR />
	  	   <BR />
	  	   <BR />
<div id="psdgraphics-com-table">
		 <div id="psdg-header">
		 	<span class="psdg-bold">Catalogo de Usuarios</span><br />
		<div id="psdg-light">Bitácora de Producción</div>
		</div>
 <div id="ps-top">
	<table id="tblUsuarios">
		<thead>
			<tr> 
				<th><div >Usuario LDAP</div></th>	 
				<th><div >Nombre</div></th>			 
				<th><div >Apellido paterno</div></th>	 
				<th><div >Apellido materno</div></th>	 
				<th><div  >Acciones</div></th>	
		
			</tr>
		</thead>
	
		<tbody>
		<div id="psdg-middle">
			<s:iterator value="list" >
				<tr>
					<td >${cdLdap}</td>
					<td >${txNombre}</td>
				<td >${txAp}</td>
					<td >${txAm}</td>
					<td>
					 <a href="${pageContext.request.contextPath}/catalogo-usuario/${id}/edit">
					 	<image src="${pageContext.request.contextPath}/images/edit.png" width="70" height="25"></image> 
					</a>
						<a href="${pageContext.request.contextPath}/catalogo-usuario/${id}/deleteConfirm">
						<image src="${pageContext.request.contextPath}/images/delete.png" width="70" height="25"></image>
						  </a>
					</td>
				</tr>
			</s:iterator>
		</div>
		</tbody>
		
	  </table>
	</div>	
</div>
</body>
	</html>
</jsp:root>
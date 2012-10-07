<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/catalogo-area.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Administración de áreas</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
</head>
<body>

<a  id="newuser"  href="${pageContext.request.contextPath}/catalogo-area/new">
<input   type="submit" id="newuser"  class="button3" value="Agregar Área" />
		</a>
 <BR />
	  	   <BR />
	  	   <BR />
	  	   <BR />
<div id="psdgraphics-com-table">
		
		 <div id="psdg-header">
		 	<span class="psdg-bold">Catalogo de Áreas</span><br />
			<div id="psdg-light">Bitácora de Producción</div>
		</div>
 <div id="ps-top">
	<table id="tblArea">
		<thead>
			<tr>
				<th>Area</th>
				<th>Acciones</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="list">
				<tr>
					<td>${nbArea}</td>
					<td><a	href="${pageContext.request.contextPath}/catalogo-area/${id}/edit">
						<image src="${pageContext.request.contextPath}/images/edit.png" width="70" height="25"></image></a>
						<a 	href="${pageContext.request.contextPath}/catalogo-area/${id}/deleteConfirm">
						<image src="${pageContext.request.contextPath}/images/delete.png" width="70" height="25"></image>
						</a>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	</div>
	</div>
</body>
	</html>
</jsp:root>
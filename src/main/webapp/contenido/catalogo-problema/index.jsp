<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags">
	<jsp:directive.page contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8" session="false" />
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="true" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/catalogo-problema.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Catálogo de problemas</title>
</head>
<body>
<a  id="newuser" href="${pageContext.request.contextPath}/catalogo-problema/new">
 <input   type="submit" id="newuser"  class="button3" value="Agregar Problema" />
</a>
 <BR />
	  	   <BR />
	  	   <BR />
	  	   <BR />
<div id="psdgraphics-com-table">
 <div id="psdg-header">
		 	<span class="psdg-bold">Catalogo de Problemas</span><br />
			<div id="psdg-light">Bitácora de Producción</div>
		</div>
<div id="ps-top">
	<table id="tblProblema">
		<THEAD>
			<TR>
				<TH>Área</TH>
				<TH >Problema</TH>
				<TH>Responsabilidad</TH>
				<TH>Corrige</TH>
				<TH>Acciones</TH>
				
			</TR>
		</THEAD>
		<TBODY>
			<s:iterator value="list">
				<tr>
					<td>${area.nbArea}</td>
					<td id="columproblem">${nbProblema}</td>
					<td>${txResponsabilidad}</td>
					<td>${txCorrige}</td>
					<td><a	href="${pageContext.request.contextPath}/catalogo-problema/${id}/edit">
						<image src="${pageContext.request.contextPath}/images/edit.png" width="70" height="25"></image> </a>
						<a	href="${pageContext.request.contextPath}/catalogo-problema/${id}/deleteConfirm">
						<image src="${pageContext.request.contextPath}/images/delete.png" width="70" height="25"></image></a>
					</td>
				</tr>
			</s:iterator>
		</TBODY>
	</table>
	</div>
	</div>
</body>
	</html>
</jsp:root>
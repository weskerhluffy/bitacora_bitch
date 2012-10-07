<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/mostrar-cargas.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Historial de cargas</title>
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>

<div id="psdgraphics-com-table">
 <div id="psdg-header">
		 	<span class="psdg-bold">Historia de Cargas</span><br />
			<div id="psdg-light">Bitácora de Producción</div>
		</div>
<div id="ps-top" >

	<table id="tblCargas">
		<thead>
			<tr>
				<th>Tiempo</th>
				<th>Estado</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="list" var="carga">
				<tr>
					<td><s:date name="#carga.id.tmInsercion"
							format="yyyy-MM-dd hh:mm" />
					</td>
					<td><s:if test="#carga.stCarga">
							<s:text name="cargaExitosa" />
						</s:if> <s:else>
							<s:text name="cargaNoExitosa" />
						</s:else>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<s:url id="urlRegresar" action="bitacora" includeContext="true" />
	<sj:a button="true" id="button1"
		href="javascript:void(0);" onclick="location.href='%{urlRegresar}'">Regresar</sj:a>
</div>
</div>
</body>
	</html>
</jsp:root>
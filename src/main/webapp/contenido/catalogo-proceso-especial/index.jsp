<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:text>
	<![CDATA[
		<script src="${pageContext.request.contextPath}/scripts/catalogo-proceso-especial.js" type="text/javascript"></script>
	]]>
</jsp:text>
<title>Catalogo Procesos Críticos</title>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
</head>
<body>
<div id="bodyedituser" >
<div  class="myformespeciales">
	<s:select
				list="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].empresas"
				id="rdoEmpresas" name="empSel" listKey="cdEmpresa" listValue="nbEmpresa"
				buttonset="true" cssClass="campo" label="Empresa" />
				
				<div id="newproceso">
				<a  href="${pageContext.request.contextPath}/catalogo-proceso-especial/new">
				<input   type="submit"   class="button3" value="Nuevo Proceso" /></a>
		</div>
				
		<BR />
	  	   <BR />
	  	   <BR />
	  	   <BR />		
<div id="psdgraphics-com-table">
		 <div id="psdg-header">
		 	<span class="psdg-bold">Catalogo de Procesos Críticos</span><br />
			<div id="psdg-light">Bitácora de Producción</div>
		</div>
		 <div id="ps-top">
					
	<table id="tblProcesosEspeciales">
		<thead>
			<tr>
				<th>Proceso</th>
				<th>Grupo</th>
				<th>Tipo </th>
				<th>Origen</th>
				<th>Acciones</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="list">
				<tr>
				<td>${nbProceso}</td>
				<td>${nbGrupo}</td>
				<td><s:property value="tipoProcesoEspecial.nbTipProEsp" /></td>
				<td><s:property value="origen.nbOrigen" /></td>
				<td><a	href="${pageContext.request.contextPath}/catalogo-proceso-especial/${id}/edit">
					<image src="${pageContext.request.contextPath}/images/edit.png" width="70" height="25"></image> </a>
					<a	href="${pageContext.request.contextPath}/catalogo-proceso-especial/${id}/deleteConfirm">
					<image src="${pageContext.request.contextPath}/images/delete.png" width="70" height="25"></image> </a>
				</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	</div>
</div>
</div>
</div>
</body>
	</html>
</jsp:root>
<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/headers.css" />
<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/jquery.dataTables.rowGrouping.js" type="text/javascript"></script>
		<script	src="${pageContext.request.contextPath}/scripts/documentacion-proceso-retrasado.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Documentación de procesos retrasados</title>
</head>
<body>
<div class="spacer0">
 <div id="psdg-header">
		 	<span class="psdg-bold">Procesos retrasados pendientes de documentación</span><br />
		<div id="psdg-light">Bitácora de Producción</div>
		</div></div>
 <div id="ps-top">
	
	<s:form id="frmProcesoRetrasado"
		action="%{#request.contextPath}/documentacion-proceso-retrasado/new"
		method="get" theme="simple">


		<s:set var="puedeDocumentar" scope="action"
			value="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}" />
		<table id="tblProcesoRetrasado" class="indexholdeado">
			<thead>
				<tr>
					<s:if test="#puedeDocumentar">
						<th></th>
					</s:if>
					<th>Id</th>
					<th>Grupo</th>
					<th>Proceso</th>
					<th>ODate</th>
					<th>OId</th>
					<th>Origen</th>
					<th>Tiempo inicio</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="procesoRetrasadosVigentes" var="procesoRetrasado">
					<s:if test="documentacionProcesoRetrasados.size eq 0">
						<tr>
							<s:if test="#puedeDocumentar">
								<td><s:checkbox name="idsProcesosRetrasadosSel"
										fieldValue="%{cdProRet}" /></td>
							</s:if>
							<td>${id}</td>
							<td>${nbGrupo}</td>
							<td>${nbProceso}</td>
							<td><s:date name="#procesoRetrasado.fhCiclo"
									format="yyyy-MM-dd" />
							</td>

							<td>${cdCiclo}</td>
							<td><s:property value="origen.nbOrigen" />
							</td>
							<td><s:date name="tmInicio" format="yyyy-MM-dd hh:mm:ss" />
							</td>
						</tr>
					</s:if>
				</s:iterator>
			</tbody>
		</table>

		<s:if test="#puedeDocumentar">
			<sj:a button="true" id="button1" key="aceptar"
				formIds="frmProcesoRetrasado">Documentar</sj:a>
		</s:if>

	</s:form>
	<div class="spacer0">
 <div id="psdg-header">
		 	<span class="psdg-bold">Procesos retrasados documentados</span><br />
		<div id="psdg-light">Bitácora de Producción</div>
		</div></div>
 <div id="ps-top">

		<table id="tblProcesoRetrasadoDocumentado"
		class="tablaNormal tablaHoldeado table.dataTable1  ">
		<thead>
			<tr>
				<th>Id</th>
				<th>Grupo</th>
				<th>Proceso</th>
				<th>ODate</th>
				<th>OId</th>
				<th>Origen</th>
				<th>Tiempo inicio</th>
				<th>Tiempo cierre</th>
				<th>Asociado a</th>
				<th>Documentación</th>

				<s:if test="#puedeDocumentar">
					<th>Editar documentación</th>
				</s:if>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="procesoRetrasadosVigentes" var="procesoRetrasado">
				<s:if test="documentacionProcesoRetrasados.size gt 0">
					<tr>
						<td>${id}</td>
						<td>${nbGrupo}</td>
						<td>${nbProceso}</td>
						<td><s:date name="#procesoRetrasado.fhCiclo"
								format="yyyy-MM-dd" /></td>

						<td>${cdCiclo}</td>
						<td><s:property value="origen.nbOrigen" /></td>
						<td><s:date name="tmInicio" format="yyyy-MM-dd hh:mm:ss" />
						</td>

						<td><s:if test="tmCerrado">
								<s:date name="tmCerrado" format="yyyy-MM-dd hh:mm:ss" />
							</s:if> <s:else>
										Aún no se cierra la documentación
								</s:else>
						</td>

						<td>${ultimaDocumentacionProcesoRetrasado.primerProcesoRetrasado.cdProRet}</td>
						<td>${ultimaDocumentacionProcesoRetrasado.txDocumentacion}</td>

						<s:if test="#puedeDocumentar">
							<td><s:if test="!tmCerrado">
									<!-- Se forma la lista de parametros -->
									<s:set var="idsRetrasadosDocumentados" scope="action"
										value="'idsProcesosRetrasadosSel='+ultimaDocumentacionProcesoRetrasado.procesoRetrasados[0].id" />
									<s:iterator
										value="ultimaDocumentacionProcesoRetrasado.procesoRetrasados"
										var="procesoRetrasadoRelacionado" begin="1">
										<s:set var="idsRetrasadosDocumentados" scope="action"
											value="#idsRetrasadosDocumentados+'&amp;idsProcesosRetrasadosSel='+#procesoRetrasadoRelacionado.id" />
									</s:iterator>
									<!-- Se asigna la lista de parametros a la url de la petición -->
									<s:url
										value="documentacion-proceso-retrasado/new?%{#idsRetrasadosDocumentados}"
										id="urlDocumentarProcesoRetrasado" includeParams="all"
										includeContext="true">
									</s:url>
									<s:a href="%{urlDocumentarProcesoRetrasado}">Documentar</s:a>
								</s:if> <s:else>
									<s:text name="procesoRetrasadoYaCerrado" />
								</s:else></td>
						</s:if>

					</tr>
				</s:if>
			</s:iterator>
		</tbody>
	</table>
	</div>
	</div>
	
</body>
	</html>
</jsp:root>
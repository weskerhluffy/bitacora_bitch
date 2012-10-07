<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/documentacion-seguimiento-proceso-especial.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Documentación de procesos especiales</title>
</head>
<body>
<div class="spacer0">
 <div id="psdg-header">
		 	<span class="psdg-bold">Procesos Especiales</span><br />
		<div id="psdg-light">Bitácora de Producción</div>
		</div></div>

	<s:set var="puedeDocumentar" scope="action"
		value="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@PANEL_DE_CONTROL in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}" />
	<table id="tblSeguimientoProcesoEspecial"
		class="indexholdeado" >
		<thead>
			<tr>
				<th>Proceso</th>
				<th>Grupo</th>
				<th>ODate</th>
				<th>Origen</th>
				<th>Tipo</th>
				<th>Hora Tecnica</th>
				<th>Hora Servicio</th>
				<th>Tiempo inicio</th>
				<th>Tiempo fin</th>
				<th>Tipo Retraso</th>
				<th>Documentación</th>

				<s:if test="#puedeDocumentar">
					<th>Documentar</th>
				</s:if>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="seguimientoProcesoEspecialsVigentes"
				var="seguimientoProcesoEspecial">
				<tr>
					<td>${id.nbProceso}</td>
					<td>${id.nbGrupo}</td>
					<td><s:date name="#seguimientoProcesoEspecial.id.fhCiclo"
							format="yyyy-MM-dd" /></td>
					<td><s:property value="procesoEspecial.origen.nbOrigen" /></td>
					<td><s:property
							value="procesoEspecial.tipoProcesoEspecial.nbTipProEsp" /></td>
					<td><s:date
							name="#seguimientoProcesoEspecial.procesoEspecial.hmTecnica"
							format="HH:mm" /></td>
					<td><s:date
							name="#seguimientoProcesoEspecial.procesoEspecial.hmServicio"
							format="HH:mm" /></td>

					<td><s:date name="id.tmInicio" format="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td><s:if test="tmFin">
							<s:date name="tmFin" format="yyyy-MM-dd HH:mm:ss" />
						</s:if> <s:else>
						Aún no se ha finalizado el proceso
						</s:else></td>
					<td><s:if
							test="estado==@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial@ROJO">
							<p style="color: red">Muy atrasado</p>
						</s:if> <s:if
							test="estado==@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial@AMARILLO">
							<p style="color: yellow">Atrasado</p>
						</s:if> <s:if
							test="estado==@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial@VERDE">
							<p style="color: green">A Tiempo</p>
						</s:if>
					</td>
					<td><s:if test="ultimaDocumentacionSeguimientoProcesoEspecial">
					${ultimaDocumentacionSeguimientoProcesoEspecial.txDocumentacion}
					</s:if> <s:else>
					Aún no ha sido documentado
					</s:else></td>

					<s:if test="#puedeDocumentar">
						<td><s:url
								action="documentacion-seguimiento-proceso-especial/new"
								id="urlDocumentarSeguimientoProcesoEspecial">
								<s:param name="seguimientoProcesoEspecialIdSel">
									<s:property value="id" />
								</s:param>
							</s:url> <s:a href="%{urlDocumentarSeguimientoProcesoEspecial}">Documentar</s:a>
						</td>
					</s:if>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
	</html>
</jsp:root>
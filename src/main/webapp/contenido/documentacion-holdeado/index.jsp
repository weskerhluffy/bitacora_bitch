<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/headers.css" />
<!-- NO UTILIZAR CSS HOLDER YA QUE APLICA OTRO ESTILO AL MENU -->
<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/documentacion-holdeado.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Documentación de holdeados</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/headers.css" />

</head>
<body>
<div class="spacer0">
 <div id="psdg-header">
		 	<span class="psdg-bold">Procesos Holdeados</span><br />
			<div id="psdg-light">Bitácora de Producción</div>
		</div>
		</div>
		<P />
	<s:set var="puedeDocumentar" scope="action"
		value="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}" />
	<table id="tblHoldeado" class="tablaNormal tablaHoldeado">
		<thead>
			<tr class="indexholdeado">
				<th><div class="indexholdeado">JOB</div></th>
				<th>Grupo</th>
				<th>ODate</th>
				<th>Tiempo-holdeo</th>
				<th>Usuario holdeo</th>
				<th>Tiempo de liberación</th>
				<th>Usuario de liberación</th>
				<th>Documentación</th>
				<th>Usuario de ultima modificación</th>
				<th>Tiempo de Ultima modificación</th>
				<s:if test="#puedeDocumentar">
					<th>Documentar</th>
				</s:if>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="holdeadosVigentes" var="holdeado">
				<tr>
					<td>${nbProceso}</td>
					<td>${nbGrupo}</td>
					<td>${fhCiclo}</td>

					<td><s:date name="id.tmEvento" format="yyyy-MM-dd hh:mm:ss" />
					</td>

					<td><s:if test="usuarioDetiene">
							<s:property value="usuarioDetiene.txNombre" />
							<p></p>
							<s:property value="usuarioDetiene.txAp" />
							<p></p>
							<s:property value="usuarioDetiene.txAm" />
						</s:if> <s:else>
							<s:property value="cdUsuarioDetiene" />
						</s:else></td>
					<td><s:if test="tmFinalizacion">
							<s:date name="tmFinalizacion" format="yyyy-MM-dd hh:mm:ss" />
						</s:if> <s:else>
						Aún sigue holdeado
						</s:else>
					</td>

					<td><s:if test="usuarioFinaliza">
							<s:property value="usuarioFinaliza.txNombre" />
							<p></p>
							<s:property value="usuarioFinaliza.txAp" />
							<p></p>
							<s:property value="usuarioFinaliza.txAm" />
						</s:if> <s:else>
							<s:property value="cdUsuarioFinaliza" />
						</s:else>
					</td>




					<td><s:if test="ultimaDocumentacionHoldeado">
					${ultimaDocumentacionHoldeado.txDocumentacion}
					</s:if> <s:else>
					Aún no ha sido documentado
					</s:else></td>
					<td><s:if test="ultimaDocumentacionHoldeado">	
					${ultimaDocumentacionHoldeado.usuario.txNombre} ${ultimaDocumentacionHoldeado.usuario.txAp} ${ultimaDocumentacionHoldeado.usuario.txAm} 
					</s:if> <s:else>
					No hay Usuario
					</s:else></td>
					<td><s:if test="ultimaDocumentacionHoldeado">
							<s:date name="ultimaDocumentacionHoldeado.tmDocumentacion"
								format="yyyy-MM-dd hh:mm:ss" />
						</s:if> <s:else>
					Aún no ha sido documentado
					</s:else></td>

					<s:if test="#puedeDocumentar">
						<td><s:url action="documentacion-holdeado/new"
								id="urlDocumentarHoldeado">
								<s:param name="holdeadoIdSel">
									<s:property value="id" />
								</s:param>
							</s:url> <s:a href="%{urlDocumentarHoldeado}">Documentar</s:a>
						</td>
					</s:if>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
	</html>
</jsp:root>
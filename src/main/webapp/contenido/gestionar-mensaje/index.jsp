<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:text>
	<![CDATA[ 
		<script	src="${pageContext.request.contextPath}/scripts/gestionar-mensaje.js" type="text/javascript"></script>
		]]>
</jsp:text>
<title>Administración Mensajes</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/headers.css" />

</head>
<body class="indexholdeado">
<div class="spacer0">
 <div id="psdg-header">
		 	<span class="psdg-bold">Mensajes</span><br />
		<div id="psdg-light">Bitácora de Producción</div>
		</div></div>
	<s:set var="puedeDocumentar" scope="action"
		value="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@MENSAJES in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}" />
	<s:actionmessage theme="jquery" id="msgMensaje" />
	<s:div id="divIndiceMensajes">
		<s:iterator value="tipoMensajes" var="tipoMensaje">
			<table class="tablaMensajes" style="display: inline;">
				<thead>
					<tr>
						<th colspan="2"><s:property value="#tipoMensaje.nbTipoMsg" />
						</th>
					</tr>
				</thead>
				<tbody>
				
							<tr><TD class="idmensajes">ID</TD>
						
							<td class="white">-------------</td>
						
							<td class="idmensajes"> Mensaje </td>
						</tr>
					<s:iterator value="mensajesVigentesClasificados[#tipoMensaje.id]"
						var="mensajeVigente" status="estado">


						<tr>
							<td class="idmensajes"><s:property value="%{#mensajeVigente.cdMensaje}" />
							</td>
							<td class="white">-------------</td>
							<td><s:if test="%{#mensajeVigente.ultimoMensajeSecundario}">
									<s:property
										value="%{#mensajeVigente.ultimoMensajeSecundario.txMensaje}"
										escapeHtml="false" />
								</s:if> <s:else>
									<s:property value="#mensajeVigente.txMensaje"
										escapeHtml="false" />
								</s:else>
							</td>
							<td class="white">-------------</td>
							<td><s:if test="#mensajeVigente.tmMensajeCerrado"> Mensaje cerrado el día <s:date name="#mensajeVigente.tmMensajeCerrado" format="dd " />  de <s:date name="#mensajeVigente.tmMensajeCerrado" format=" MMMM " />  de <s:date name="#mensajeVigente.tmMensajeCerrado" format="yyyy" /> a las <s:date name="#mensajeVigente.tmMensajeCerrado" format="HH" /> hrs <s:date name="#mensajeVigente.tmMensajeCerrado" format="mm" />  minutos  <s:date name="#mensajeVigente.tmMensajeCerrado" format="ss" /> segundos <s:date name="#mensajeVigente.tmMensajeCerrado" format="SS" /> milisegundos  
								</s:if> <s:else>
									<s:if test="#puedeDocumentar">
										<s:url id="urlEditarMensaje"
											action="gestionar-mensaje/%{#mensajeVigente.cdMensaje}/edit" />
										<sj:a button="true" buttonIcon="ui-icon-pencil"
											href="javascript:void(0);" onClickTopics="accionMensaje"
											onclick="location.href='%{urlEditarMensaje}'"><div class="idmensajes">Editar</div></sj:a>
										<s:url id="urlBorrarMensaje"
											action="gestionar-mensaje/%{#mensajeVigente.cdMensaje}/deleteConfirm" />
										<sj:a button="true" buttonIcon="ui-icon-trash"
											href="javascript:void(0);" onClickTopics="accionMensaje"
											onclick="location.href='%{urlBorrarMensaje}'"><div class="idmensajes">Borrar</div></sj:a>
										<s:url id="urlCerrarMensaje"
											action="gestionar-mensaje/%{#mensajeVigente.cdMensaje}!confirmarCerrarMensaje">
											<s:param name="idSel">
												<s:property value="#mensajeVigente.cdMensaje" />
											</s:param>
										</s:url>
										<sj:a button="true" buttonIcon="ui-icon-locked"
											href="javascript:void(0);" onClickTopics="accionMensaje"
											onclick="location.href='%{urlCerrarMensaje}'"><div class="idmensajes">  Cerrar</div></sj:a>
									</s:if>
								</s:else>
							</td>
						</tr>
					</s:iterator>
				</tbody>

			</table>
			
			<p></p><p></p><p></p>

			<s:if test="#puedeDocumentar">
				<s:url id="urlAgregarMensaje" action="gestionar-mensaje/new">
					<s:param name="idTipoMensajeSel">
						<s:property value="%{#tipoMensaje.id}" />
					</s:param>
				</s:url>
				<sj:a button="true" onclick="location.href='%{urlAgregarMensaje}'"
					href="javascript:void(0);" buttonIcon="ui-icon-note"> <div class="idmensajes">Agregar <s:property
						value="#tipoMensaje.nbTipoMsg" /></div>
				</sj:a>
			</s:if>
		</s:iterator>

	</s:div>
</body>
	</html>
</jsp:root>
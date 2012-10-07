<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags"
	xmlns:log="http://jakarta.apache.org/taglibs/log-1.0"
	xmlns:sjg="/struts-jquery-grid-tags"
	xmlns:sjt="/struts-jquery-tree-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="true" />
	<html xmlns="http://www.w3.org/1999/xhtml">
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	
		<!-- NO UTILIZAR HOLDER.CSS PORQUE APLICARA THEME SUPERFISH -->
	<head>
<jsp:text>
	<![CDATA[ 
			<script src='${pageContext.request.contextPath}/dwr/interface/BitacoraController.js' type='text/javascript'></script>
			<script src="${pageContext.request.contextPath}/scripts/form-ajax-util.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/jquery.togglepanels.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/bitacora.js" type="text/javascript"></script>
			<script src='${pageContext.request.contextPath}/dwr/interface/MensajesDocumentacion.js' type='text/javascript'></script>
		]]>
</jsp:text>
</head>
<body>

<style type="text/css">
* { margin:0;
    padding:0;
}
body {background-color:#ffffff;
line-height: 24px;
font-family:BBVA Office Light, Arial;
font-size: 12px;
color: #000000; }


</style>
<div id="psdg-header">
		 	<span class="psdg-bold">Bitácora de Producción México</span>
		</div>
<DIV class='centra'>
<s:hidden name="nombreEmpresa" id="nomEmpresa" value="%{#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@EMPRESA].cdEmpresa}"/>
	<!-- Pestañas de las últimas 3 bitácoras -->
	<sj:tabbedpanel id="tbpBitacoras" useSelectedTabCookie="true">
		<s:iterator value="list" status="estatus">
			<sj:tab id="tab%{id}" target="tbpBitacora%{id}"
				label="%{'Turno '+turno.nbTurno+' del ciclo '+@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil@getFechaFormateadaBonita(fhCicloBitacora)}" />
			<sj:tabbedpanel id="tbpBitacora%{id}" useSelectedTabCookie="true">
				<!-- Pestaña de Recibe-->
				<sj:tab  id="tabRecibe%{id}" target="tbpRecibe%{id}" label="Recibe" />
				<sj:tabbedpanel id="tbpRecibe%{id}" useSelectedTabCookie="true">
					<!-- Pestaña de Equipo/Sistema-->
					<sj:tab id="tabRecibeES%{id}" target="divRecibeES%{id}"
						label="Equipo/Sistema" />
					<s:div id="divRecibeES%{id}">					Equipo Sistema					</s:div>
					<!-- Pestaña de Cambios-->
					<sj:tab id="tabRecibeCambios%{id}" target="divRecibeCambios%{id}"
						label="Cambios" />
					<s:div id="divRecibeCambios%{id}">					Cambios					</s:div>
					<!-- Pestaña de Incidentes-->
					<sj:tab id="tabRecibeIncidentes%{id}"
						target="divRecibeIncidentes%{id}" label="Incidentes" />
					<s:div id="divRecibeIncidentes%{id}">
					Incidentes
					</s:div>
					<!-- Pestaña de Pendientes-->
					<sj:tab id="tabRecibePendientes%{id}"
						target="divRecibePendientes%{id}" label="Pendientes" />
					<s:div id="divRecibePendientes%{id}">
					Pendientes
					</s:div>
					<!-- Pestaña de EnProceso-->
					<sj:tab id="tabRecibeEnProceso%{id}"
						target="divRecibeEnProceso%{id}" label="En proceso" />
					<s:div id="divRecibeEnProceso%{id}">
					EnProceso
					</s:div>
					<!-- Pestaña de Holdeados-->
					<sj:tab id="tabRecibeHoldeados%{id}"
						target="divRecibeHoldeados%{id}" label="Holdeados" />
					<s:div id="divRecibeHoldeados%{id}">
					Holdeados
					</s:div>
				</sj:tabbedpanel>
				
				<!-- Termina Recibe-->

				<!-- Pestaña de Durante-->
				<sj:tab id="tabDurante%{id}" target="tbpDurante%{id}"
					label="Durante" />
				<sj:tabbedpanel id="tbpDurante%{id}" useSelectedTabCookie="true">
					<!-- Si no se trata de la bitacora actual -->
					<s:if test="#estatus.index!=list.size-1">
						<!-- Pestaña de Equipo/Sistema-->
						<sj:tab id="tabDuranteES%{id}" target="divDuranteES%{id}"
							label="Equipo/Sistema" />
						<s:div id="divDuranteES%{id}">				Equipo Sistema					</s:div>
						<!-- Pestaña de Cambios-->
						<sj:tab id="tabDuranteCambios%{id}"
							target="divDuranteCambios%{id}" label="Cambios" />
						<s:div id="divDuranteCambios%{id}">					Cambios					</s:div>
						<!-- Pestaña de Incidentes-->
						<sj:tab id="tabDuranteIncidentes%{id}"
							target="divDuranteIncidentes%{id}" label="Incidentes" />
						<s:div id="divDuranteIncidentes%{id}">					Incidentes					</s:div>
						<!-- Pestaña de Pendientes-->
						<sj:tab id="tabDurantePendientes%{id}"
							target="divDurantePendientes%{id}" label="Pendientes" />
						<s:div id="divDurantePendientes%{id}">					Pendientes					</s:div>
						<!-- Pestaña de EnProceso-->
						<sj:tab id="tabDuranteEnProceso%{id}"
							target="divDuranteEnProceso%{id}" label="En proceso" />
						<s:div id="divDuranteEnProceso%{id}">					EnProceso					</s:div>
						<!-- Pestaña de Holdeados-->
						<sj:tab id="tabDuranteHoldeados%{id}"
							target="divDuranteHoldeados%{id}" label="Holdeados" />
						<s:div id="divDuranteHoldeados%{id}">					Holdeados					</s:div>
					</s:if>
					<!-- Si se trata de la bitacora actual -->
					<s:else>
					
						<!-- Pestaña de Incidentes-->

						<sj:tab id="tabDuranteIncidentes%{id}"
							target="divDuranteIncidentes%{id}" label="Incidentes" />
						<s:div id="divDuranteIncidentes%{id}">

							<s:hidden id="hdnParaFU"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@PARA_FU}" />
							<s:hidden id="hdnDesdeFU"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@DESDE_FU}" />
							<s:hidden id="hdnDesdeCCR"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@DESDE_CCR}" />
							<s:hidden id="hdnParaCCR"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@PARA_CCR}" />
							<s:hidden id="hdnEnValidacion"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@EN_VALIDACION}" />
							<s:hidden id="hdnProduccion"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@PRODUCCION}" />
							<s:hidden id="hdnGSI"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService@GSI}" />
							<s:hidden id="hdnIdFU"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO}" />
							<s:hidden id="hdnIdProduccion"
								value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@PRODUCCION}" />
								
								
							<table class="tablaTree" border="0">
							<br />
						 <div id="logMensajes" style="overflow:hidden; width: 1200px; height: 100px;">Area de Mensajes
							</div>	
							
							<BR/>
							<BR/>
							<BR/>
							
							
														
								<tr>
									<td><div id="notaccordion">

											<s:if
												test="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}) or (@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_SOPORTE in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} and #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].cdArea eq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO)">
												<h3>
													<a href="#"  title="Para Frente Unico">Para FU</a>
												</h3>
												<div>
													<sjt:tree id="treParaFU" title="" jstreetheme="default"></sjt:tree>
												</div>
												<h3>
													<a href="#">Desde FU</a>
												</h3>
												<div>
													<sjt:tree id="treDesdeFU" jstreetheme="default"></sjt:tree>
												</div>
											</s:if>

											<s:if
												test="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso})">
												<h3>
													<a href="#">Producción</a>
												</h3>
												<div>
													<sjt:tree id="treProduccion" jstreetheme="default"></sjt:tree>
												</div>
											</s:if>
											<s:if
												test="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}) or (@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_SOPORTE in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} and #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].cdArea != @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO)">
												<h3>
													<a href="#">Para CCR</a>
												</h3>
												<div>
													<sjt:tree id="treParaCCR" jstreetheme="default"></sjt:tree>
												</div>
												<h3>
													<a href="#">Desde CCR</a>
												</h3>
												<div>
													<sjt:tree id="treDesdeCCR" jstreetheme="default"></sjt:tree>
												</div>
											</s:if>

											<s:if
												test="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso})">
												<h3>
													<a href="#">En Validación</a>
												</h3>
												<div>
													<sjt:tree id="treEnValidacion" jstreetheme="default"></sjt:tree>
												</div>
												<h3>
													<a href="#">GSI</a>
												</h3>
												<div>
													<sjt:tree id="treGSI" jstreetheme="default"></sjt:tree>
												</div>
											</s:if>
										</div>
										
										</td>
										
										 
									<td rowspan="2">
									
										<div id="eneltop">
										
											<s:url id="urlCanceladosPendientes"
												value="bitacora!traerCanceladosPendientes" />
												<sjg:grid gridModel="canceladosPendientes"
												id="tblCanceladosPendientes" caption="Incidentes"
												pager="true" dataType="json" rowList="10,15,20,50,100,500"
												rowNum="50" loadonce="true" width="850"
												href="%{urlCanceladosPendientes}"
												loadingText="Cargando datos, espere por favor..."
												navigator="true" navigatorSearch="true"
												navigatorEdit="false" navigatorAdd="false"
												navigatorDelete="false"
												navigatorSearchOptions="{multipleSearch:true}"
												multiselect="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}) or (@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_SOPORTE in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} and #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].cdArea eq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO)"
												onCompleteTopics="tablaLista" shrinkToFit="false">
												<sjg:gridColumn name="id.fhCiclo" index="id.fhCiclo"
													title="ODate" sortable="true" formatter="date"
													formatoptions="{newformat : 'Y-m-d', srcformat : 'Y-m-d'}"
													search="true"
													searchoptions="{sopt:['eq','ne','lt','gt'], dataInit:fecCicloPick, attr:{title:'Fecha de busqueda'}}"
													width="95" />
												<sjg:gridColumn name="id.nbGrupo" index="id.nbGrupo"
													title="Grupo" sortable="true" width="180" />
												<sjg:gridColumn name="id.nbProceso" index="id.nbProceso"
													title="Proceso" sortable="true" width="150" />
												<sjg:gridColumn name="cdError" index="cdError"
													title="Código" sortable="true" width="110" />
												<sjg:gridColumn name="id.cdPaso" index="id.cdPaso"
													title="Paso" sortable="true" width="130" />
												<sjg:gridColumn name="origen.nbOrigen"
													index="origen.nbOrigen" title="Origen" sortable="true"
													width="120" />

												<sjg:gridColumn name="canceladoId" title="CanceladoId"
													sortable="false" formatter="formateadorCanceladoId"
													hidden="true" />
												<sjg:gridColumn name="id.cdEmpresa" title="Empresa"
													hidden="true" />
												<sjg:gridColumn name="id.cdOrigen" title="Origen"
													hidden="true" />
												<sjg:gridColumn name="cdCancelado" title="" key="true"
													hidden="true" />
												<sjg:gridColumn name="cdCanceladoPrincipal" title=""
													hidden="true" />
											</sjg:grid>
											<s:if
												test="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}) or (@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_SOPORTE in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} and #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].cdArea eq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO)">
												<sj:a id="lnkDocumentarCancelados" 
													onClickTopics="documentarCancelados" button="true"
													>Asignar...</sj:a>
											</s:if>
											<!-- 
											<s:url id="urlDocumentarPorAdelantado"
												action="documentacion-adelantada" includeContext="true" />
											<sj:a id="lnkDocumentarAdelantado" button="true"
												buttonIcon="ui-icon-pencil"
												buttonIconSecondary="ui-icon-clock" href="#"
												onclick="location.href='%{urlDocumentarPorAdelantado}'">Documentar por adelantado</sj:a>
												 -->
										</div>
									</td>
								</tr>
							</table>
						</s:div>


						<!-- Pestaña de Holdeados-->
						<sj:tab id="tabDuranteHoldeados%{id}"
							target="divDuranteHoldeados%{id}" label="Holdeados" />
						<s:div id="divDuranteHoldeados%{id}">

							<s:url action="documentacion-holdeado" includeContext="true"
								id="urlDocumentacionHoldeados" />
							<sj:div id="divDocumentacionHoldeados"
								href="%{urlDocumentacionHoldeados}"
								reloadTopics="refrescarHoldeados"
								cssClass="result ui-widget-content ui-corner-all"
								requestType="GET"></sj:div>
							<sj:a id="lnkRefrescarHoldeados"
								onClickTopics="refrescarHoldeados" button="true"
								indicator="indicadorCagandoHoldeados"
								errorElementId="indicadorErrorCagandoHoldeados">Refrescar holdeados</sj:a>

							<img id="indicadorCagandoHoldeados" src="images/load1.gif"
								alt="Cacargando holdeados..." style="display: none" />
							<img id="indicadorErrorCagandoHoldeados" src="images/load1.gif"
								alt="No se pudieron recargar los holdeados, da click en RECARGAR"
								style="display: none" />
						</s:div>
						<!-- Pestaña de Pendientes-->
						<sj:tab id="tabDurantePendientes%{id}"
							target="divDurantePendientes%{id}" label="Procesos retrasados" />
						<s:div id="divDurantePendientes%{id}">
							<s:actionmessage id="msgRetrasados" theme="jquery" />
							<s:url action="documentacion-proceso-retrasado"
								includeContext="true" id="urlDocumentarProcesoRetrasado" />
							<sj:div id="divDocumentarProcesoRetrasado"
								href="%{urlDocumentarProcesoRetrasado}"
								reloadTopics="refrescarRetrasados"
								cssClass="result ui-widget-content ui-corner-all"
								requestType="GET"></sj:div>
							<br />
							<sj:a id="lnkRefrescarRetrasados"
								onClickTopics="refrescarRetrasados" button="true"
								>Refrescar procesos retrasados</sj:a>
						</s:div>
						<!--  Pestaña de Especiales -->
						<sj:tab id="tabDuranteEspeciales%{id}"
							target="divDuranteEspeciales%{id}" label="Procesos Especiales" />
						<s:div id="divDuranteEspeciales%{id}">
							<s:actionmessage id="msgEspeciales" theme="jquery" />
							<s:url action="documentacion-seguimiento-proceso-especial"
								includeContext="true"
								id="urlDocumentarSeguimientoProcesoEspecial" />
							<sj:div id="divDocumentarProcesoEspecial"
								href="%{urlDocumentarSeguimientoProcesoEspecial}"
								reloadTopics="refrescarEspeciales"
								cssClass="result ui-widget-content ui-corner-all"
								requestType="GET" formIds="frmEspeciales"></sj:div>
							<br />
							<s:form id="frmEspeciales" theme="simple">
								<s:text name="fechaCampo" />
								<sj:datepicker name="fechaSel" value="today"
									displayFormat="yy-mm-dd" />
								<br />
								<s:text name="tipoFechaCampo" />
								<sj:radio list="#{true:'ODate',false:'Fecha del evento'}"
									value="false" name="usarODate" />
								<br />
								<s:text name="tipoProcesoEspecial" />
								<sj:radio
									list="#{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial@HITO:'Hito',
								@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial@CHECKLIST:'Checklist',
								@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial@BR:'BR'
								}"
									value="%{@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial@HITO}"
									name="tipoProcesoEspecialSel" />
							</s:form>
							<sj:a id="lnkRefrescarEspeciales"
								onClickTopics="refrescarEspeciales" button="true"
								indicator="indicadorCagandoEspeciales"> Refrescar procesos especiales</sj:a>

							<img id="indicadorCagandoEspeciales" src="images/load1.gif"
								alt="No se pudieron recargar los especiales, da click en RECARGAR"
								style="display: none" />
						</s:div>
						<!-- Pestaña de Mensajes-->
						<sj:tab id="tabDuranteMensajes%{id}"
							target="divDuranteMensajes%{id}" label="Mensajes" />
						<s:div id="divDuranteMensajes%{id}">
							<s:actionmessage id="msgGestionarMensajes" theme="jquery" />
							<s:url action="gestionar-mensaje" includeContext="true"
								id="urlGestionarMensaje" />
							<sj:div id="divGestionarMensaje" href="%{urlGestionarMensaje}"
								reloadTopics="refrescarMensajes"
								cssClass="result ui-widget-content ui-corner-all"
								requestType="GET"></sj:div>
							<sj:a id="lnkRefrescarMensajes" onClickTopics="refrescarMensajes"
								button="true" >Refrescar mensajes</sj:a>
						</s:div>
					</s:else>
				</sj:tabbedpanel>
				<!-- Termina Durante-->

				<s:if test="#estatus.index!=list.size-1">
					<!-- Pestaña de Delega-->
					<sj:tab id="tabDelega%{id}" target="tbpDelega%{id}" label="Delega" />
					<sj:tabbedpanel id="tbpDelega%{id}" useSelectedTabCookie="true">
						<!-- Pestaña de Equipo/Sistema-->
						<sj:tab id="tabDelegaES%{id}" target="divDelegaES%{id}"
							label="Equipo/Sistema" />
						<s:div id="divDelegaES%{id}">
					Equipo Sistema
					</s:div>
						<!-- Pestaña de Cambios-->
						<sj:tab id="tabDelegaCambios%{id}" target="divDelegaCambios%{id}"
							label="Cambios" />
						<s:div id="divDelegaCambios%{id}">
					Cambios
					</s:div>
						<!-- Pestaña de Incidentes-->
						<sj:tab id="tabDelegaIncidentes%{id}"
							target="divDelegaIncidentes%{id}" label="Incidentes" />
						<s:div id="divDelegaIncidentes%{id}">
					Incidentes
					</s:div>
						<!-- Pestaña de Pendientes-->
						<sj:tab id="tabDelegaPendientes%{id}"
							target="divDelegaPendientes%{id}" label="Pendientes" />
						<s:div id="divDelegaPendientes%{id}">
					Pendientes
					</s:div>
						<!-- Pestaña de EnProceso-->
						<sj:tab id="tabDelegaEnProceso%{id}"
							target="divDelegaEnProceso%{id}" label="En proceso" />
						<s:div id="divDelegaEnProceso%{id}">
					EnProceso
					</s:div>
						<!-- Pestaña de Holdeados-->
						<sj:tab id="tabDelegaHoldeados%{id}"
							target="divDelegaHoldeados%{id}" label="Holdeados" />
						<s:div id="divDelegaHoldeados%{id}">
					Holdeados
					</s:div>
					</sj:tabbedpanel>
					<!-- Termina Delega-->
				</s:if>
			</sj:tabbedpanel>

		</s:iterator>
	</sj:tabbedpanel>
<BR /><BR />
	<s:url action="bitacora/%{model.id}/edit" id="urlGestionarDelegacion" />
	<sj:a id="button3" href="%{urlGestionarDelegacion}" button="true"
		onclick="location.href='%{urlGestionarDelegacion}'">Delegar</sj:a>
	<s:url id="urlMostrarCargas" action="mostrar-cargas"
		includeContext="true" />
	<sj:a  id="button2" href="javascript:void(0);"
		onclick="location.href='%{urlMostrarCargas}'" button="true"
		>Historial de cargas</sj:a>

	<sj:dialog id="dlgDocumentarCancelados" autoOpen="false" modal="true"
		title="Mover Incidente" showEffect="slide" hideEffect="explode" width="700"
		closeTopics="cerrarDialogoCancelados"
		openTopics="abrirDialogoCancelados">
		<s:form id="frmDocumentarCancelados"
			action="%{request.contextPath}/bitacora!moverCancelados"
			method="post" theme="simple">
			<label>Destino:</label>
			<s:select id="slcAreas"
				list="(@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} or (#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].cdArea != @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO and @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_SOPORTE in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}))?areasDisponibles:areasDisponibles.{? #this.cdArea eq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area@FRENTE_UNICO}"
				listKey="cdArea" listValue="nbArea" name="idAreaSel"
				cssClass="campo" /><BR/>
			<s:checkboxlist list="#{'true':'¿Mover como grupo?'}"
				name="moverComoGrupo" value="true" /><BR/><BR/>
			<table id="tblMoverCancelados">
				<thead>
					<tr>
						<th>ODate</th>
						<th>Grupo</th>
						<th>Proceso</th>
						<th>Código</th>
						<th>Paso</th>
					</tr>
				</thead>
				<tbody id="tbdDocumentarCancelados">
				
				</tbody>
			</table>
			<BR/>
			<sj:a id="lnkMoverCancelados" button="true"
				onclick="$.publish('moverCanceladosInicial');">Aceptar</sj:a>
		
		</s:form>

	</sj:dialog>

	</DIV>

</body>
	</html>
</jsp:root>
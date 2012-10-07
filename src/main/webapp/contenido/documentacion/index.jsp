<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sx="/struts-dojo-tags"
	xmlns:sj="/struts-jquery-tags" xmlns:sjc="/struts-jquery-chart-tags"
	xmlns:log="http://jakarta.apache.org/taglibs/log-1.0"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/holders.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />	
<head>
<jsp:text>
	<![CDATA[ 
			<script src='${pageContext.request.contextPath}/dwr/interface/DocumentacionController.js' type='text/javascript'></script>
			<script src="${pageContext.request.contextPath}/scripts/documentacion.js" type="text/javascript"></script>
			
		]]>
</jsp:text>
<title>Documentar cancelado</title>
</head>
<body>

	<s:url id="urlCancelar" value="bitacora" includeContext="true" />
	<img id="imgCargando"
		src="${pageContext.request.contextPath}/images/load1.gif"
		alt="Cargando datos, espere por favor..." style="display: none;" />
		
		<div id="bodyedituser" >
<div id="cssdocumenta" class="myindexdocumenta">
	<span class="psdg-bold">Captura de datos</span>

<div id="psdg-light">Documentación - Bitácora de Producción</div>	<p/>
<div id="formulafont">
<div id="logMensajes">
Área de mensajes
	<BR /><BR /><BR /><BR /><s:if
		test="!#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@BITACORA]">
		<s:a class="errorMessage">No se ha elegido una bitácora, no será
			posible documentar</s:a>
	</s:if>
	<div>
	<ul id="formerrors" class="errorMessage">
		<li style="display: none;"></li>
	</ul>
	<div>
		
			<s:a id="cdEmpresaError" class="errorMessage"></s:a>
		<div>
			<s:a id="cdLdapError" class="errorMessage"></s:a>
		</div>
		<div>
			<s:a id="fhCicloBitacoraError" class="errorMessage"></s:a>
		</div>
		<div>
			<s:a id="cdTurnoError" class="errorMessage"></s:a>
		</div>
	</div>
	</div>
	<div id="bodyedituser" >
<div id="cssdocumenta" class="myindexdocumenta">
	<p/>	
	<div id="	">
	
					<s:text name="txResponsabilidadCampo" />
					:
					<s:a  id="lblResponsabilidad"></s:a> 
		</div>
		<BR />
		<div id="responsabilidad">
				<s:text name="txCorrigeCampo" />
				:			
			<s:a id="lblCorrige"></s:a>
		</div>
		<BR />
		
		<div id="responsabilidad">
				<s:text name="cdEvidenciaCampo" />
				:			${bitacoraSel.empresa.cdEvidencia}
		</div>
<BR />
	<!-- Si no es la primera documentación, se hara uso de update -->
	<s:form id="frmDocumentarCancelado"
		action="%{#request.contextPath}/documentacion%{(canceladoPrincipalSel.cdEstadoCan neq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado@REGISTRADO)?'!update':''}"
		theme="simple" method="post" acceptcharset="UTF-8"
		enctype="multipart/form-data">

		<!-- Varibles necesarias para validación ajax -->
		<s:hidden id="hdnValidarSolamente" name="struts.validateOnly"
			value="true" />
		<s:hidden id="hdnValidacionJSON" name="struts.enableJSONValidation"
			value="true" />
		<!--Bandera que señala si el cancelado ha sido documentado anteriormente. Si su estado es "Registrado", significa que no ha sido movido de estado y, por lo tanto, no ha sido documentado-->
		<s:hidden
			value="%{canceladoPrincipalSel.cdEstadoCan neq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado@REGISTRADO}"
			id="hdnCanceladoDocumentado" />
		<!-- Tabla de cancelaciones relacionadas con esta documentacion -->
		 <div id="psdgraphics-com-table">
		
		<span class="psdg-bold">Cancelados elegidos</span><BR />
		<div id="ps-top">
		<table id="tblCanceladosSel">
			<thead>
				<tr>
					<th>Número</th>
					<th>ODate</th>
					<th>Grupo</th>
					<th>Proceso</th>
					<th>Paso</th>
				</tr>
			</thead>
						<tbody>
						
				<s:iterator value="canceladosSel">
					<tr class="${id==canceladoIdPrincipalSel?'principal':''}">
						<td>${cdCancelado} <s:hidden name="canceladoIdsSel"
								value="%{id}" />
						</td>
						<td>${fhCiclo}</td>
						<td>${nbGrupo}</td>
						<td>${nbProceso}</td>
						<td>${cdPaso}</td>
					</tr>
				</s:iterator>
				
			</tbody>
		</table>
			</div>
			</div>
		<br/>
		<br/>
		<!-- Síntoma -->
		<div class="nombreCampo">
			<span class="psdg-bold">Síntoma</span>
			<p id="txSintomaError" class="errorMessage"></p>
		</div>
		<s:url id="urlConfigEditor" value="/META-INF/lib/alex.jar"
			includeContext="true" />
		<sjr:ckeditor id="txaDocumentacionSintoma" name="model.txSintoma"
			customConfig="%{urlConfigEditor}" editorLocal="es" width="920"></sjr:ckeditor><br/><br />
		<!-- Impacto -->
		<div class="nombreCampo">
			<span class="psdg-bold">Impacto</span>
			<p id="txImpactoError" class="errorMessage"></p>
		</div>
		<sjr:ckeditor id="txaDocumentacionImpacto" name="model.txImpacto"
			editorLocal="es" customConfig="%{urlConfigEditor}" width="920"></sjr:ckeditor>

		<s:if
			test="%{canceladoPrincipalSel.cdEstadoCan neq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado@REGISTRADO}">
			<!-- Comienza área de documentación--><br/><br />
			<div id="psdgraphics-com-table">
			<div class="nombreCampo"><span class="psdg-bold">Historial de recuperación</span></div>
			 <div id="ps-top">
			<table class="tablaNormal">
				<thead>
					<tr>
						<th>Tiempo</th>
						<th>Usuario</th>
						<th>Acción de recuperación</th>
					</tr>	
				</thead>
				<tbody id="tbdRecuperaciones">
					<s:iterator value="canceladoPrincipalSel.documentacions"
						var="documentacion">
						<tr>
							<TD><s:property
									value="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil@getTimeStampFormateadoCorto(tmDocumentacion)" />
							</TD>
							<td><s:property value="usuario.cdLdap" /></td>
							<td>${txRecuperacion}</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			</div>
			</div>
		</s:if>
		<sj:div id="txRecuperacionError" cssClass="errorMessage"></sj:div>
		<div class="nombreCampo"><span class="psdg-bold">Recuperacion</span></div><br />
		<sjr:ckeditor id="txaDocumentacionRecuperacion"
			name="model.txRecuperacion" width="920"
			customConfig="%{urlConfigEditor}" editorLocal="es">
		</sjr:ckeditor>
		<br />
		<s:if
			test="%{canceladoPrincipalSel.cdEstadoCan neq @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado@REGISTRADO}">
			<sj:a  id="lnkEnviarRecuperacion" indicator="indicator" button="true"
				 cssClass="campo"
				onClickTopics="enviarDocumentacionRecuperacion"
				cssStyle="width:25%;">Enviar acción de recuperación</sj:a>
		</s:if>
		<!--Termina área de documentación-->

		<!-- Lista de áreas, al cambiar se recarga la lista de problemas --><br /><br />
		<div class="nombreCampo">
			Áreas
			<s:a id="idAreaSelError" class="errorMessage"></s:a>
		</div><BR />
		<s:select list="areasDisponibles" listKey="cdArea" listValue="nbArea"
			name="idAreaSel" id="slcAreas" cssClass="campo" />
		<!-- Lista de problemas--><br /><br />
		<div class="nombreCampo">
			Problemas
			<s:a id="cdProblemaError" class="errorMessage"></s:a>
		</div><BR />
		<s:url action="documentacion!traerProblemas" id="urlTraerProblemas"
			includeParams="none" includeContext="true" />
		<log:debug
			category="mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionController">Despues de poner el action</log:debug>
		<sj:select id="slcProblemas" list="problemasDisponibles"
			listKey="cdProblema" listValue="nbProblema" name="model.cdProblema"
			reloadTopics="recargarProblemas" href="%{urlTraerProblemas}"
			formIds="frmArea" cssClass="campo"
			onCompleteTopics="problemaSeleccionado"
			onChangeTopics="problemaSeleccionado"
			onSuccessTopics="problemaSeleccionado" />
		<log:debug
			category="mx.bbva.ccr.in
			tranet.bitacoraProduccion.batch.controller.DocumentacionController">Despues de poner el select de problemas</log:debug>
		<!-- Selección de estado --><br /><br />
		<div class="nombreCampo">
			Estado
			<s:a id="cdEstadoError" class="errorMessage"></s:a>
		</div>
		<s:url action="documentacion!traerEstados" id="urlTraerEstados"
			includeParams="none" includeContext="true" />
		<sj:radio list="estatusTodos" listKey="cdEstado" listValue="nbEstado"
			name="model.cdEstado" cssClass="campo" href="%{urlTraerEstados}"
			formIds="frmEstado" id="rdoEstados" reloadTopics="recargaEstados"
			buttonset="false" />
		<log:debug
			category="mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionController">Despues de los estados</log:debug>
<br /><br /><br />
		<div class="nombreCampo">
			Evidencia
			<s:a id="archivoEvidenciaError" class="errorMessage"></s:a>
		</div>
		<s:file name="archivoEvidencia" cssClass="campo" id="button3"/>

		<s:if test="model.ultimaEvidencia">
			<label class="campo">Ultima evidencia <s:property
					value="%{model.ultimaEvidencia.nbEvidencia}" />, registrada en <s:date
					name="model.ultimaEvidencia.tmEnvioEvid" format="yyyy-MM-dd HH:mm" />
			</label>
			<s:url id="urlBajarEvidencia" action="documentacion!bajarEvidencia"
				includeContext="true" includeParams="all" />
			<s:a href="%{urlBajarEvidencia}" cssClass="campo">Bajar evidencia</s:a>
		</s:if>
<br /><br /><br /><br />
		<s:if
			test="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@BITACORA]">
			<sj:a id="button1" button="true"
				onClickTopics="enviarDocumentacion" href="#">Aceptar</sj:a>
		</s:if>
		<sj:a href="#" button="true" id="button2"
			onclick="location.href='%{urlCancelar}'">
			Cancelar
		</sj:a>
	</s:form>
	<!-- Form necesario para que al hacer la petición ajax de problemas no se mande todo el formulario sino solo el id del área -->
	<s:form id="frmArea" cssStyle="display:none;">
		<s:hidden id="hdnAreaSel" name="idAreaSel" />
	</s:form>
	<!-- Form necesario para la petición ajax de estados, solo manda el id de cancelacion principal -->
	<s:form id="frmEstado" cssStyle="display:none;">
	</s:form>
	</div>
</div>
</div>	</div>
		</div>
		</div>
</body>
	</html>
</jsp:root>
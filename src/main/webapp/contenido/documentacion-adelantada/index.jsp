<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sx="/struts-dojo-tags"
	xmlns:sj="/struts-jquery-tags" xmlns:sjg="/struts-jquery-grid-tags"
	xmlns:log="http://jakarta.apache.org/taglibs/log-1.0"
	xmlns:sjr="/struts-jquery-richtext-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:text>
	<![CDATA[ 
			<script src="${pageContext.request.contextPath}/scripts/documentacion-adelantada.js" type="text/javascript"></script>
			]]>
</jsp:text>
<title>Documentar por adelantado cancelado</title>
</head>
<body>
	<h1>Captura de datos</h1>
	<s:actionerror theme="jquery" />
	<s:fielderror theme="jquery" />
	<taglib uri="http://ckeditor.com" prefix="ckeditor" > </taglib>
	<s:form id="frmDocumentarAdelantado"
		action="%{request.contextPath}/documentacion-adelantada"
		theme="simple" method="post">

		<!-- Tabla de cancelaciones relacionadas con esta documentacion -->
		<s:url action="documentacion-adelantada!traerCanceladoDocumentacions"
			id="urlTraerCanceladoDocumentacions" />
		<s:url action="documentacion-adelantada!dummy" id="urlDummy"
			includeContext="true" />
		<sjg:grid id="tblCanceladoDocumentacions"
			caption="Cancelaciones aún no registradas" dataType="json"
			href="%{urlTraerCanceladoDocumentacions}" editurl="%{urlDummy}"
			pager="true" navigator="true" navigatorEdit="true"
			navigatorView="false" navigatorDelete="true" navigatorSearch="false"
			navigatorRefresh="false"
			navigatorAddOptions="{height:280,reloadAfterSubmit:false}"
			navigatorEditOptions="{height:280,reloadAfterSubmit:false}"
			navigatorDeleteOptions="{height:280,reloadAfterSubmit:false}"
			gridModel="canceladosDocumentacionsSel" rowList="10,15,20"
			rowNum="15" multiselect="true" formIds="frmDocumentarAdelantado"
			editinline="true">
			<sjg:gridColumn name="identificador" index="identificador" title="ID"
				hidden="true" key="true" />
			<sjg:gridColumn name="fhCiclo" index="fhCiclo" title="ODate"
				sortable="true" editable="true" edittype="text"
				editrules="{required:true,date:true}" formatter="date"
				formatoptions="{newformat : 'Y-m-d', srcformat : 'Y-m-d'}"
				editoptions="{dataInit:fecCicloPick}" />
			<sjg:gridColumn name="nbGrupo" index="nbGrupo" title="Grupo"
				sortable="true" editable="true" edittype="text"
				editrules="{required:true}" />
			<sjg:gridColumn name="nbProceso" index="nbProceso" title="Proceso"
				sortable="true" editable="true" edittype="text"
				editrules="{required:true}" />
			<sjg:gridColumn name="cdPaso" index="cdPaso" title="Paso"
				sortable="true" editable="true" edittype="text"
				editrules="{required:true}" />
		</sjg:grid>


		<label>Síntoma</label>
		<s:url id="urlConfigEditor" value="/META-INF/lib/alex.jar" includeContext="true" />
		<sjr:ckeditor name="model.txSintoma" rows="10" cols="100" width="900"
		editorLocal="es"></sjr:ckeditor>

		<label>Recuperación</label>
		<sjr:ckeditor id="txaDocumentacionRecuperacion"
			name="model.txRecuperacion" 
			editorLocal="es" customConfig="%{urlConfigEditor}"></sjr:ckeditor>

		<label>Impacto</label>
		<sjr:ckeditor name="model.txImpacto" rows="100" cols="1000" width="1024"
			editorLocal="es" customConfig="%{urlConfigEditor}"></sjr:ckeditor>

		<label>Áreas</label>
		<s:select list="areasDisponibles" listKey="cdArea" listValue="nbArea"
			name="idAreaSel" id="slcAreas" cssClass="campo" />

		<label>Problemas</label>
		<s:url action="documentacion-adelantada!traerProblemas"
			id="urlTraerProblemas" includeParams="none" includeContext="true" />
		<sj:select list="problemasDisponibles" listKey="cdProblema"
			listValue="nbProblema" name="model.cdProblema"
			reloadTopics="recargarProblemas" href="%{urlTraerProblemas}"
			formIds="frmDocumentarAdelantado" cssClass="campo" />

		<label>Origen</label>
		<s:select list="origenesDisponibles" listValue="nbOrigen"
			listKey="cdOrigen" cssClass="campo" name="cdOrigenSel" />

		<s:div id="divCanceladoDocumentacions"></s:div>

	</s:form>
	<sj:a buttonIcon="ui-icon-star" button="true"
		onClickTopics="enviarForma">Aceptar</sj:a>
	<sj:a href="%{request.contextPath}/bitacora" button="true"
		buttonIcon="ui-icon-cancel" formIds="frmDummy" id="divDummy">Cancelar</sj:a>
	<sj:a href="%{request.contextPath}/bitacora" button="true"
		buttonIcon="ui-icon-trash" formIds="frmDummy">Eliminar</sj:a>
	<s:form id="frmDummy"></s:form>
	<s:div id="divDummy"></s:div>
</body>
	</html>
</jsp:root>
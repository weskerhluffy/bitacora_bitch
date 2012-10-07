<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:decorator="http://www.opensymphony.com/sitemesh/decorator"
	xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:s="/struts-tags"
	xmlns:sj="/struts-jquery-tags" xmlns:sjc="/struts-jquery-chart-tags"
	xmlns:log="http://jakarta.apache.org/taglibs/log-1.0"
	xmlns:sjg="/struts-jquery-grid-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<jsp:text>
		<![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
	</jsp:text>
	<jsp:text>
		<![CDATA[
		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
		]]>
	</jsp:text>
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<sj:head debug="true" jqueryui="true" jquerytheme="redmond" locale="es"
	compressed="false" defaultIndicator="indicadorCagando"
	defaultErrorText="Error" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/demo_page.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/demo_table.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/demo_table_jui.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/jquery.dataTables.css" />
 <link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/menu1.css"
	type="text/css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />
	
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />

	
	
	
	

<jsp:text>
	<![CDATA[ 
			<script src="${pageContext.request.contextPath}/scripts/mootools-core.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/mootools-more.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/log4javascript.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/log4javascript.conf.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/base/jquery.ui.datepicker.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/plugins/jquery.jqGrid.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/plugins/jquery.form.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/plugins/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/jquery.blockUI.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/jquery.frameready.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/jquery.feedback.js" type="text/javascript"></script>
			<script src='${pageContext.request.contextPath}/dwr/engine.js' type='text/javascript'></script>
			<script src="${pageContext.request.contextPath}/scripts/ccr-util.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/hoverIntent.js" type="text/javascript"></script>
			<script	src="${pageContext.request.contextPath}/scripts/menu1.js" type="text/javascript"></script>	
  			<script	src="${pageContext.request.contextPath}/scripts/quitarpublicidad.js" type="text/javascript"></script>
  			<script	src="${pageContext.request.contextPath}/scripts/jquery.dataTables.js" type="text/javascript"></script>
  			
			
		
  			
		]]>
</jsp:text>

<decorator:head />

</head>
<body>

<div id="header">
	

	<s:if
		test="#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO]">
			
		<div id="Salir"	>
			<s:a href="%{urlBajarEvidencia}" cssClass="campo">
				<s:url id="urlBajarEvidencia" value="/logoff" action="logoff"
					includeContext="true" includeParams="all" />
				<s:text name="cerrarSesion" />
			</s:a>
			<br></br>
		</div>
		<div id="Home">
			<A href="${pageContext.request.contextPath}/catalogo-usuario"></A>
								<s:text name="Home" />
					<br></br>
		</div>
	</s:if>
		
	 <div id='logo'>
		<image align="right"
			src="${pageContext.request.contextPath}/images/menu/logobitacora.png" ></image>
	</div> 
	
<div id='bbvalogo'>
		<image
			src="${pageContext.request.contextPath}/images/menu/bbva_logo.png"></image>
	</div> 
	
		
	
<style type="text/css">
* { margin:0;
    padding:0;
}
body {background-color:#ffffff;/*background:url(${pageContext.request.contextPath}/images/5.jpg;) no-repeat;*/
line-height: 18px;
font-family:BBVA Office Light, Arial;
font-size: 12px;
color: #000000; }
div#menu { margin:43px auto; }
div#copyright {
    font:11px 'BBVA Office Light', Arial;
    color:#000;
    text-indent:30px;
    padding:40px 0 0 0;
}
div#copyright a { color:#00bfff; }
div#copyright a:hover { color:#fff; }
</style>
<div id="menu">
	<ul class="menu" >
		<s:if
			test="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@ADMINISTRACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso}">
			<li class="parent"><a href="#"><SPAN>Administración </SPAN></a>
				<ul>
					<LI class="parent"><A
						href="${pageContext.request.contextPath}/catalogo-problema">Problemas</A>
					</LI>
					<LI class="parent"><A
						href="${pageContext.request.contextPath}/catalogo-usuario">Usuarios </A></LI>
					<LI class="parent"><A href="${pageContext.request.contextPath}/catalogo-area">Áreas </A></LI>
					<LI class="parent"><A
						href="${pageContext.request.contextPath}/catalogo-empresa">Empresas </A></LI>
					<LI class="parent"><A
						href="${pageContext.request.contextPath}/catalogo-dias-inhabiles">Días Habiles </A></LI>
					<LI class="parent"><A
						href="${pageContext.request.contextPath}/catalogo-proceso-especial">Procesos Críticos </A></LI>
				</ul></li>
		</s:if>
		<s:if
			test="@mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@BITACORA_OPERATIVA_CONSULTA in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} or @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_OPERACION in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} or @mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso@DOCUMENTAR_SOPORTE in #session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@USUARIO].permisos.{cdPermiso} ">
			<li class="parent" ><a><SPAN>Bitácoras operativas</SPAN></a>
				<UL>
					<LI><a>Batch</a>
						<UL>
							<LI><a>México</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'M'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>
									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'MXCB'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Casa de Bolsa'" />
										</s:a>
									</LI>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'MXAFP'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'AFP'" />
										</s:a>
									</LI>


								</UL>
							</LI>

							<LI><a>Puerto Rico</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'R'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>


								</UL>
							</LI>

							<LI><a>Chile</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'L'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>


									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'CLAFP'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'AFP'" />
										</s:a>
									</LI>


								</UL>
							</LI>


							<LI><a>Perú</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'P'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>


									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'PEAFP'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'AFP'" />
										</s:a>
									</LI>


								</UL>
							</LI>

							<LI><a>Colombia</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'C'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>


									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'COAFP'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'AFP'" />
										</s:a>
									</LI>
								</UL>
							</LI>

							<LI><a>Estados Unidos</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'W'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>

								</UL>
							</LI>

							<LI><a>Venezuela</a>
								<UL>

									<LI><s:url id="urlCdEmpresa" action="bitacora"
											includeContext="true">
											<s:param name="idEmpresaSel" value="'V'" />
										</s:url> <s:a href="%{urlCdEmpresa}">
											<s:property value="'Banco'" />
										</s:a>
									</LI>

								</UL>
							</LI>
						</UL>
					</LI>
				</UL>
			</li>
			
		</s:if>
	</ul>
	</div>
	</div>
	<BR />
	<BR />
	<BR />
	<input id="hdnRutaContexto" type="hidden"
		value="${pageContext.request.contextPath}" />

	<img id="indicadorCagando"
		src="${pageContext.request.contextPath}/images/load1.gif"
		alt="Loading..." style="display: none" />
		
		
	<decorator:body />
</body>
	</html>
</jsp:root>
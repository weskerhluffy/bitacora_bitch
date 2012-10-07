<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8" session="false" />
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="true" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>BITACORA DE PRODUCCIÓN</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/miestilo.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/temple.css" />

<jsp:text>
	<![CDATA[ 
				<script src="${pageContext.request.contextPath}/scripts/jqueryRotate.js" type="text/javascript"></script>
				<script src="${pageContext.request.contextPath}/scripts/login.js" type="text/javascript"></script>
				<script src="${pageContext.request.contextPath}/scripts/display.js" type="text/javascript"></script>
		]]>
</jsp:text>

</head>
<body >

<div>

	<img id="background" src="${pageContext.request.contextPath}/images/blue11.jpg" alt="" title="" /> 

</div>
	<s:actionerror />
	<s:fielderror />
	<div>
	
	<a href="#login-box" class="login-window"> <img ID="bitacora" src="${pageContext.request.contextPath}/images/bitacora3.png" border="0" alt="" /> </a>
	
		<!-- <a href="#login-box" class="login-window" id="bitacora"
			style="filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${pageContext.request.contextPath}/images/bitacora3.png', sizingMethod='scale' );"></a>
	-->
	</div> 
	
			<div id="login-box" class="login-popup">
			<a href="#" class="close"><img
			src="${pageContext.request.contextPath}/images/close_pop.png" 
			class="btn_close" title="Cerrar Ventana" alt="Close" /></a>
		<form method="post" class="signin"		
			action="${pageContext.request.contextPath}/login">
			<fieldset class="textbox">
							<label class="username"><input
					id="username" name="login" value="" type="text" autocomplete="on"
					placeholder="Usuario" /></label>

			</fieldset>
			<!-- button class="submit button" type="button">ENTRAR</button> -->
			<input type="submit" class="button3" value="ENTRAR" onclick = "example_ajax_request ()" />
<div id="example-placeholder">
  <p></p>
</div>
		</form>
	
	</div>
	
	<div id="flash">

		<object id="world2.swf" width="500px" height="500px"
			classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
			codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
			<param name="movie"
				value="${pageContext.request.contextPath}/scripts/world3.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#FFFFFF" />
			<param name="wmode" value="transparent" />
			<param name="scale" value="noscale" />
			<param name="salign" value="LT" />
			<param name="menu" value="false" />
			<embed type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer" width="600px"
				height="600px" bgcolor="#FFFFFF" wmode="transparent"
				src="${pageContext.request.contextPath}/scripts/world3.swf"
				quality="high" scale="noscale" menu="false" salign="LT" />
		</object>
	</div>
	
	<img ID="spa" src="${pageContext.request.contextPath}/images/SPA1.png"/> 
    <!-- <img ID="test" src="${pageContext.request.contextPath}/images/cargando.gif"/> --> 
    <img ID="ccr" src="${pageContext.request.contextPath}/images/ccr.png"/> 

	<div id="header"></div>
	<div id="posicionreloj">
		<div id="clockHolderchile">
			<div class="rotatingWrapperchile">
				<img id="hourchile"
					src="${pageContext.request.contextPath}/images/hour.png" />
			</div>
			<div class="rotatingWrapperchile">
				<img id="minchile"
					src="${pageContext.request.contextPath}/images/minute.png" />
			</div>
			<div class="rotatingWrapperchile">
				<img id="secchile"
					src="${pageContext.request.contextPath}/images/second.png" />
			</div>
			<img id="clockchile"
				src="${pageContext.request.contextPath}/images/clockchile.png" />
		</div>

		<div id="clockHoldermexico">
			<div class="rotatingWrappermexico">
				<img id="hourmexico"
					src="${pageContext.request.contextPath}/images/hour.png" />
			</div>
			<div class="rotatingWrappermexico">
				<img id="minmexico"
					src="${pageContext.request.contextPath}/images/minute.png" />
			</div>
			<div class="rotatingWrappermexico">
				<img id="secmexico"
					src="${pageContext.request.contextPath}/images/second.png" />
			</div>
			<img id="clockmexico"
				src="${pageContext.request.contextPath}/images/clock.png" />
		</div>

		<div id="clockHolderpr">
			<div class="rotatingWrapperpr">
				<img id="hourpr"
					src="${pageContext.request.contextPath}/images/hour.png" />
			</div>
			<div class="rotatingWrapperpr">
				<img id="minpr"
					src="${pageContext.request.contextPath}/images/minute.png" />
			</div>
			<div class="rotatingWrapperpr">
				<img id="secpr"
					src="${pageContext.request.contextPath}/images/second.png" />
			</div>
			<img id="clockpr"
				src="${pageContext.request.contextPath}/images/clockpr.png" />

		</div>

		<div id="clockHoldercolombia">
			<div class="rotatingWrappercolombia">
				<img id="hourcolombia"
					src="${pageContext.request.contextPath}/images/hour.png" />
			</div>
			<div class="rotatingWrappercolombia">
				<img id="mincolombia"
					src="${pageContext.request.contextPath}/images/minute.png" />
			</div>
			<div class="rotatingWrappercolombia">
				<img id="seccolombia"
					src="${pageContext.request.contextPath}/images/second.png" />
			</div>
			<img id="clockcolombia"
				src="${pageContext.request.contextPath}/images/clockcolombia.png" />
		</div>

		<div id="clockHolderperu">
			<div class="rotatingWrapperperu">
				<img id="hourperu"
					src="${pageContext.request.contextPath}/images/hour.png" />
			</div>
			<div class="rotatingWrapperperu">
				<img id="minperu"
					src="${pageContext.request.contextPath}/images/minute.png" />
			</div>
			<div class="rotatingWrapperperu">
				<img id="secperu"
					src="${pageContext.request.contextPath}/images/second.png" />
			</div>
			<img id="clockperu"
				src="${pageContext.request.contextPath}/images/clockperu.png" />
		</div>

		<div id="clockHoldereeuu">
			<div class="rotatingWrappereeuu">
				<img id="houreeuu"
					src="${pageContext.request.contextPath}/images/hour.png" />
			</div>
			<div class="rotatingWrappereeuu">
				<img id="mineeuu"
					src="${pageContext.request.contextPath}/images/minute.png" />
			</div>
			<div class="rotatingWrappereeuu">
				<img id="seceeuu"
					src="${pageContext.request.contextPath}/images/second.png" />
			</div>
			<img id="clockeeuu"
				src="${pageContext.request.contextPath}/images/clockeeuu.png" />
		</div>


		<div id="clockHoldermadrid">
			<div class="rotatingWrappermadrid">
				<img id="hourmadrid"
					src="${pageContext.request.contextPath}/images/hourmadrid.png" />
			</div>
			<div class="rotatingWrappermadrid">
				<img id="minmadrid"
					src="${pageContext.request.contextPath}/images/minutemadrid.png" />
			</div>
			<div class="rotatingWrappermadrid">
				<img id="secmadrid"
					src="${pageContext.request.contextPath}/images/secondmadrid.png" />
			</div>
			<img id="clockmadrid"
				src="${pageContext.request.contextPath}/images/clockmadrid.png" />
		</div>
	</div>
	
	
	
</body>
	</html>
</jsp:root>
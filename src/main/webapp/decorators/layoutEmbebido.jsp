<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:decorator="http://www.opensymphony.com/sitemesh/decorator"
	xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:s="/struts-tags"
	xmlns:sj="/struts-jquery-tags" xmlns:sjc="/struts-jquery-chart-tags"
	xmlns:log="http://jakarta.apache.org/taglibs/log-1.0"
	xmlns:sjg="/struts-jquery-grid-tags">
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<sj:head debug="true" jqueryui="true" jquerytheme="redmond" locale="es"
	compressed="false" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/demo_page.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/demo_table.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/demo_table_jui.css" />
 <link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/supfish.css"
	type="text/css" />
	
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/tablestyle.css" />
	
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/jquery.dataTables_themeroller.css" />
	<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/dataTables/media/css/jquery.dataTables.css" />
		
	
 <link rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/menu1.css"
	type="text/css" />
		
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/styles/bitacora.css" />

<jsp:text>
	<![CDATA[ 
			<script src="${pageContext.request.contextPath}/scripts/mootools-core.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/mootools-more.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/log4javascript.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/log4javascript.conf.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/base/jquery.ui.datepicker.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/plugins/jquery.jqGrid.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/plugins/jquery.form.min.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/struts/js/plugins/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
			
			<script src="${pageContext.request.contextPath}/scripts/jquery.blockUI.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/jquery.frameready.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/scripts/jquery.feedback.js" type="text/javascript"></script>
			<script src='${pageContext.request.contextPath}/dwr/engine.js' type='text/javascript'></script>
			<script src="${pageContext.request.contextPath}/scripts/ccr-util.js" type="text/javascript"></script>		
			<script	src="${pageContext.request.contextPath}/scripts/tabl.js" type="text/javascript"></script>
			
			<script	src="${pageContext.request.contextPath}/scripts/dataTables/media/js/jquery.dataTables.js" type="text/javascript"></script>
		
		
		
		]]>
</jsp:text>

<decorator:head />

</head>
<body>

	<decorator:body />

</body>
	</html>
</jsp:root>
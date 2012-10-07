/**
 * 
 */
$(function() {
	 $("#tblProcesoRetrasado").dataTable(
			 { "iDisplayLength": 50 }
	 );
	 $("#tblProcesoRetrasadoDocumentado").dataTable().rowGrouping({
		iGroupingColumnIndex : 8,
		sGroupingColumnSortDirection : "asc"
	});
//	 $("#tblProcesoRetrasadoDocumentado").dataTable();
});

$(document).ready(
	function(){
		$("#tblProcesosEspeciales").dataTable(		
				{  "iDisplayLength": 70 }		
		);
		$("#rdoEmpresas").change(function() {
			location.href=$("#hdnRutaContexto").val()+"/catalogo-proceso-especial?empSel="+$(this).val();
		});
	}
);
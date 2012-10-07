/**
 * 
 */
$(function() {
	$("#tblUsuarios").dataTable(	
			{  "iDisplayLength": 50 }
	);

	$.subscribe("perfilCambiado", function(event, data) {
		var cdPerfilSel = parseInt($("[name='cdPerfil']:checked").val(), 10);
		logger.trace("El perfil seleccionado es " + cdPerfilSel);
		CatalogoUsuarioController.traerPermisosDWR(cdPerfilSel, function(
				permisos) {
			var cdPermisosSel = [];
			var cdPermisoSel = null;
			for ( var int = 0; int < permisos.length; int++) {
				cdPermisoSel = permisos[int].id;
				logger.trace("El permiso seleccionado es " + cdPermisoSel);
				cdPermisosSel.push(cdPermisoSel);
			}
			$("[name='permisosSel']").val(cdPermisosSel);
			$("[name='permisosSel'] + label").removeClass("ui-state-active");
			logger.trace("Voy a beber "
					+ $("[name='permisosSel']:checked").length);
			$("[name='permisosSel']:checked + label").addClass(
					"ui-state-active");
			$("[name='permisosSel']:checked + label").attr("aria-pressed",
					"true");
		});
	});
});

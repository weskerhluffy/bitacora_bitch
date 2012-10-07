package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;

public interface ProcesoRetrasadoDao extends
		GenericDao<ProcesoRetrasado, Integer> {

	/**
	 * Obtiene los registros de procesos retrasados que aún no hayan sido
	 * documentados que pertenezcan a la empresa de <code>bitacora</code> o que
	 * tengan documentaciones asociadas a la bitacora <code>bitacora</code>.
	 * 
	 * @param bitacora
	 * @return La lista de procesos retrasados
	 */
	public List<ProcesoRetrasado> getProcesosRetrasadosVigentes(
			Bitacora bitacora);
}

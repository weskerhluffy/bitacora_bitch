package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.HoldeadoId;

public interface HoldeadoDao extends GenericDao<Holdeado, HoldeadoId> {

	/**
	 * Obtiene los registros de holdeados que aún no hayan sido documentados que
	 * pertenezcan a la empresa de <code>bitacora</code> y que no tengan
	 * cancelaciones asociadas o que tengan documentacion asociadas a la
	 * bitacora <code>bitacora</code>.
	 * 
	 * @param bitacora
	 * @return La lista de holdeados
	 */
	public List<Holdeado> getHoldeadosVigentes(Bitacora bitacora);
}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatos;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatosId;

public interface CargaDatosDao extends GenericDao<CargaDatos, CargaDatosId> {
	/**
	 * @param numUltimas
	 * @return Las últimas <code>numUltimas</code> cargas hechas para la empresa
	 *         asociada con <code>bitacora</code>
	 */
	public List<CargaDatos> getStatusUltimasCargas(Bitacora bitacora,
			Integer numUltimas);

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.CargaDatosDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatos;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatosId;

public interface ICargaDatosService extends
		IGenericoService<CargaDatos, CargaDatosId> {

	/**
	 * Delega a {@link CargaDatosDao#getStatusUltimasCargas(Bitacora, Integer)}
	 * con el segundo parametro puesto a 50.
	 * 
	 * @param bitacora
	 * @return
	 */
	public List<CargaDatos> getStatusUltimasCargas(Bitacora bitacora);
}

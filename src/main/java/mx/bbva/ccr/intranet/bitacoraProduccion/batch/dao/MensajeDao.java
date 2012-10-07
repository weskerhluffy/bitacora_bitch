package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;

public interface MensajeDao extends GenericDao<Mensaje, Integer> {

	/**
	 * Busca los mensajes principales (es decir, que no tengan referencia a otro
	 * mensaje como principal) que pertenezcan a la bitacora
	 * <code>bitacora</code> y/o que esten abiertos (que no tienen fecha de
	 * cierre o de borrado).
	 * 
	 * @param bitacora
	 * @return La lista de mensajes.
	 */
	public List<Mensaje> getMensajesVigentes(Bitacora bitacora);
}

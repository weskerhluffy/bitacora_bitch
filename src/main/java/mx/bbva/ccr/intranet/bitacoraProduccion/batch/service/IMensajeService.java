package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;
import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.MensajeDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoMensaje;

public interface IMensajeService extends IGenericoService<Mensaje, Integer> {

	/**
	 * Obtiene de {@link MensajeDao#getMensajesVigentes(Bitacora)} los mensajes
	 * vigentes, y los clasifica según su tipo.
	 * 
	 * @param bitacora
	 * @return Los mensajes vigentes clasificados en un mapa cuya llave es el
	 *         tipo de mensaje y el valor es la lista de los mensajes de ese
	 *         tipo.
	 */
	public Map<Integer, List<Mensaje>> getMensajesVigentes(Bitacora bitacora);

	/**
	 * 
	 * @return Los tipos de mensaje que no estan deshabilitados
	 */
	public List<TipoMensaje> getTipoMensajesHabilitados();

}

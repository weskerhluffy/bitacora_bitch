package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.HoldeadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;

public interface IDocumentacionHoldeadoService extends
		IGenericoService<DocumentacionHoldeado, Integer> {

	/**
	 * Delega a {@link HoldeadoDao#getHoldeadosVigentes(Bitacora)}.
	 * 
	 * @return Lista de holdeados vigentes
	 */
	public List<Holdeado> getHoldeadosVigentes(Bitacora bitacora);

	/**
	 * Se graba la documentación <code>documentacionHoldeado</code> asociandola
	 * a <code>holdeado</code>.
	 * 
	 * @param holdeado
	 * @param documentacionHoldeado
	 * @return El objeto <code>documentacionHoldeado</code> persistido.
	 */
	public DocumentacionHoldeado documentarHoldeado(Holdeado holdeado,
			DocumentacionHoldeado documentacionHoldeado);
}

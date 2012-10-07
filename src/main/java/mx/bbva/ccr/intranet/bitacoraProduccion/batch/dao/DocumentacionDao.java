package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;

public interface DocumentacionDao extends GenericDao<Documentacion, Integer> {
	/**
	 * Encuentra los cancelados que tengan asociada documentacion adelantada y
	 * les pone el estado y el problema de dicha documentación. Ademas asigna
	 * como cancelado principal aquel que haya sucedido primero en todos los
	 * registros menos en el principal mismo.
	 */
	public void sincronizarDocumentacionAdelantada();
}

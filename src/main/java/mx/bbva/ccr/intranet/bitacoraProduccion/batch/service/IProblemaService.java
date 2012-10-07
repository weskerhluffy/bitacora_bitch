package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;

public interface IProblemaService extends IGenericoService<Problema, Integer> {

	/**
	 * 
	 * @param problema
	 * @return True si el problema tiene cancelados asociados
	 */
	public Boolean problemaAsociado(Problema problema);

}

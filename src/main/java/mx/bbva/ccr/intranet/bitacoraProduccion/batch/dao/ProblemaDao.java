package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;

public interface ProblemaDao extends GenericDao<Problema, Integer> {

	/**
	 * 
	 * @param problema
	 * @return El número de cancelados asociados a este problema
	 */
	public Long cuentaCanceladosAsociados(Problema problema);

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.ProblemaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IProblemaService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;

@Singleton
@Named("problemaService")
public class ProblemaService extends GenericoService<Problema, Integer>
		implements IProblemaService {
	public void setProblemaDao(ProblemaDao dao) {
		setEntidadDao(dao);
	}

	@Transactional
	@Override
	public Boolean problemaAsociado(Problema problema) {
		Long numCancelados = ((ProblemaDao) getEntidadDao())
				.cuentaCanceladosAsociados(problema);
		logger.trace("El numero de cancealdos de " + problema + " es "
				+ numCancelados);
		return numCancelados > 0;
	}


	public ProblemaDao getProblemaDao() {
		return (ProblemaDao) entidadDao;
	}
}

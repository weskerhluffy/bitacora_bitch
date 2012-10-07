package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.ProblemaHibernateDao;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {
		"file:src/test/resources/contexto-tests.xml",
		"file:src/main/resources/applicationContext.xml" })
public class ProblemaDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(ProblemaDaoTests.class);

	@Inject
	ProblemaHibernateDao problemaDao;

	@Test
	public void pruebaCuentaCancelados() {
		Long numCancelados;
		numCancelados = problemaDao.cuentaCanceladosAsociados(problemaDao
				.findAll().get(0));
		logger.trace("Se encontraron " + numCancelados + " cancelados");
		assert numCancelados != null;
	}

}

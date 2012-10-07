package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.DocumentacionHibernateDao;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class DocumentacionHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(DocumentacionHibernateDaoTests.class);

	@Inject
	DocumentacionHibernateDao documentacionDao;

	@Test(enabled = true)
	public void mierda() {
		documentacionDao.sincronizarDocumentacionAdelantada();
	}

}

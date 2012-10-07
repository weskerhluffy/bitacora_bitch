package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.BitacoraHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.HoldeadoHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class HoldeadoHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(HoldeadoHibernateDaoTests.class);

	@Inject
	HoldeadoHibernateDao holdeadoDao;
	@Inject
	BitacoraHibernateDao bitacoraDao;

	@Test(enabled = true)
	public void mierda() {
		List<Bitacora> bitacoras = null;
		List<Holdeado> holdeados;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		holdeados = holdeadoDao.getHoldeadosVigentes(bitacoras.get(0));
		for (Holdeado holdeado : holdeados) {
			logger.trace("Holdeado " + holdeado + " con "
					+ holdeado.getDocumentacionHoldeados().size()
					+ " documentaciones ");
			for (DocumentacionHoldeado documentacionHoldeado : holdeado
					.getDocumentacionHoldeados()) {
				logger.trace("Tiene documentacion " + documentacionHoldeado);
			}
		}
		assert holdeados.size() > 0;
	}

}

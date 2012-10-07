package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.BitacoraHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.ProcesoRetrasadoHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionProcesoRetrasado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class ProcesoRetrasadoHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(ProcesoRetrasadoHibernateDaoTests.class);

	@Inject
	ProcesoRetrasadoHibernateDao procesoRetrasadoDao;
	@Inject
	BitacoraHibernateDao bitacoraDao;

	@Test(enabled = true)
	public void mierda() {
		List<Bitacora> bitacoras = null;
		List<ProcesoRetrasado> procesoRetrasados;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		procesoRetrasados = procesoRetrasadoDao.getProcesosRetrasadosVigentes(bitacoras.get(0));
		for (ProcesoRetrasado procesoRetrasado : procesoRetrasados) {
			logger.trace("ProcesoRetrasado " + procesoRetrasado + " con "
					+ procesoRetrasado.getDocumentacionProcesoRetrasados().size()
					+ " documentaciones ");
			for (DocumentacionProcesoRetrasado documentacionProcesoRetrasado : procesoRetrasado
					.getDocumentacionProcesoRetrasados()) {
				logger.trace("Tiene documentacion " + documentacionProcesoRetrasado);
			}
		}
		assert procesoRetrasados.size() > 0;
	}

}

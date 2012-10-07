package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.BitacoraHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.CargaDatosHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatos;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class CargaDatosHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(CargaDatosHibernateDaoTests.class);

	@Inject
	BitacoraHibernateDao bitacoraDao;
	@Inject
	CargaDatosHibernateDao cargaDatosDao;

	@Test
	public void probarUltimasCargas() {
		List<Bitacora> bitacoras;
		List<CargaDatos> cargaDatos;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		logger.trace("Para la empresa " + 0 + " se encontraron "
				+ bitacoras.size() + " bitacoras");
		cargaDatos = cargaDatosDao.getStatusUltimasCargas(bitacoras.get(0), 3);

		for (CargaDatos cargaDatosCaca : cargaDatos) {
			logger.trace("La informacion de carga es " + cargaDatosCaca);
		}

		assert cargaDatos.size() > 0;
	}
}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.BitacoraHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.SeguimientoProcesoEspecialHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class SeguimientoProcesoEspecialHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger
			.getLogger(SeguimientoProcesoEspecialHibernateDaoTests.class);

	@Inject
	SeguimientoProcesoEspecialHibernateDao seguimientoProcesoEspecialDao;
	@Inject
	BitacoraHibernateDao bitacoraDao;

	@Test(enabled = true)
	public void mierda() {
		List<Bitacora> bitacoras = null;
		List<SeguimientoProcesoEspecial> seguimientoProcesoEspecials;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		seguimientoProcesoEspecials = seguimientoProcesoEspecialDao
				.getSeguimientosProcesoEspecial(bitacoras.get(0), new Date(),
						false, TipoProcesoEspecial.HITO);
		assert seguimientoProcesoEspecials.size() > 0;
	}

}

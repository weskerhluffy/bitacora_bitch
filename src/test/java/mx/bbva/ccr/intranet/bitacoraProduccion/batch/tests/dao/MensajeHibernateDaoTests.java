package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.BitacoraHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.MensajeHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class MensajeHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(MensajeHibernateDaoTests.class);

	@Inject
	MensajeHibernateDao mensajeDao;
	@Inject
	BitacoraHibernateDao bitacoraDao;

	@Test(enabled = true)
	public void mierda() {
		List<Bitacora> bitacoras = null;
		List<Mensaje> mensajes;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		mensajes = mensajeDao.getMensajesVigentes(bitacoras.get(2));
		for (Mensaje mensaje : mensajes) {
			logger.trace("Mensaje " + mensaje + " con mensajes secundarios "
					+ mensaje.getMensajesSecundarios().size());
			for (Mensaje mensajeSecundario : mensaje.getMensajesSecundarios()) {
				logger.trace("Tiene mensaje secundario " + mensajeSecundario);
			}
		}
		assert mensajes.size() > 0;
	}

}

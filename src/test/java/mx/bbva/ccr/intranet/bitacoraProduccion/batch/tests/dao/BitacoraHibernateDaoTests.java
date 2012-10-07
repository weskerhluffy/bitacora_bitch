package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.BitacoraHibernateDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Origen;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class BitacoraHibernateDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(BitacoraHibernateDaoTests.class);

	@Inject
	BitacoraHibernateDao bitacoraDao;

	@Test(enabled = false)
	public void mierda() {
		List<Bitacora> bitacoras = null;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		assert bitacoras.get(0).getDocumentacionesEnviadas() != null
				&& bitacoras.get(0).getDocumentacionesNoEnviadas() != null;
	}

	@Test(enabled = false)
	public void probarTraerPorIds() {
		List<Bitacora> bitacoras = null;
		List<Integer> bitacoraIds = new ArrayList<Integer>();
		Integer numInicial = 0;
		Integer numFinal = 0;
		bitacoras = bitacoraDao.getUltimasBitacoras(Empresa.MEXICO, 3);
		numInicial = bitacoras.size();
		for (Bitacora bitacora : bitacoras) {
			bitacoraIds.add(bitacora.getId());
		}
		bitacoras = bitacoraDao.findByIds(Bitacora.class, bitacoraIds);
		numFinal = bitacoras.size();
		assert numInicial.equals(numFinal);
	}

	@Test(enabled = false)
	public void probarComparacionCanceladoId() {
		CanceladoId caca, mierda;
		Date fecha = new Date();
		caca = new CanceladoId("", "", "", "", fecha, Origen.HOST);
		mierda = new CanceladoId("", "", "", "", fecha, Origen.HOST);
		logger.trace("el cancelado id es " + caca);
		assert caca.equals(mierda);
	}

	@Test(enabled = false)
	public void probarCanceladosActuales() {
		Bitacora bitacora = null;
		List<Bitacora> bitacoras;

		bitacoras = bitacoraDao.findAll();

		bitacora = bitacoras.get(bitacoras.size() - 1);
		logger.trace("Ejecutando getCanceladosActuales con " + bitacora);

		assert bitacoraDao.getCanceladosAsignados(bitacora).size() > 0;
	}

	@Test
	public void probarUltimasBitacoras() {
		List<Bitacora> bitacoras;
		bitacoras = bitacoraDao.getUltimasBitacoras("M", 3);
		logger.trace("Para la empresa " + 0 + " se encontraron "
				+ bitacoras.size() + " bitacoras");
		assert bitacoras.size() > 0;
	}
}

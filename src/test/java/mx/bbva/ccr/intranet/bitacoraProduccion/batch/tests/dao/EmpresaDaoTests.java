package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.dao;

import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.EmpresaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class EmpresaDaoTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(EmpresaDaoTests.class);

	@Inject
	EmpresaDao empresaDao;

	@Test
	public void pruebaEmpresas() {
		List<Empresa> empresas;
		empresas = empresaDao.findAll();
		assert empresas.size() > 0;
	}

}

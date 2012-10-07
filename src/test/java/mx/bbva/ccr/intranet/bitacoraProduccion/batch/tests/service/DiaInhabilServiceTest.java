package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDiaInhabilService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class DiaInhabilServiceTest extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(DiaInhabilServiceTest.class);
	@Inject
	IDiaInhabilService diaInhabilService;

	@Test(enabled = true)
	public void probarCaca() {
		List<String> fechasO = new ArrayList<String>();
		List<String> fechasS = new ArrayList<String>();

		Empresa emp;

		emp = diaInhabilService.traerPorIdGenerico(Empresa.class, "M");

		fechasS.add("2012-03-03");
		fechasS.add("2012-04-03");

		for (DiaInhabil diaInhabil : emp.getDiasInhabiles()) {
			fechasO.add(CcrUtil.getFechaFormateadaCorta(diaInhabil.getId()
					.getFhInhabil()));
		}

		diaInhabilService.agregarFechaInhabil("M", fechasO, fechasS);

	}

}

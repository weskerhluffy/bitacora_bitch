package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Origen;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {
		"file:src/test/resources/contexto-tests.xml",
		"file:src/main/resources/applicationContext.xml" })
public class DocumentacionControllerTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(DocumentacionControllerTests.class);

	@Inject
	DocumentacionController documentacionController;

	@Test(enabled = true)
	public void probarComparacionCanceladoId() {
		Bitacora bitacora = new Bitacora();
		bitacora.setCdEmpresa("MX");
		documentacionController.setIdAreaSel(1);
		documentacionController.setCanceladoPrincipalSel(new Cancelado() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7589906195853477053L;
			{
				this.setCdEmpresa("MX");
				this.setCdEstadoCan(Estado.REGISTRADO);
			}
		});
//		documentacionController.setCanceladoIdPrincipalSel(new CanceladoId("MX",
//				"AFOREDIA", "AFGCCD06", "AFGCP019", new Date(1290578400000L),
//				Origen.HOST));
		documentacionController
				.setCanceladoIdsSel(new ArrayList<CanceladoId>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 8833593397165501036L;
					{
						this.add(new CanceladoId("MX", "AFOREDIA", "AFGCCD06",
								"AFGCP019", new Date(1290578400000L),
								Origen.HOST));
						this.add(new CanceladoId("MX", "AFOREDIA", "AFGCCD06",
								"CONTROLR", new Date(1290578400000L),
								Origen.HOST));
					}
				});

		documentacionController.setBitacoraSel(documentacionController
				.getCatalogoGenericoService()
				.encontrarPorInstanciaEjemploGenerico(bitacora).get(0));
		documentacionController.anadirRecuperacion();
	}
}

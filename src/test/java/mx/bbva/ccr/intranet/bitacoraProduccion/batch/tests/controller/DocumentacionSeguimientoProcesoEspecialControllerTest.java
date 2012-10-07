package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.controller;

import java.util.Date;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionSeguimientoProcesoEspecialController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionSeguimientoProcesoEspecialService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecialId;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {
		"file:src/test/resources/contexto-tests.xml" })
public class DocumentacionSeguimientoProcesoEspecialControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	Logger logger = Logger.getLogger(DocumentacionSeguimientoProcesoEspecialControllerTest.class);
	
	@Inject
	DocumentacionSeguimientoProcesoEspecialController seguimientoProcesoEspecialController;
	
	IDocumentacionSeguimientoProcesoEspecialService documentacionSeguimientoProcesoEspecialService;
	
	SeguimientoProcesoEspecial seguimientoProcesoEspecial;
	SeguimientoProcesoEspecialId seguimientoProcesoEspecialId;
	
	@Test(enabled = true)
	public void probarTraerByIdGenerico(){
		
		Date tmIni=new Date(1327950057000L);
		Date fhCic=new Date(1327903200000L);
		/*
		String fechaTexto="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		
		fecha=format.parse(fechaTexto);
		*/
		
		//M_DSOPSMID_DSJOPSEC_1327950057000_1327903200000_1
		seguimientoProcesoEspecialId=new SeguimientoProcesoEspecialId("M", "DSOPSMID", "DSJOPSEC", 1,fhCic,tmIni);
		
		seguimientoProcesoEspecial = seguimientoProcesoEspecialController.getDocumentacionSeguimientoProcesoEspecialService().traerPorIdGenerico(SeguimientoProcesoEspecial.class, seguimientoProcesoEspecialId);
		
		System.out.println("Mierda :"+seguimientoProcesoEspecial);

		
	}
}

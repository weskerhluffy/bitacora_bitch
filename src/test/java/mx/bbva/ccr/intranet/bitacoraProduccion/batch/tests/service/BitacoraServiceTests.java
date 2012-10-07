package mx.bbva.ccr.intranet.bitacoraProduccion.batch.tests.service;

import java.util.List;

import javax.inject.Inject;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;

import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "file:src/test/resources/contexto-tests.xml" })
public class BitacoraServiceTests extends
		AbstractTransactionalTestNGSpringContextTests {
	Logger logger = Logger.getLogger(BitacoraServiceTests.class);
	@Inject
	IBitacoraService bitacoraService;

	@Test(enabled = false)
	public void probarCanceladosActuales() {
		List<Cancelado> cancelados = null;
		Cancelado canceladoEjemplo = new Cancelado();
		// canceladoEjemplo.setCdEstadoCan(1);
		// cancelados = bitacoraService.getCanceladosActuales(bitacoraService
		// .traerTodos().get(0).getId());
		cancelados = bitacoraService
				.encontrarPorInstanciaEjemploGenerico(canceladoEjemplo);
		for (Cancelado cancelado : cancelados) {
			logger.trace("El ultimo sintoma es "
					+ cancelado.getUltimaDocumentacion().getId());
			// logger.trace("El ultimo impacto es "
			// + cancelado.getUltimoImpacto().getId());
			logger.trace("El cacancelado padre es "
					+ cancelado.getCanceladoPrincipal().getId());
			// logger.trace("Es principal "
			// + cancelado.getCanceladosDocumentacionPrincipal().size());
		}
		assert cancelados.size() > 0;
	}

	@Test
	public void probarClasificarCancelados() {
		Bitacora bitacora = null;
		List<Bitacora> bitacoras;
		List<Cancelado> cancelados;

		bitacoras = bitacoraService.traerTodos();

		bitacora = bitacoras.get(bitacoras.size() - 1);
		logger.trace("Ejecutando getCanceladosActuales con " + bitacora);

		cancelados = bitacoraService.getCanceladosActuales(bitacora);
		assert bitacoraService.clasificaCancelados(cancelados).size() > 0;
	}
}

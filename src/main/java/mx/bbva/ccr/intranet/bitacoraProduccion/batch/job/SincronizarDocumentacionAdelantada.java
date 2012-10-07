package mx.bbva.ccr.intranet.bitacoraProduccion.batch.job;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
//import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;

//FIXME Descomentar antes de haceer el war.
//@Lazy(false)
@Singleton
@Named
public class SincronizarDocumentacionAdelantada {

	private Logger logger = Logger
			.getLogger(SincronizarDocumentacionAdelantada.class);

	@Inject
	IDocumentacionService documentacionService;

	@Scheduled(fixedRate = 60000)
	public void sincronizarDocumentacionAdelantada() {
		logger.trace("Sincronizando la documentación adelantada");
		documentacionService.sincronizarDocumentacionAdelantada();
	}
}

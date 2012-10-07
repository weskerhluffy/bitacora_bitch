package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionHoldeadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.HoldeadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionHoldeadoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Origen;

@Singleton
@Named("documentacionHoldeadoService")
public class DocumentacionHoldeadoService extends
		GenericoService<DocumentacionHoldeado, Integer> implements
		IDocumentacionHoldeadoService {
	private HoldeadoDao holdeadoDao;

	public void setDocumentacionHoldeadoDao(
			DocumentacionHoldeadoDao documentacionHoldeadoDao) {
		setEntidadDao(documentacionHoldeadoDao);
	}

	@Transactional
	@Override
	public List<Holdeado> getHoldeadosVigentes(Bitacora bitacora) {
		return getHoldeadoDao().getHoldeadosVigentes(bitacora);
	}

	@Transactional
	@Override
	public DocumentacionHoldeado documentarHoldeado(Holdeado holdeado,
			DocumentacionHoldeado documentacionHoldeado) {
		DocumentacionHoldeado documentacionHoldeadoNueva = null;
		documentacionHoldeado.setCdEmpresa(holdeado.getId().getCdEmpresa());
		documentacionHoldeado.setCdOrigen(holdeado.getId().getCdOrigen());
		documentacionHoldeado.setCdCiclo(holdeado.getId().getCdCiclo());
		documentacionHoldeado.setTmEvento(holdeado.getId().getTmEvento());
		documentacionHoldeado.setTmDocumentacion(new Date());

		if (holdeado.getId().getCdOrigen().equals(Origen.HOST)) {
			documentacionHoldeado.setTxDocumentacion(documentacionHoldeado
					.getTxDocumentacion().toUpperCase()
					.replaceAll("&NBSP;", "&nbsp;"));
		}

		documentacionHoldeadoNueva = guardar(documentacionHoldeado);
		logger.trace("Guardada documentacion de holdeado "
				+ documentacionHoldeadoNueva);
		return documentacionHoldeadoNueva;
	}

	public HoldeadoDao getHoldeadoDao() {
		return holdeadoDao;
	}

	public void setHoldeadoDao(HoldeadoDao holdeadoDao) {
		this.holdeadoDao = holdeadoDao;
	}

}

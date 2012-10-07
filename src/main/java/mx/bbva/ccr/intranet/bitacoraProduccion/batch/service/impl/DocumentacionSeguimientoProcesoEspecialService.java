package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionSeguimientoProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.SeguimientoProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionSeguimientoProcesoEspecialService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Origen;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;

import org.springframework.transaction.annotation.Transactional;

@Singleton
@Named("documentacionSeguimientoProcesoEspecialService")
public class DocumentacionSeguimientoProcesoEspecialService extends
		GenericoService<DocumentacionSeguimientoProcesoEspecial, Integer>
		implements IDocumentacionSeguimientoProcesoEspecialService {

	private SeguimientoProcesoEspecialDao seguimientoProcesoEspecialDao;

	public void setDocumentacionSeguimientoProcesoEspecialDao(
			DocumentacionSeguimientoProcesoEspecialDao documentacionSeguimientoProcesoEspecialDao) {
		setEntidadDao(documentacionSeguimientoProcesoEspecialDao);
	}

	@Transactional
	@Override
	public DocumentacionSeguimientoProcesoEspecial documentarSeguimientoProcesoEspecial(
			SeguimientoProcesoEspecial seguimientoProcesoEspecial,
			DocumentacionSeguimientoProcesoEspecial documentacionSeguimientoProcesoEspecial) {

		DocumentacionSeguimientoProcesoEspecial documentacionSeguimientoProcesoEspecialNueva = null;

		documentacionSeguimientoProcesoEspecial
				.setCdEmpresa(seguimientoProcesoEspecial.getId().getCdEmpresa());
		documentacionSeguimientoProcesoEspecial
				.setCdOrigen(seguimientoProcesoEspecial.getId().getCdOrigen());
		documentacionSeguimientoProcesoEspecial
				.setNbGrupo(seguimientoProcesoEspecial.getId().getNbGrupo());
		documentacionSeguimientoProcesoEspecial
				.setNbProceso(seguimientoProcesoEspecial.getId().getNbProceso());
		documentacionSeguimientoProcesoEspecial
				.setFhCiclo(seguimientoProcesoEspecial.getId().getFhCiclo());
		documentacionSeguimientoProcesoEspecial
				.setTmInicio(seguimientoProcesoEspecial.getId().getTmInicio());

		documentacionSeguimientoProcesoEspecial.setTmDocumentacion(new Date());
		if (seguimientoProcesoEspecial.getCdOrigen().equals(Origen.HOST)) {
			documentacionSeguimientoProcesoEspecial
					.setTxDocumentacion(documentacionSeguimientoProcesoEspecial
							.getTxDocumentacion().toUpperCase()
							.replaceAll("&NBSP;", "&nbsp;"));
		}
		documentacionSeguimientoProcesoEspecialNueva = guardar(documentacionSeguimientoProcesoEspecial);
		actualizarGenerico(seguimientoProcesoEspecial);
		logger.trace("Guardada documentacion de seguimientoProcesoEspecial "
				+ documentacionSeguimientoProcesoEspecialNueva);
		return documentacionSeguimientoProcesoEspecialNueva;

	}

	@Transactional
	@Override
	public List<SeguimientoProcesoEspecial> getSeguimientoProcesosEspeciales(
			Bitacora bitacora, Date fecha, Boolean usarODate,
			Integer tipoProcesoEspecial) {
		return seguimientoProcesoEspecialDao.getSeguimientosProcesoEspecial(
				bitacora, fecha, usarODate, tipoProcesoEspecial);
	}

	public SeguimientoProcesoEspecialDao getSeguimientoProcesoEspecialDao() {
		return seguimientoProcesoEspecialDao;
	}

	public void setSeguimientoProcesoEspecialDao(
			SeguimientoProcesoEspecialDao seguimientoProcesoEspecialDao) {
		this.seguimientoProcesoEspecialDao = seguimientoProcesoEspecialDao;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionProcesoRetrasadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.ProcesoRetrasadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionProcesoRetrasadoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionProcesoRetrasado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.RetrasadoDocumentacion;

import org.springframework.transaction.annotation.Transactional;

@Singleton
@Named("documentacionProcesoRetrasadoService")
public class DocumentacionProcesoRetrasadoService extends
		GenericoService<DocumentacionProcesoRetrasado, Integer> implements
		IDocumentacionProcesoRetrasadoService {

	private ProcesoRetrasadoDao procesoRetrasadoDao;

	public void setDocumentacionProcesoRetrasadoDao(
			DocumentacionProcesoRetrasadoDao documentacionProcesoRetrasadoDao) {
		setEntidadDao(documentacionProcesoRetrasadoDao);
	}

	@Transactional
	@Override
	public List<ProcesoRetrasado> getProcesoRetrasadosVigentes(Bitacora bitacora) {
		return getProcesoRetrasadoDao().getProcesosRetrasadosVigentes(bitacora);
	}

	@Transactional
	@Override
	public DocumentacionProcesoRetrasado documentarProcesoRetrasado(
			List<Integer> idProcesosRetrasados,
			DocumentacionProcesoRetrasado documentacionProcesoRetrasado,
			Boolean cerrarDocumentacionProcesoRetrasado) {

		DocumentacionProcesoRetrasado documentacionProcesoRetrasadoNueva = null;
		RetrasadoDocumentacion retrasadoDocumentacion = null;
		List<ProcesoRetrasado> procesosRetrasados = null;
		Date tiempoActual;

		tiempoActual = new Date();

		documentacionProcesoRetrasado.setTmDocumentacion(tiempoActual);
		documentacionProcesoRetrasadoNueva = guardar(documentacionProcesoRetrasado);

		procesosRetrasados = traerPorIds(ProcesoRetrasado.class,
				idProcesosRetrasados);

		for (ProcesoRetrasado procesoRetrasado : procesosRetrasados) {

			procesoRetrasado
					.setTmCerrado((cerrarDocumentacionProcesoRetrasado != null && cerrarDocumentacionProcesoRetrasado) ? tiempoActual
							: null);
			procesoRetrasado = actualizarGenerico(procesoRetrasado);

			logger.trace("Guardada documentacion de procesoRetrasado "
					+ documentacionProcesoRetrasadoNueva);

			retrasadoDocumentacion = new RetrasadoDocumentacion(
					documentacionProcesoRetrasadoNueva.getCdDocPr(),
					procesoRetrasado.getCdProRet());
			guardarGenerico(retrasadoDocumentacion);
		}

		return documentacionProcesoRetrasadoNueva;
	}

	@Transactional
	@Override
	public List<ProcesoRetrasado> validaDocumentarProcesosRetrasados(
			List<Integer> idProcesosRetrasados) {
		List<ProcesoRetrasado> procesosRetrasadosYaDocumentados;
		Integer idProcesoRetrasadoDocumentado = null;

		Collections.sort(idProcesosRetrasados);
		procesosRetrasadosYaDocumentados = new ArrayList<ProcesoRetrasado>();
		for (ProcesoRetrasado procesoRetrasado : traerPorIds(
				ProcesoRetrasado.class, idProcesosRetrasados)) {
			if (procesoRetrasado.getDocumentacionProcesoRetrasados() != null
					&& procesoRetrasado.getDocumentacionProcesoRetrasados()
							.size() > 0) {
				if (idProcesoRetrasadoDocumentado == null) {
					idProcesoRetrasadoDocumentado = procesoRetrasado
							.getUltimaDocumentacionProcesoRetrasado()
							.getPrimerProcesoRetrasado().getCdProRet();
				}
				if (!procesoRetrasado.getUltimaDocumentacionProcesoRetrasado()
						.getPrimerProcesoRetrasado().getCdProRet()
						.equals(idProcesoRetrasadoDocumentado)) {
					procesosRetrasadosYaDocumentados.add(procesoRetrasado);
				}
			} else {
				if (idProcesoRetrasadoDocumentado != null
						&& !idProcesoRetrasadoDocumentado.equals(0)) {
					procesosRetrasadosYaDocumentados.add(procesoRetrasado);
				} else {
					idProcesoRetrasadoDocumentado = 0;
				}
			}
		}
		return procesosRetrasadosYaDocumentados;
	}

	public ProcesoRetrasadoDao getProcesoRetrasadoDao() {
		return procesoRetrasadoDao;
	}

	public void setProcesoRetrasadoDao(ProcesoRetrasadoDao procesoRetrasadoDao) {
		this.procesoRetrasadoDao = procesoRetrasadoDao;
	}

}

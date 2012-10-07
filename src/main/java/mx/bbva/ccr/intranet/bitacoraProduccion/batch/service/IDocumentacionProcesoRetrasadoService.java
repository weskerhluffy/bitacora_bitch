package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionProcesoRetrasado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;

public interface IDocumentacionProcesoRetrasadoService extends
		IGenericoService<DocumentacionProcesoRetrasado, Integer> {

	/**
	 * Delega a
	 * {@link ProcesoRetrasadoDao#getProcesosRetrasadosVigentes(Bitacora))}.
	 * 
	 * @return Lista de procesos especiales vigentes
	 */
	public List<ProcesoRetrasado> getProcesoRetrasadosVigentes(Bitacora bitacora);

	/**
	 * Se graba la documentación <code>documentacionProcesoRetrasado</code>
	 * asociandola a los procesos retrasados identificados por
	 * <code>idProcesosRetrasados</code>. Dependiendo de la bandera
	 * <code>cerrarDocumentacionProcesoRetrasado</code>, marca los procesos
	 * retrasados como cerrados.
	 * 
	 * Efectos secundarios: modifica las referencias de los parametros para
	 * apuntar a instancias ya persistidas.
	 * 
	 * @param idsProcesosRetrasados
	 * @param documentacionProcesoRetrasado
	 * @param cerrarDocumentacionProcesoRetrasado
	 * @return El objeto <code>documentacionProcesoRetrasado</code> persistido.
	 */
	public DocumentacionProcesoRetrasado documentarProcesoRetrasado(
			List<Integer> idProcesosRetrasados,
			DocumentacionProcesoRetrasado documentacionProcesoRetrasado,
			Boolean cerrarDocumentacionProcesoRetrasado);

	/**
	 * Valida que los procesos retrasados identificados por
	 * <code>idProcesosRetrasados</code> no esten documentados o todos esten
	 * documentados juntos.
	 * 
	 * @param idProcesosRetrasados
	 * @return La lista de los procesos retrasados que ya fueron documentados.
	 */
	public List<ProcesoRetrasado> validaDocumentarProcesosRetrasados(
			List<Integer> idProcesosRetrasados);

}

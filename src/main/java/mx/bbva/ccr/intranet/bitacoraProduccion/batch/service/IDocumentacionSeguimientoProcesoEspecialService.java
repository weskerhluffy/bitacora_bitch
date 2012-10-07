package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.Date;
import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.SeguimientoProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;

public interface IDocumentacionSeguimientoProcesoEspecialService extends
		IGenericoService<DocumentacionSeguimientoProcesoEspecial, Integer> {

	/**
	 * Delega a
	 * {@link SeguimientoProcesoEspecialDao#getSeguimientosProcesoEspecial(Bitacora, Date, Boolean,Integer)}
	 * 
	 * @return Lista de procesos especiales
	 */
	public List<SeguimientoProcesoEspecial> getSeguimientoProcesosEspeciales(
			Bitacora bitacora, Date fecha, Boolean usarODate,
			Integer tipoProcesoEspecial);

	/**
	 * Se graba la documentación
	 * <code>documentacionSeguimientoProcesoEspecial</code> asociandola a
	 * <code>seguimientoProcesoEspecial</code>.
	 * 
	 * Cuando se trata de un proceso de host, se pasa la documentación a
	 * mayúsculas.
	 * 
	 * @param procesoEspecial
	 * @param documentacionSeguimientoProcesoEspecial
	 * @return El objeto <code>documentacionSeguimientoProcesoEspecial</code>
	 *         persistido.
	 */
	public DocumentacionSeguimientoProcesoEspecial documentarSeguimientoProcesoEspecial(
			SeguimientoProcesoEspecial seguimientoProcesoEspecial,
			DocumentacionSeguimientoProcesoEspecial documentacionSeguimientoProcesoEspecial);

}

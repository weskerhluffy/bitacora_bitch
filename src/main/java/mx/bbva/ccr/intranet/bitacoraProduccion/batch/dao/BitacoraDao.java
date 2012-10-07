package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;

/**
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 */
public interface BitacoraDao extends GenericDao<Bitacora, Integer> {

	/**
	 * 
	 * @param idEmpresa
	 * @param numBitacoras
	 * @return Las últimas <code>numBitacoras</code> bitacoras de acuerdo a su
	 *         fecha de ciclo de la empresa identificada por
	 *         <code>idEmpresa</code>
	 */
	public List<Bitacora> getUltimasBitacoras(String idEmpresa,
			Integer numBitacoras);

	/**
	 * Encuentra los cancelados que fueron enviados a unicenter y que su
	 * documentación pertenezca a la bitácora <code>bitacora</code> ademas de
	 * los que pertenezcan a empresa de la bitácora pero que aún no han sido
	 * enviados a unicenter
	 * 
	 * @param bitacora
	 * @return Lista de cancelados segun se define en la descripción
	 */
	public List<Cancelado> getCanceladosAsignados(Bitacora bitacora);

	/**
	 * Encuentra los cancelados que estan en estado {@link Estado#REGISTRADO}
	 * 
	 * @param bitacora
	 * @return Lista de cancelados segun se define en la descripción
	 */
	public List<Cancelado> getCanceladosNoAsignados(Bitacora bitacora);
}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;
import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;

public interface IBitacoraService extends IGenericoService<Bitacora, Integer> {
	public static final int DESDE_FU = 0;
	public static final int PARA_FU = 1;
	public static final int DESDE_CCR = 2;
	public static final int PARA_CCR = 3;
	public static final int GSI = 4;
	public static final int EN_VALIDACION = 5;
	public static final int PRODUCCION = 6;

	/**
	 * 
	 * @param idEmpresa
	 * @return Las últimas 3 bitácoras existentes para la empresa señalada
	 */
	public List<Bitacora> traerUltimasBitacoras(String idEmpresa);

	/**
	 * 
	 * @param bitacora
	 * @return La lista de cancelados pendientes y cerrados asociados a la
	 *         bitácora <code>bitacora</code>
	 */
	public List<Cancelado> getCanceladosActuales(Bitacora bitacora);

	/**
	 * Regresa los cancelados <code>cancelados</code> clasificados de la
	 * siguiente manera:
	 * <ul>
	 * <li>DESDE_FU: Cancelaciones que tienen acciones de recuperación de FU
	 * pendientes de ejecutar.
	 * <li>PARA_FU: Cancelaciones que requieren documentación de acciones de
	 * recuperación de FU.
	 * <li>DESDE_CCR: Cancelaciones que tienen documentación de acciones de
	 * recuperación de algún área de CCR pendientes de ejecutar.
	 * <li>PARA_CCR: Cancelaciones que requieren documentación de acciones de
	 * recuperación de algún área de CCR.
	 * <li>GSI: Cancelaciones ya enviadas a unicenter.
	 * <li>EN_VALIDACIÓN: Cancelaciones con instrucciones ya atendidas y en
	 * validación.
	 * <li>PRODUCCION: Cancelaciones con instrucciones enviadas por producción
	 * </ul>
	 * 
	 * @param cancelados
	 * @return
	 */
	public Map<Integer, List<Cancelado>> clasificaCancelados(
			List<Cancelado> cancelados);

	/**
	 * 
	 * @param bitacora
	 * @return La lista de cancelados de la empresa de la bitácora
	 *         <code>bitacora</code> que no han sido asignados, es decir, no han
	 *         entrado al proceso de documentación
	 */
	public List<Cancelado> getCanceladosSinAsignacion(Bitacora bitacora);

	/**
	 * Efectos secundarios: Refresca el objeto <code>bitacora</code> respecto a
	 * la BD.
	 * 
	 * @param bitacora
	 * @return Si la bitácora <code>bitacora</code> ya fue delegada.
	 */
	public Boolean validaBitacoraDelegada(Bitacora bitacora);

	/**
	 * Se marca como delegada(cerrada) la bitácora <code>bitacora</code>,
	 * actualizandola con
	 * <ul>
	 * <li>Tiempo de delegación.
	 * <li>Usuario que recibe.
	 * <li>Usuario que autoriza.
	 * </ul>
	 * 
	 * 
	 * También genera un nuevo registro de bitácora con los siguientes datos:
	 * <ul>
	 * <li>El turno siguiente al asociado a <code>bitacora</code>.
	 * <li>El tiempo en que se abre la bitácora.
	 * <li>El usuario que delegará, el que recibe en <code>bitacora</code>.
	 * <li>El ciclo productivo, tomando en cuenta que al delegar un turno
	 * nocturno, se le avanza un día.
	 * <li>La empresa asociada a <code>bitacora</code>.
	 * </ul>
	 * 
	 * @param bitacora
	 * @param ldapAutoriza
	 *            Usuario de quien autoriza
	 * @param ldapRecibe
	 *            Usuario de quien recibe la delegación
	 * @return El nuevo registro de bitácora, la nueva bitácora vigente.
	 */
	public Bitacora delegaBitacora(Bitacora bitacora, String ldapAutoriza,
			String ldapRecibe);

	/**
	 * Asigna los datos asociados de <code>bitacora</code> a la bitácora
	 * anterior. Borra <code>bitacora</code>.
	 * 
	 * @param bitacora
	 * @param ldapAutoriza
	 *            Usuario de quien autoriza
	 * @return La bitácora anterior a <code>bitacora</code>.
	 */
	public Bitacora retornaBitacora(Bitacora bitacora, String ldapAutoriza);
}

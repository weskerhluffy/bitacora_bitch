package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.util.Date;
import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecialId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial;

public interface SeguimientoProcesoEspecialDao extends
		GenericDao<SeguimientoProcesoEspecial, SeguimientoProcesoEspecialId> {
	/**
	 * Buscar los seguimientos de proceso especial de la empresa asociada a
	 * <code>Bitacora</code>, del tipo <code>tipoProcesoEspecial</code> y,
	 * dependiendo de la bandera <code>usarODate</code> , de la fecha
	 * <code>fecha</code>.
	 * 
	 * 
	 * @param bitacora
	 * @param fecha
	 * @param usarODate
	 *            Puede tomar los siguientes valores:
	 *            <ul>
	 *            <li> <code>true</code> Busca los registros con ODate igual a
	 *            <code>fecha</code>.
	 *            <li> <code>false</code> Busca los registros cuyo día del tiempo
	 *            de inicio sea <code>fecha</code>.
	 *            </ul>
	 * @param tipoProcesoEspecial
	 *            Puede ser cualquiera de los siguientes:
	 *            <ul>
	 *            <li> {@link TipoProcesoEspecial#HITO}
	 *            <li> {@link TipoProcesoEspecial#CHECKLIST}
	 *            <li> {@link TipoProcesoEspecial#BR}
	 *            </ul>
	 * @return
	 */
	public List<SeguimientoProcesoEspecial> getSeguimientosProcesoEspecial(
			Bitacora bitacora, Date fecha, Boolean usarODate,
			Integer tipoProcesoEspecial);

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabilId;

public interface IDiaInhabilService extends IGenericoService<DiaInhabil, DiaInhabilId> {
	/**
	 * Agrega <code>fhInhabil</code> y <code>cdEmpresa</code> 
	 * 
	 * Verifica que no haya fechas repetidas o que no existan
	 * 
	 * @param empSel
	 * @param fechasOriginales
	 * @param fechasSel
	 */
	public void agregarFechaInhabil(String empSel,List<String> fechasOriginales, List<String> fechasSel);

}

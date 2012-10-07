package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoEspecial;

public interface IProcesoEspecialService extends IGenericoService<ProcesoEspecial, Integer> {
	
	/**
	 * 
	 * @param procesoEspecial
	 * @return Verificar si el proceso especial ya esta registrado
	 */
	public Boolean validarProcesoEspecialExistente(ProcesoEspecial procesoEspecial);

}

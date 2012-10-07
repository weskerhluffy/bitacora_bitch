package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

public interface IEmpresaService extends IGenericoService<Empresa, String> {
	/**
	 * 
	 * @param empresa
	 * @return Verdadero si es que el codigo de la empresa <code>empresa</code>
	 *         esta siendo ocupado ya por otra empresa
	 */
	public Boolean validarCodigoEmpresaUnico(Empresa empresa);
}

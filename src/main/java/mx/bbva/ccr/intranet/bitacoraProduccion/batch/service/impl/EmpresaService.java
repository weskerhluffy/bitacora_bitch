package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.EmpresaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IEmpresaService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

@Singleton
@Named("empresaService")
public class EmpresaService extends GenericoService<Empresa, String> implements
		IEmpresaService {

	public void setEmpresaDao(EmpresaDao empresaDao) {
		setEntidadDao(empresaDao);
	}

	@Transactional
	@Override
	public Boolean validarCodigoEmpresaUnico(Empresa empresa) {
		Boolean yaRegistrado = false;
		Empresa empresaEjemplo = null;
		List<Empresa> empresasEncontradas;
		empresaEjemplo = new Empresa();
		empresaEjemplo.setCdEmpresa(empresa.getCdEmpresa());
		empresasEncontradas = encontrarPorInstanciaEjemplo(empresaEjemplo);
		logger.trace("Al buscar la empresa " + empresa.getCdEmpresa()
				+ " se encontraron " + empresasEncontradas);

		for (Empresa empresa2 : empresasEncontradas) {
			if (!empresa2.getNuEmpresa().equals(empresa.getNuEmpresa())) {
				yaRegistrado = true;
				break;
			}
		}

		return yaRegistrado;
	}
}

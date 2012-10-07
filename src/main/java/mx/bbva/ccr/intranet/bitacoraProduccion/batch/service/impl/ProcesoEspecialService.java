package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.ProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IProcesoEspecialService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoEspecial;

@Singleton
@Named("procesoEspecialService")
public class ProcesoEspecialService extends
		GenericoService<ProcesoEspecial, Integer> implements
		IProcesoEspecialService {

	public void setProcesoEspecialDao(ProcesoEspecialDao procesoEspecialDao) {
		setEntidadDao(procesoEspecialDao);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean validarProcesoEspecialExistente(
			ProcesoEspecial procesoEspecial) {
		Boolean yaRegistrado = false;
		ProcesoEspecial procesoEspecialEjemplo = null;
		List<ProcesoEspecial> procesosEspecialesEncontrados;

		procesoEspecialEjemplo = new ProcesoEspecial();
		procesoEspecialEjemplo.setNbProceso(procesoEspecial.getNbProceso());
		procesoEspecialEjemplo.setCdOrigen(procesoEspecial.getCdOrigen());
		procesoEspecialEjemplo.setCdEmpresa(procesoEspecial.getCdEmpresa());
		procesoEspecialEjemplo.setNbGrupo(procesoEspecial.getNbGrupo());
		procesosEspecialesEncontrados = encontrarPorInstanciaEjemplo(procesoEspecialEjemplo);
		logger.trace("Al buscar el proceso especial con nombre "
				+ procesoEspecial.getNbProceso() + ", grupo "
				+ procesoEspecial.getNbGrupo() + ", empresa "
				+ procesoEspecial.getCdEmpresa() + " y origen "
				+ procesoEspecial.getCdOrigen()
				+ "  se encontraron " + procesosEspecialesEncontrados);
		for (ProcesoEspecial procesoEspecial2 : procesosEspecialesEncontrados) {
			if (procesoEspecial2.getCdEmpresa().equals(
					procesoEspecial.getCdEmpresa())
					&& procesoEspecial2.getCdOrigen().equals(
							procesoEspecial.getCdOrigen())
					&& procesoEspecial2.getNbProceso().equals(
							procesoEspecial.getNbProceso())
					&& procesoEspecial2.getNbGrupo().equals(
							procesoEspecial.getNbGrupo())&&!procesoEspecial2.getCdProEsp().equals(procesoEspecial.getCdProEsp())) {
				yaRegistrado = true;
				break;
			}
		}
		return yaRegistrado;
	}
}

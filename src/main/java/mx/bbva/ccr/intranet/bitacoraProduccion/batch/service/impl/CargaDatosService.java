package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.CargaDatosDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.ICargaDatosService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatos;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatosId;

@Singleton
@Named("cargaDatosService")
public class CargaDatosService extends
		GenericoService<CargaDatos, CargaDatosId> implements ICargaDatosService {
	public void setCargaDatosDao(CargaDatosDao cargaDatosDao) {
		setEntidadDao(cargaDatosDao);
	}

	@Transactional
	@Override
	public List<CargaDatos> getStatusUltimasCargas(Bitacora bitacora) {
		return getCargaDatosDao().getStatusUltimasCargas(bitacora, 50);
	}

	public CargaDatosDao getCargaDatosDao() {
		return (CargaDatosDao) entidadDao;
	}
}

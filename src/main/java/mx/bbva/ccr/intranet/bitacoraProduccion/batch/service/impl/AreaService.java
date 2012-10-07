package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.AreaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IAreaService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;

@Singleton
@Named("areaService")
public class AreaService extends GenericoService<Area, Integer> implements
		IAreaService {
	public void setAreaDao(AreaDao areaDao) {
		setEntidadDao(areaDao);
	}
}

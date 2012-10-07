package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.AreaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;

@Singleton
@Named("areaDao")
public class AreaHibernateDao extends GenericHibernateDAO<Area, Integer>
		implements AreaDao {

}

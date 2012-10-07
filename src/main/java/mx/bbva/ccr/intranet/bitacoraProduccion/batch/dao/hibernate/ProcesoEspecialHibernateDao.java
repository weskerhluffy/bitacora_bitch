package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.ProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoEspecial;

@Singleton
@Named("procesoEspecialDao")
public class ProcesoEspecialHibernateDao extends GenericHibernateDAO<ProcesoEspecial, Integer> implements ProcesoEspecialDao {

}

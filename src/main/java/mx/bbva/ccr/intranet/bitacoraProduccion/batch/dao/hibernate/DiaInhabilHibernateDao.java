package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DiaInhabilDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabilId;

@Singleton
@Named("diaInhabilDao")
public class DiaInhabilHibernateDao extends GenericHibernateDAO<DiaInhabil, DiaInhabilId>
		implements DiaInhabilDao {


}

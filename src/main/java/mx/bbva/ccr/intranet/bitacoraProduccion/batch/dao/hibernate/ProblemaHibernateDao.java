package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.ProblemaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;

@Singleton
@Named("problemaDao")
public class ProblemaHibernateDao extends
		GenericHibernateDAO<Problema, Integer> implements ProblemaDao {

	@Override
	public Long cuentaCanceladosAsociados(Problema problema) {
		return (Long) getHibernateTemplate().findByNamedQueryAndNamedParam(
				"cuentaCancelacionesAsociadasProblema", "cdProblema",
				problema.getCdProblema()).get(0);
	}


}

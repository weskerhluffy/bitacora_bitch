package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionProcesoRetrasadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionProcesoRetrasado;

@Singleton
@Named("documentacionProcesoRetrasadoDao")
public class DocumentacionProcesoRetrasadoHibernateDao extends
		GenericHibernateDAO<DocumentacionProcesoRetrasado, Integer> implements DocumentacionProcesoRetrasadoDao{


}

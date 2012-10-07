package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionHoldeadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;

@Singleton
@Named("documentacionHoldeadoDao")
public class DocumentacionHoldeadoHibernateDao extends
		GenericHibernateDAO<DocumentacionHoldeado, Integer> implements DocumentacionHoldeadoDao {


}

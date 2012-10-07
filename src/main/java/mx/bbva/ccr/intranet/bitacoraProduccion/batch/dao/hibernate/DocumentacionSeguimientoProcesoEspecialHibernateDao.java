package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionSeguimientoProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial;

@Singleton
@Named("documentacionSeguimientoProcesoEspecialDao")
public class DocumentacionSeguimientoProcesoEspecialHibernateDao extends
		GenericHibernateDAO<DocumentacionSeguimientoProcesoEspecial, Integer> implements
		DocumentacionSeguimientoProcesoEspecialDao {
	
}

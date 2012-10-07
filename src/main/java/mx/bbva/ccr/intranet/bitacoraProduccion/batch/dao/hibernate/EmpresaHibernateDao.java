package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.Query;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.EmpresaDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

@Singleton
@Named("empresaDao")
public class EmpresaHibernateDao extends GenericHibernateDAO<Empresa, String>
		implements EmpresaDao {

	@Override
	public Empresa update(Empresa entity) {
		logger.trace("Actualizando empresa " + entity);
		Empresa empresa;
		Query query;
		super.update(entity);
		query = getSession().getNamedQuery("actualizarEmpresa");
		query.setString("cdEmpresa", entity.getCdEmpresa());
		query.setString("cdEmpresaNuevo", entity.getCdEmpresaMutable());
		query.executeUpdate();
		empresa = findById(entity.getCdEmpresaMutable());
		getSession().flush();
		logger.trace("Empresa actualizada " + entity);
		return empresa;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.ProcesoRetrasadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;

import org.hibernate.Query;

@Singleton
@Named("procesoRetrasadoDao")
public class ProcesoRetrasadoHibernateDao extends
		GenericHibernateDAO<ProcesoRetrasado, Integer> implements
		ProcesoRetrasadoDao {

	@SuppressWarnings("unchecked")
	// Por el cast de List a List<ProcesoRetrasado>
	@Override
	public List<ProcesoRetrasado> getProcesosRetrasadosVigentes(
			Bitacora bitacora) {
		List<ProcesoRetrasado> procesoRetrasados = null;
		Query query;
		query = getSession().getNamedQuery("retrasadosDocumentados");
		query.setInteger("cdBitacora", bitacora.getCdBitacora());
		query.setString("cdEmpresa", bitacora.getCdEmpresa());
		query.setCacheable(true);
		query.setCacheRegion("query.retrasadosDocumentados");
		procesoRetrasados = query.list();

		query = getSession().getNamedQuery("retrasadosNoDocumentados")
				.setString("cdEmpresa", bitacora.getCdEmpresa());
		procesoRetrasados.addAll(query.list());
		return procesoRetrasados;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.Collections;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.Query;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.CargaDatosDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatos;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatosId;

@Singleton
@Named("cargaDatosDao")
public class CargaDatosHibernateDao extends
		GenericHibernateDAO<CargaDatos, CargaDatosId> implements CargaDatosDao {

	@SuppressWarnings("unchecked")
	// Por el cast de List a List<CargaDatos>
	@Override
	public List<CargaDatos> getStatusUltimasCargas(Bitacora bitacora,
			Integer numUltimas) {
		List<CargaDatos> cargaDatos = null;
		Query query;
		query = getSession().getNamedQuery("ultimasCargas");
		query.setString("cdEmpresa", bitacora.getCdEmpresa());
		query.setMaxResults(numUltimas);

		cargaDatos = query.list();

		Collections.reverse(cargaDatos);
		return cargaDatos;
	}

}

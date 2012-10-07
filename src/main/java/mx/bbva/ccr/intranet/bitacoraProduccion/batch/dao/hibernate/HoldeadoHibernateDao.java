package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.Query;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.HoldeadoDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.HoldeadoId;

@Singleton
@Named("holdeadoDao")
public class HoldeadoHibernateDao extends
		GenericHibernateDAO<Holdeado, HoldeadoId> implements HoldeadoDao {

	@SuppressWarnings("unchecked")
	// Por el cast de lista a lista de holdeados
	@Override
	public List<Holdeado> getHoldeadosVigentes(Bitacora bitacora) {
		List<Holdeado> holdeados = null;
		Query query;
		query = getSession().getNamedQuery("holdeadosVigentesBitacora");
		query.setCacheable(true);
		query.setCacheRegion("query.holdeadosVigentesBitacora");
		query.setInteger("cdBitacora", bitacora.getCdBitacora());
		holdeados = query.list();
		query = getSession().getNamedQuery("holdeadosVigentesSinDocumentar")
				.setString("cdEmpresa", bitacora.getCdEmpresa());
		holdeados.addAll(query.list());
		return holdeados;
	}

}

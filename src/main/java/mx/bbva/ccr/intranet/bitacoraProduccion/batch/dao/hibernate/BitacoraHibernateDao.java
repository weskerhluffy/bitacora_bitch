package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.Collections;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.BitacoraDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.hibernate.Query;

@Singleton
@Named("bitacoraDao")
public class BitacoraHibernateDao extends
		GenericHibernateDAO<Bitacora, Integer> implements BitacoraDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Bitacora> getUltimasBitacoras(String idEmpresa,
			Integer numBitacoras) {
		List<Bitacora> bitacoras = null;
		Empresa empresa;
		empresa = findByIdGenerico(Empresa.class, idEmpresa);
		Query query = getSession().getNamedQuery("ultimasBitacoras");
		query.setString("cdEmpresa", empresa.getCdEmpresa());
		query.setMaxResults(numBitacoras);
		query.setCacheable(true);
		bitacoras = query.list();
		Collections.reverse(bitacoras);
		return bitacoras;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cancelado> getCanceladosAsignados(Bitacora bitacora) {
		List<Cancelado> cancelados = null;
		Query query = null;

		query = getSession().getNamedQuery("canceladosPendientesBitacora");
		query.setInteger("cdBitacora", bitacora.getCdBitacora());
		query.setCacheable(true);
		query.setCacheRegion("query.canceladosPendientes");
		cancelados = query.list();

		query = getSession().getNamedQuery(
				"canceladosPendientesEmpresaAsignados");
		query.setString("cdEmpresa", bitacora.getCdEmpresa());
		query.setCacheable(true);
		query.setCacheRegion("query.canceladosPendientes");
		cancelados.addAll(query.list());

		return cancelados;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cancelado> getCanceladosNoAsignados(Bitacora bitacora) {
		List<Cancelado> cancelados = null;
		Query query = null;

		query = getSession().getNamedQuery(
				"canceladosPendientesEmpresaNoAsignados");
		query.setString("cdEmpresa", bitacora.getCdEmpresa());
		query.setCacheable(true);
		query.setCacheRegion("query.canceladosPendientesEmpresaNoAsignados");
		cancelados = query.list();

		return cancelados;
	}

}

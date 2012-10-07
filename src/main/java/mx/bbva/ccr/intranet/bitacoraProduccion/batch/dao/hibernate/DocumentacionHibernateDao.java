package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;

import org.hibernate.Query;

@Singleton
@Named("documentacionDao")
public class DocumentacionHibernateDao extends
		GenericHibernateDAO<Documentacion, Integer> implements DocumentacionDao {

	@SuppressWarnings("unchecked")
	// Por el cast de List a List<Cancelado>
	@Override
	public void sincronizarDocumentacionAdelantada() {
		Query query;
		Documentacion documentacion;
		Cancelado canceladoPrincipal;
		List<Cancelado> cancelados;
		List<Cancelado> canceladosAsociadosADocumentacion;

		query = getSession().getNamedQuery("canceladosDocumentacionAdelantada");
		cancelados = query.list();

		for (Cancelado cancelado : cancelados) {
			documentacion = cancelado.getUltimaDocumentacion();
			canceladoPrincipal = documentacion.getCancelados().get(0);
			canceladosAsociadosADocumentacion = documentacion.getCancelados();
			for (Cancelado canceladoAsociadoADocumentacion : canceladosAsociadosADocumentacion) {
				if (canceladoAsociadoADocumentacion.getId().equals(
						canceladoPrincipal.getId())) {
					canceladoAsociadoADocumentacion.setCdEmpresaPri(null);
					canceladoAsociadoADocumentacion.setNbProcesoPri(null);
					canceladoAsociadoADocumentacion.setFhCicloPri(null);
					canceladoAsociadoADocumentacion.setCdPasoPri(null);
					canceladoAsociadoADocumentacion.setNbGrupoPri(null);
					canceladoAsociadoADocumentacion.setCdOrigenPri(null);
				} else {
					canceladoAsociadoADocumentacion
							.setCdEmpresaPri(canceladoPrincipal.getCdEmpresa());
					canceladoAsociadoADocumentacion
							.setNbProcesoPri(canceladoPrincipal.getNbProceso());
					canceladoAsociadoADocumentacion
							.setFhCicloPri(canceladoPrincipal.getFhCiclo());
					canceladoAsociadoADocumentacion
							.setCdPasoPri(canceladoPrincipal.getCdPaso());
					canceladoAsociadoADocumentacion
							.setNbGrupoPri(canceladoPrincipal.getNbGrupo());
					canceladoAsociadoADocumentacion
							.setCdOrigenPri(canceladoPrincipal.getCdOrigen());
				}
				canceladoAsociadoADocumentacion.setCdProblema(documentacion
						.getCdProblema());
				canceladoAsociadoADocumentacion.setCdEstadoCan(documentacion
						.getCdEstado());
				getHibernateTemplate()
						.getSessionFactory()
						.getCache()
						.evictEntity(Cancelado.class,
								canceladoAsociadoADocumentacion.getId());
				getHibernateTemplate()
						.getSessionFactory()
						.getCache()
						.evictCollection(
								Cancelado.class.getName() + ".documentacions",
								canceladoAsociadoADocumentacion.getId());
				getHibernateTemplate()
						.getSessionFactory()
						.getCache()
						.evictCollection(
								Cancelado.class.getName()
										+ ".canceladosSecundarios",
								canceladoAsociadoADocumentacion.getId());
			}
			getHibernateTemplate()
					.getSessionFactory()
					.getCache()
					.evictCollection(
							Documentacion.class.getName() + ".cancelados",
							documentacion.getId());
			getHibernateTemplate()
					.getSessionFactory()
					.getCache()
					.evictCollection(
							Documentacion.class.getName()
									+ ".canceladoPrincipal",
							documentacion.getId());
		}
		if (cancelados.size() > 0) {
			getHibernateTemplate().getSessionFactory().getCache()
					.evictQueryRegion("query.canceladosPendientes");
		}
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.util;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

/**
 * Este filtro pone el modo de flush a commit y realiza un flush explicito al
 * cerrar la sesión. Debe trabajar bien con operaciones dentro y fuera de una
 * transacción, las cuales utilizan en flushes externos.
 * 
 * The filter above sets the flush mode to commit and performs an explicit flush
 * upon the close session stage. It should work with an explicit transaction and
 * transaction less operations, which rely on outside flushes.
 * 
 * Fuente: http://wiki.apache.org/myfaces/Hibernate_And_MyFaces
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 */
public class FiltroOpenSessionInView extends OpenSessionInViewFilter {
	private Logger logger = Logger.getLogger(FiltroOpenSessionInView.class);

	/**
	 * we do a different flushmode than in the codebase here
	 */
	protected Session getSession(SessionFactory sessionFactory)
			throws DataAccessResourceFailureException {
		logger.trace("Abriendo la session ");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		logger.trace("session " + session + " abierta");
		session.setFlushMode(FlushMode.MANUAL);
		// Filter documentacionSintoma = session
		// .enableFilter("documentacionSintoma");
		// Filter documentacionRecuperacion = session
		// .enableFilter("documentacionRecuperacion");
		// Filter documentacionImpacto = session
		// .enableFilter("documentacionImpacto");
		// documentacionSintoma.setParameter("idTipoDocumentacion",
		// Documentacion.SINTOMA);
		// documentacionRecuperacion.setParameter("idTipoDocumentacion",
		// Documentacion.RECUPERACION);
		// documentacionImpacto.setParameter("idTipoDocumentacion",
		// Documentacion.IMPACTO);
		logger.trace("Filtros hibernate habilitados");
		return session;
	}

	protected void closeSession(Session session, SessionFactory factory) {
		logger.trace("Cerrando sesion " + session);
		super.closeSession(session, factory);
		logger.trace("Session cerrada");
	}

}

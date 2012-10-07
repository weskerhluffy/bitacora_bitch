package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.hibernate.Query;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.MensajeDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;

@Singleton
@Named("mensajeDao")
public class MensajeHibernateDao extends GenericHibernateDAO<Mensaje, Integer>
		implements MensajeDao {

	@SuppressWarnings("unchecked")
	// Por el cast de lista sin tipo a lista de mensajes
	@Override
	public List<Mensaje> getMensajesVigentes(Bitacora bitacora) {
		List<Mensaje> mensajes = null;
		Query query;
		query = getSession().getNamedQuery("mensajesVigentes");
		query.setInteger("cdBitacora", bitacora.getCdBitacora());
		query.setString("cdEmpresa", bitacora.getCdEmpresa());
		mensajes = query.list();
		return mensajes;
	}

}

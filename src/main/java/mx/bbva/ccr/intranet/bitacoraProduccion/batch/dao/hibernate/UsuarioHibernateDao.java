package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.UsuarioDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

@Singleton
@Named("usuarioDao")
public class UsuarioHibernateDao extends GenericHibernateDAO<Usuario, Integer>
		implements UsuarioDao {

}

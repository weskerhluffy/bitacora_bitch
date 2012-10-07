/**
 * 
 */
package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.GenericDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.IModelo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implementación en hibernate de <code>GenericDao</code> para que hereden los
 * daos del sistema
 * 
 * @author Ernesto Alvarado Gaspar
 * @param <MODELO>
 *            Clase persistente
 * @param <ID>
 *            Clase del id de la clase persistente
 */
public abstract class GenericHibernateDAO<MODELO extends IModelo<ID>, ID extends Serializable>
		extends HibernateDaoSupport implements GenericDao<MODELO, ID> {
	private Class<MODELO> persistentClass;

	private Logger loggerAbstractClass = Logger
			.getLogger(GenericHibernateDAO.class);
	protected Logger logger = Logger.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class<MODELO>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

	}

	public Class<MODELO> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public List<MODELO> findAll() {
		loggerAbstractClass.trace("Buscando todos los registros de "
				+ persistentClass.getSimpleName());
		try {
			return getHibernateTemplate().findByExample(
					persistentClass.newInstance());
		} catch (InstantiationException ie) {
			loggerAbstractClass.error("Error al instanciar la clase "
					+ persistentClass.getSimpleName());
			loggerAbstractClass.error(ExceptionUtils.getStackTrace(ie));
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			loggerAbstractClass.error("Error de accesso a la clase "
					+ persistentClass.getSimpleName());
			loggerAbstractClass.error(ExceptionUtils.getStackTrace(iae));
			iae.printStackTrace();
		} catch (DataAccessException dae) {
			dae.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<MODELO> findByExample(MODELO exampleInstance) {
		return getHibernateTemplate().findByExample(exampleInstance);
	}

	public boolean delete(MODELO entity) {
		entity = getHibernateTemplate().merge(entity);
		getHibernateTemplate().delete(entity);
		return true;
	}

	/**
	 * 
	 * @param criterion
	 * @return Los objetos encontrados según el objeto <code>criterion</code>.
	 */
	@SuppressWarnings("unchecked")
	public List<MODELO> findByCriteria(Criterion... criterion) {
		loggerAbstractClass.trace("Buscando por criterio");
		List<MODELO> lista = null;
		Criteria crit = getSession().createCriteria(getPersistentClass());
		loggerAbstractClass.trace("Se obtuvo el criterio " + crit.getAlias()
				+ " de la clase " + persistentClass.getSimpleName());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		try {
			lista = crit.list();
		} catch (HibernateException he) {
			he.printStackTrace();
		}

		loggerAbstractClass.trace("Se encontraron " + lista.size()
				+ " tuplas de " + persistentClass.getSimpleName());
		return lista;
	}

	/**
	 * Método de conveniencia para obtener todos los registros de una tabla sin
	 * tener que crear un heredero de este DAO.
	 * 
	 * @param claseModelo
	 * @return Todos los registros de la tabla que representa la clase
	 *         <code>claseModelo</code>
	 */
	public List<?> findAll(Class<?> claseModelo) {
		loggerAbstractClass.trace("Pidiendo todos los elementos de la clase "
				+ claseModelo);
		return getHibernateTemplate().loadAll(claseModelo);
	}

	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> boolean deleteGenerico(
			CLASE_PERSISTENTE entity) {
		entity = getHibernateTemplate().merge(entity);
		getHibernateTemplate().delete(entity);
		return true;
	}

	@SuppressWarnings("unchecked")
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE reasociar(
			CLASE_PERSISTENTE entidad) {
		loggerAbstractClass.trace("Haciendo persistente la entidad "
				+ entidad.getClass());
		loggerAbstractClass.trace("El tipo del id es "
				+ entidad.getId().getClass());
		if (!getSession().contains(entidad)) {
			return (CLASE_PERSISTENTE) getSession().get(entidad.getClass(),
					entidad.getId());
		} else {
			return entidad;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE findByIdGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente, CLASE_ID id) {
		CLASE_PERSISTENTE entidad = null;
		boolean esModelo = false;
		Type[] interfaces = clasePersistente.getInterfaces();
		if (!(id instanceof Serializable)) {
			loggerAbstractClass
					.error("La clase "
							+ id.getClass()
							+ " no implementa la interfaz serializable, no se puede utilizar como objeto id de hibernate");
			return null;
		}
		for (Type type : interfaces) {
			if (type.equals(IModelo.class)) {
				esModelo = true;
			}
		}
		if (!esModelo) {
			loggerAbstractClass
					.error("La clase "
							+ clasePersistente
							+ " no implementa la interfaz IModelo, no se puede instanciar");
			return null;
		}

		entidad = (CLASE_PERSISTENTE) getSession().load(clasePersistente,
				(Serializable) id);
		return entidad;
	}

	public Criteria crearCriterio() {
		return getSession().createCriteria(persistentClass);
	}

	@SuppressWarnings("unchecked")
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findByExampleGenerico(
			CLASE_PERSISTENTE exampleInstance) {
		loggerAbstractClass.trace("Buscando por instancia ejemplo la clase "
				+ exampleInstance.getClass());

		return getHibernateTemplate().findByExample(exampleInstance);
	}

	/**
	 * Método de conveniencia para encontrar por criterio usando cualquier clase
	 * persistente desde todos los daos que heredan de
	 * <code>GenericHibernateDao</code>
	 * 
	 * @param criterion
	 * @return Los objetos encontrados según el objeto <code>criterion</code>.
	 */
	@SuppressWarnings("unchecked")
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findByCriteriaGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente, Criterion... criterion) {
		loggerAbstractClass.trace("Buscando por criterio para la clase "
				+ clasePersistente);
		List<CLASE_PERSISTENTE> lista = null;
		DetachedCriteria crit = DetachedCriteria.forClass(clasePersistente);
		loggerAbstractClass.trace("Se obtuvo el criterio " + crit.getAlias()
				+ " de la clase " + clasePersistente.getSimpleName());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		try {
			lista = getHibernateTemplate().findByCriteria(crit);
		} catch (HibernateException he) {
			loggerAbstractClass
					.trace("Hubo un error al intentar encontrar por criterio:");
			loggerAbstractClass.trace(ExceptionUtils.getStackTrace(he));
			he.printStackTrace();
		}

		loggerAbstractClass.trace("Se encontraron " + lista.size()
				+ " tuplas de " + clasePersistente.getSimpleName());
		return lista;
	}

	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findAllGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente) {
		List<CLASE_PERSISTENTE> lista = null;
		lista = getHibernateTemplate().loadAll(clasePersistente);
		return lista;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findByIds(
			Class<CLASE_PERSISTENTE> clasePersistente, List<CLASE_ID> ids) {
		// Este método crea un query con varios "or" en vez de usar el operador
		// "in" por el performance del query
		ClassMetadata metadatos = null;
		String nombrePropiedadId = null;
		List<CLASE_PERSISTENTE> persistentes = null;
		loggerAbstractClass.trace("Se usan " + ids.size() + " ids");
		if (ids == null || ids.size() < 1) {
			return new ArrayList<CLASE_PERSISTENTE>();
		}
		metadatos = getSessionFactory().getClassMetadata(clasePersistente);
		nombrePropiedadId = metadatos.getIdentifierPropertyName();
		Criteria critero = getSession().createCriteria(clasePersistente);
		Disjunction disyuncion = Restrictions.disjunction();
		for (CLASE_ID id : ids) {
			if (id != null) {
				disyuncion.add(Restrictions.eq(nombrePropiedadId, id));
			} else {
				loggerAbstractClass.warn("Se ha encontrado un id nulo");
			}
		}
		critero.add(disyuncion);
		persistentes = critero.list();
		return persistentes;
	}

	@Override
	public MODELO save(MODELO entity) {
		loggerAbstractClass.trace("Guardando la entidad " + entity);
		getHibernateTemplate().save(entity);
		return entity;
	}

	@Override
	public MODELO update(MODELO entity) {
		logger.trace("Actualizando " + entity);
		entity = getHibernateTemplate().merge(entity);
		getHibernateTemplate().update(entity);
		return entity;
	}

	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE saveGenerico(
			CLASE_PERSISTENTE entity) {
		loggerAbstractClass.trace("Guardando la entidad " + entity);
		getHibernateTemplate().save(entity);
		return entity;
	}

	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE updateGenerico(
			CLASE_PERSISTENTE entity) {
		logger.trace("Actualizando " + entity);
		entity = getHibernateTemplate().merge(entity);
		getHibernateTemplate().update(entity);
		return entity;
	}

	@Override
	public MODELO findById(ID id) {
		return getHibernateTemplate().get(persistentClass, id);
	}

}

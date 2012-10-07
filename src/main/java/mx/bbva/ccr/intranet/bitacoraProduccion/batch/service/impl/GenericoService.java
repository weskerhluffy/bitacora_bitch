package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.io.Serializable;
import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.GenericDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.GenericHibernateDAO;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IGenericoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.IModelo;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa la interfaz <code>IGenericoService</code> usando para el acceso a
 * datos el objeto <code>entidadDao</code>
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 * @param <MODELO>
 *            Clase persistente
 * @param <ID>
 *            Clase del id de la clase persistente
 */
public abstract class GenericoService<MODELO extends IModelo<ID>, ID extends Serializable>
		implements IGenericoService<MODELO, ID> {
	GenericDao<MODELO, ID> entidadDao;
	private Logger loggerClaseAbstracta = Logger
			.getLogger(GenericoService.class);
	protected Logger logger = Logger.getLogger(this.getClass());

	public GenericoService() {
	}

	public GenericoService(GenericDao<MODELO, ID> entidadDao) {
		setEntidadDao(entidadDao);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean eliminar(MODELO entidad) {
		entidadDao.delete(entidad);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public MODELO guardar(MODELO entidad) {
		MODELO modelo = entidadDao.save(entidad);
		return modelo;
	}

	@Transactional
	@Override
	public MODELO traerPorId(ID id) {
		return entidadDao.findById(id);
	}

	@Transactional
	@Override
	public List<MODELO> traerTodos() {
		return entidadDao.findAll();
	}

	public GenericDao<MODELO, ID> getEntidadDao() {
		return entidadDao;
	}

	public void setEntidadDao(GenericDao<MODELO, ID> entidadDao) {
		this.entidadDao = entidadDao;
	}

	@Override
	@Transactional
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE traerPorIdGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente, CLASE_ID id) {
		return entidadDao.findByIdGenerico(clasePersistente, id);
	}

	/**
	 * Método de conveniencia que reasocia la entidad para poder utilizar la
	 * inicializacion perezosa de colecciones. Acopla fuertemente en cierta
	 * medida la capa de servicio con la de acceso a datos.
	 * 
	 * @param <CLASE_PERSISTENTE>
	 * @param <CLASE_ID>
	 * @param entidad
	 * @return
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE reAsociarModelo(
			CLASE_PERSISTENTE entidad) {
		loggerClaseAbstracta.trace("Reasociando objeto de tipo "
				+ entidad.getClass());
		entidad = ((GenericHibernateDAO<CLASE_PERSISTENTE, CLASE_ID>) entidadDao)
				.reasociar(entidad);
		return entidad;
	}

	@Transactional
	@Override
	public List<MODELO> encontrarPorInstanciaEjemplo(MODELO instanciaEjemplo) {
		loggerClaseAbstracta.trace("Encontrando por instancia ejemplo de tipo "
				+ instanciaEjemplo.getClass());
		return getEntidadDao().findByExample(instanciaEjemplo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE guardarGenerico(
			CLASE_PERSISTENTE entidad) {
		return entidadDao.saveGenerico(entidad);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> boolean eliminarGenerico(
			CLASE_PERSISTENTE entidad) {
		return entidadDao.deleteGenerico(entidad);
	}

	@Transactional
	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> traerTodosGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente) {
		return entidadDao.findAllGenerico(clasePersistente);
	}

	@Transactional
	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> traerPorIds(
			Class<CLASE_PERSISTENTE> clasePersistente, List<CLASE_ID> ids) {
		return entidadDao.findByIds(clasePersistente, ids);
	}

	@Transactional
	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> encontrarPorInstanciaEjemploGenerico(
			CLASE_PERSISTENTE instanciaEjemplo) {
		return getEntidadDao().findByExampleGenerico(instanciaEjemplo);
	}

	@Transactional
	@Override
	public MODELO actualizar(MODELO entidad) {
		return getEntidadDao().update(entidad);
	}

	@Transactional
	@Override
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE actualizarGenerico(
			CLASE_PERSISTENTE entidad) {
		return getEntidadDao().updateGenerico(entidad);
	}

}

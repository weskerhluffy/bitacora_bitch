package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao;

import java.io.Serializable;
import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.IModelo;

/**
 * Contiene varias operaciones comunes de acceso a datos tanto para la clase
 * parametrizada <code>T</code> como para cualquier clase persistente.
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 * @param <T>
 *            La clase persistente
 * @param <ID>
 *            La clase de la llave primaria
 */
public interface GenericDao<T, ID extends Serializable> {
	/**
	 * 
	 * @return La lista de todos los registros para la tabla correspondiente a
	 *         la clase <code>T</code>.
	 */
	public List<T> findAll();

	/**
	 * @param id
	 * @return La tupla identificada a traves de la llave primaria
	 *         <code>id</code>, aun si es compuesta.
	 */
	public T findById(ID id);

	/**
	 * Guarda el objeto <code>entity</code> en la bd.
	 * 
	 * @param entity
	 * @return El objeto persistido.
	 */
	public T save(T entity);

	/**
	 * Actualiza, el objeto <code>entity</code> en la bd.
	 * 
	 * @param entity
	 * @return El objeto persistido.
	 */
	public T update(T entity);

	/**
	 * Borra el objeto <code>entity</code> de la bd.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean delete(T entity);

	/**
	 * Encuentra todos los registros que tengan los atributos iguales a los de
	 * <code>exampleInstance</code>, tomando en cuenta solo los que no sean
	 * nulos.
	 * 
	 * @param exampleInstance
	 * @return La lista de registros con atributos iguales a los de
	 *         <code>exampleInstance</code>.
	 */
	public List<T> findByExample(T exampleInstance);

	/**
	 * Método de conveniencia que guarda en la bd la entidad persistente dada.
	 * 
	 * @param entity
	 * @return
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE saveGenerico(
			CLASE_PERSISTENTE entity);

	/**
	 * Método de conveniencia que actualiza en la bd la entidad persistente
	 * dada.
	 * 
	 * @param entity
	 * @return
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE updateGenerico(
			CLASE_PERSISTENTE entity);

	/**
	 * Elimina la entidad persistente dada de la bd.
	 * 
	 * @param entity
	 * @return
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> boolean deleteGenerico(
			CLASE_PERSISTENTE entity);

	/**
	 * Método de conveniencia para poder traer cualquier registro
	 * correspondiente a la entidad persistente a traves de su id sin importar
	 * los parámetros de creación del dao.
	 * 
	 * @param clasePersistente
	 * @param id
	 * @return
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE findByIdGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente, CLASE_ID id);

	/**
	 * Método de conveniencia que encuentra todos los registros que tengan los
	 * atributos iguales a los de <code>exampleInstance</code>, tomando en
	 * cuenta solo los que no sean nulos. Este método se puede utilizar con
	 * cualquier clase persistente desde cualquier dao que implemente la
	 * interfaz <code>GenericDao</code>
	 * 
	 * @param exampleInstance
	 * @return La lista de registros con atributos iguales a los de
	 *         <code>exampleInstance</code>.
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findByExampleGenerico(
			CLASE_PERSISTENTE exampleInstance);

	/**
	 * 
	 * @param <CLASE_PERSISTENTE>
	 * @param <CLASE_ID>
	 * @param clasePersistente
	 *            La clase persistente
	 * @return La lista de todos los registros de la tabla asociada a
	 *         <code>clasePersistente</code>
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findAllGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente);

	/**
	 * 
	 * @param <CLASE_PERSISTENTE>
	 * @param <CLASE_ID>
	 * @param clasePersistente
	 * @param ids
	 *            Lista de objetos que identifican a las tuplas que se desean
	 *            extraer
	 * @return La lista de todos los registros de la tabla asociada a
	 *         <code>clasePersistente</code> que esten identificadas por uno de
	 *         los elementos de la lista <code>ids</code>
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> findByIds(
			Class<CLASE_PERSISTENTE> clasePersistente, List<CLASE_ID> ids);

}

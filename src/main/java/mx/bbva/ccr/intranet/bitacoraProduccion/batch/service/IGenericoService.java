package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.io.Serializable;
import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.IModelo;

/**
 * Interfaz que define varios métodos comunes de acceso a datos para la clase
 * parametrizada <code>MODELO</code> y en general para cualquier clase
 * persistente
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 * @param <MODELO>
 *            Clase persistente
 * @param <ID>
 *            Clase del id de la clase persistente
 */
public interface IGenericoService<MODELO, ID extends Serializable> {
	/**
	 * 
	 * @return MODELOodos los registros del catálogo.
	 */
	public List<MODELO> traerTodos();

	/**
	 * 
	 * @param id
	 * @return El registro indentificado por la llave primaria <code>id</code>
	 */
	public MODELO traerPorId(ID id);

	/**
	 * Inserta un nuevo registro correspondiente al objeto <code>entidad</code>.
	 * Modifica <code>entidad</code> para reflejar que ya esta persistido.
	 * Ademas lo regresa.
	 * 
	 * @param entidad
	 * @return
	 */
	public MODELO guardar(MODELO entidad);

	/**
	 * Actualiza un registro correspondiente al objeto <code>entidad</code>.
	 * 
	 * @param entidad
	 * @return
	 */
	public MODELO actualizar(MODELO entidad);

	/**
	 * Elimina la tupla correspondiente (según su id) a <code>entidad</code> de
	 * la bd.
	 * 
	 * @param entidad
	 * @return
	 */
	public boolean eliminar(MODELO entidad);

	/**
	 * Método de conveniencia para que desde cualquier objeto service se pueda
	 * cargar a traves de su id cualquier registro asociado a la clase
	 * persistente.
	 * 
	 * @param id
	 * @return El registro indentificado por la llave primaria <code>id</code>
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE traerPorIdGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente, CLASE_ID id);

	/**
	 * @param instanciaEjemplo
	 *            Utiliza la instancia <code>instanciaEjemplo</code> para
	 *            encontrar los registros en la BD que coincidan con los
	 *            atributos que no son nulos del objeto (Obviamente utilizando
	 *            el mapeo de atributos a propiedades).
	 * @return Lista de objetos que coincidan con el ejemplo
	 */
	public List<MODELO> encontrarPorInstanciaEjemplo(MODELO instanciaEjemplo);

	/**
	 * Guarda la entidad mapeada en la bd.
	 * 
	 * @param entidad
	 * @return El objeto persistido (con la id asignada, listo para su uso)
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE guardarGenerico(
			CLASE_PERSISTENTE entidad);

	/**
	 * Actualiza la entidad mapeada en la bd.
	 * 
	 * @param entidad
	 * @return El objeto persistido y con sus propiedades actualizadas
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> CLASE_PERSISTENTE actualizarGenerico(
			CLASE_PERSISTENTE entidad);

	/**
	 * Elimina una entidad de la base de datos
	 * 
	 * @param entidad
	 *            Entidad a eliminar
	 * @return
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> boolean eliminarGenerico(
			CLASE_PERSISTENTE entidad);

	/**
	 * 
	 * @param <CLASE_PERSISTENTE>
	 * @param <CLASE_ID>
	 * @param clasePersistente
	 * @return Lista de todos los registros de la tabla asociada con
	 *         <code>clasePersistente</code>
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> traerTodosGenerico(
			Class<CLASE_PERSISTENTE> clasePersistente);

	/**
	 * 
	 * @param <CLASE_PERSISTENTE>
	 * @param <CLASE_ID>
	 * @param clasePersistente
	 * @param ids
	 * @return La lista de objetos de tipo <code>clasePersistente</code>
	 *         identificados por uno de los elementos en <code>ids</code>
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> traerPorIds(
			Class<CLASE_PERSISTENTE> clasePersistente, List<CLASE_ID> ids);

	/**
	 * 
	 * @param <CLASE_PERSISTENTE>
	 * @param <CLASE_ID>
	 * @param instanciaEjemplo
	 * @return Las instancias de CLASE_PERSISTENTE que coincidan con los
	 *         atributos no nulos de <code>instanciaEjemplo</code>
	 */
	public <CLASE_PERSISTENTE extends IModelo<CLASE_ID>, CLASE_ID extends Serializable> List<CLASE_PERSISTENTE> encontrarPorInstanciaEjemploGenerico(
			CLASE_PERSISTENTE instanciaEjemplo);

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IGenericoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.IModelo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * Clase controlador que contiene todos los métodos básicos de un catálogo,
 * altas, bajas y cambios. Utiliza para las llamadas a la capa de
 * servicio(negocio, lógica de la aplicación) el objeto
 * <code>catalogoGenericoService</code>.
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 * @param <MODELO>
 *            Clase persistente
 * @param <ID>
 *            Clase del id de la clase persistente
 */
public abstract class CatalogoGenericoController<MODELO extends IModelo<ID>, ID extends Serializable>
		extends ActionSupport implements ModelDriven<MODELO> {
	private Class<MODELO> clasePersistente;
	private Logger loggerClaseAbstracta = Logger
			.getLogger(CatalogoGenericoController.class);
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected IGenericoService<MODELO, ID> catalogoGenericoService;
	protected MODELO model = null;
	protected ID idSel = null;
	protected Collection<MODELO> list = null;
	protected String entidad;
	protected String idEntidad;
	protected String operacion;

	@SuppressWarnings("unchecked")
	public CatalogoGenericoController() {
		this.clasePersistente = (Class<MODELO>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public CatalogoGenericoController(
			@SuppressWarnings("rawtypes") IGenericoService catalogoGenericoService) {
		this.clasePersistente = (Class<MODELO>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.catalogoGenericoService = catalogoGenericoService;
	}

	@SkipValidation
	public String edit() {
		return "edit";
	}

	@SkipValidation
	public String editNew() {
		loggerClaseAbstracta.trace("Se crea un stubb de entidad "
				+ clasePersistente);
		try {
			model = clasePersistente.newInstance();
		} catch (InstantiationException e) {
			loggerClaseAbstracta.error("No se pudo instanciar la clase "
					+ clasePersistente);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			loggerClaseAbstracta
					.error("Error de acceso ilegal al intentar instanciar la clase "
							+ clasePersistente);
			e.printStackTrace();
		}
		return "editNew";
	}

	@SkipValidation
	public String deleteConfirm() {
		return "deleteConfirm";
	}

	public HttpHeaders create() {
		loggerClaseAbstracta.trace("Se intenta salvar " + model);
		model = catalogoGenericoService.guardar(model);
		return new DefaultHttpHeaders("success").setLocationId(model.getId());
	}

	@SkipValidation
	public String destroy() {
		loggerClaseAbstracta.debug("Intentando eliminar " + idSel + " modelo "
				+ model.getId());
		catalogoGenericoService.eliminar(model);
		return "success";
	}

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show").disableCaching();
	}

	public String update() {
		catalogoGenericoService.actualizar(model);
		return SUCCESS;
	}

	@SkipValidation
	public HttpHeaders index() {
		loggerClaseAbstracta.trace("Pidiendo todos");
		list = catalogoGenericoService.traerTodos();
		if (list == null)
			loggerClaseAbstracta.trace("Lista nula");
		else
			loggerClaseAbstracta.trace("Lista con " + list.size()
					+ " elementos");
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public void setIdSel(ID idSel) {
		loggerClaseAbstracta.trace("Poniendo el id " + idSel);
		loggerClaseAbstracta.trace("El modelo actual es " + model);
		this.idSel = idSel;
	}

	@Override
	public MODELO getModel() {
		loggerClaseAbstracta.trace("el id para obtener el modelo es " + idSel);
		if (model != null) {
			loggerClaseAbstracta.trace("el modelo es " + model.getId());
		} else {
			try {
				// Si el atributo id existe, se debe traer el registro con ese
				// id de la bd
				if (idSel != null) {
					loggerClaseAbstracta.trace("Trayendo la instancia de "
							+ clasePersistente.getSimpleName() + " con id "
							+ idSel);
					model = catalogoGenericoService.traerPorId(idSel);
					return model;
				}
				// Si no hay id, se esta creando un nuevo objeto, por lo tanto
				// se crea una instancia vacia
				else {
					loggerClaseAbstracta.trace("Creando una instancia de "
							+ clasePersistente.getSimpleName() + " vacia");
					model = clasePersistente.newInstance();

				}
			} catch (InstantiationException e) {
				loggerClaseAbstracta.error("No se pudo instanciar la clase "
						+ clasePersistente);
				loggerClaseAbstracta.error(ExceptionUtils.getStackTrace(e));
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				loggerClaseAbstracta
						.error("Error de acceso ilegal al intentar instanciar la clase "
								+ clasePersistente);
				loggerClaseAbstracta.error(ExceptionUtils.getStackTrace(e));
				e.printStackTrace();
			}
		}
		return model;
	}

	public IGenericoService<MODELO, ID> getCatalogoGenericoService() {
		return catalogoGenericoService;
	}

	public void setCatalogoGenericoService(
			IGenericoService<MODELO, ID> catalogoGenericoService) {
		this.catalogoGenericoService = catalogoGenericoService;
	}

	public Collection<MODELO> getList() {
		return list;
	}

	public void setList(Collection<MODELO> list) {
		this.list = list;
	}

	/**
	 * Propiedad necesaria para que funcione correctamente jqGrid
	 * 
	 * @return
	 */
	public Integer getPage() {
		return 1;
	}

	public ID getIdSel() {
		return idSel;
	}

	public void setModel(MODELO model) {
		this.model = model;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(String idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

}

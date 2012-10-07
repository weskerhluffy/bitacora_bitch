package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IProblemaService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "catalogo-problema" }) })
public class CatalogoProblemaController extends
		CatalogoGenericoController<Problema, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766320502282608749L;
	private List<Area> areasDisponibles = null;
	private Boolean areaEditable = false;

	@Inject
	public CatalogoProblemaController(IProblemaService problemaService) {
		super(problemaService);
	}

	@Override
	public HttpHeaders index() {
		HttpHeaders httpHeaders = null;
		List<Problema> problemas;
		httpHeaders = super.index();
		problemas = new ArrayList<Problema>();
		for (Problema problema : list) {
			if (problema.getTmProblemaElim() == null) {
				problemas.add(problema);
			}
		}
		list = problemas;

		return httpHeaders;
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "nbProblema", type = ValidatorType.SIMPLE, key = "nbProblema"),
			@RequiredStringValidator(fieldName = "txResponsabilidad", type = ValidatorType.SIMPLE, key = "txResponsabilidad"),
			@RequiredStringValidator(fieldName = "txCorrige", type = ValidatorType.SIMPLE, key = "txCorrige") },

	requiredFields = { @RequiredFieldValidator(fieldName = "cdArea", type = ValidatorType.SIMPLE, key = "cdArea") })
	@Override
	public HttpHeaders create() {
		return super.create();
	}

	@Override
	public String edit() {
		return super.edit();
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "nbProblema", type = ValidatorType.SIMPLE, key = "nbProblema"),
			@RequiredStringValidator(fieldName = "txResponsabilidad", type = ValidatorType.SIMPLE, key = "txResponsabilidad"),
			@RequiredStringValidator(fieldName = "txCorrige", type = ValidatorType.SIMPLE, key = "txCorrige") })
	@Override
	public String update() {
		return super.update();
	}

	@SuppressWarnings("unchecked")
	public List<Area> getAreasDisponibles() {
		areasDisponibles = (List<Area>) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.AREAS_DISPONIBLES);
		if (areasDisponibles == null) {
			areasDisponibles = catalogoGenericoService
					.traerTodosGenerico(Area.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.AREAS_DISPONIBLES, areasDisponibles);
		}
		return areasDisponibles;
	}

	@Override
	public String destroy() {
		model.setTmProblemaElim(new Date());
		catalogoGenericoService.actualizar(model);
		return SUCCESS;
	}

	public Boolean getAreaEditable() {
		areaEditable = !getProblemaService().problemaAsociado(model);
		logger.trace("El area resulto editable " + areaEditable);
		return areaEditable;
	}

	public void setAreasDisponibles(List<Area> areas) {
		this.areasDisponibles = areas;
	}

	public IProblemaService getProblemaService() {
		return (IProblemaService) catalogoGenericoService;
	}

	public void setAreaEditable(Boolean areaEditable) {
		this.areaEditable = areaEditable;
	}
}

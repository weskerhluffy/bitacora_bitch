package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IAreaService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "catalogo-area" }) })
public class CatalogoAreaController extends
		CatalogoGenericoController<Area, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766320502282608749L;

	@Inject
	public CatalogoAreaController(IAreaService areaService) {
		super(areaService);
	}

	@Override
	public HttpHeaders index() {
		List<Area> areas = null;
		HttpHeaders httpHeaders;

		httpHeaders = super.index();
		areas = new ArrayList<Area>();
		for (Area area : list) {
			if (area.getTmAreaElim() == null) {
				areas.add(area);
			}
		}
		list = areas;
		return httpHeaders;
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "nbArea", key = "nbArea", type = ValidatorType.SIMPLE) })
	@Override
	public HttpHeaders create() {
		return super.create();
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "nbArea", key = "nbArea", type = ValidatorType.SIMPLE) })
	@Override
	public String update() {
		return super.update();
	}

	@Override
	public String destroy() {
		model.setTmAreaElim(new Date());
		catalogoGenericoService.actualizar(model);
		return SUCCESS;
	}

	public IAreaService getAreaService() {
		return (IAreaService) catalogoGenericoService;
	}
}

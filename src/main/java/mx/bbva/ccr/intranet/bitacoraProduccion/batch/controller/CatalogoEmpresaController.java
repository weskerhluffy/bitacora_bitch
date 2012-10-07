package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IEmpresaService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "catalogo-empresa" }) })
public class CatalogoEmpresaController extends
		CatalogoGenericoController<Empresa, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542836679518111731L;

	@Inject
	public CatalogoEmpresaController(IEmpresaService empresaService) {
		super(empresaService);
	}

	@Override
	public HttpHeaders index() {
		HttpHeaders httpHeaders;
		List<Empresa> empresas;

		httpHeaders = super.index();
		empresas = new ArrayList<Empresa>();
		for (Empresa empresa : list) {
			if (empresa.getTmEmpresaElim() == null) {
				empresas.add(empresa);
			}
		}
		list = empresas;
		return httpHeaders;
	}

	public void validateCreate() {
		// if (getEmpresaService().validarCodigoEmpresaUnico(model)) {
		// addActionError(getText("validarCodigoEmpresaUnico"));
		// }
	}

	@Validations(

	requiredStrings = {
			@RequiredStringValidator(fieldName = "cdEmpresa", key = "cdEmpresa", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbEmpresa", key = "nbEmpresa", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "cdGsi", key = "cdGsi", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbGsi", key = "nbGsi", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "cdEvidencia", key = "cdEvidencia", type = ValidatorType.SIMPLE) },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "hmDifHoraria", key = "hmDifHoraria", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "stDifHorariaPos", key = "stDifHorariaPos", type = ValidatorType.SIMPLE) },

	conversionErrorFields = { @ConversionErrorFieldValidator(fieldName = "hmDifHoraria", key = "hmDifHorariaConversion", type = ValidatorType.SIMPLE) }

	)
	@Override
	public HttpHeaders create() {
		return super.create();
	}

	public void validateUpdate() {
		// if (getEmpresaService().validarCodigoEmpresaUnico(model)) {
		// addActionError(getText("validarCodigoEmpresaUnico"));
		// }
	}

	@Validations(

	requiredStrings = {
			@RequiredStringValidator(fieldName = "cdEmpresaMutable", key = "cdEmpresa", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbEmpresa", key = "nbEmpresa", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "cdGsi", key = "cdGsi", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbGsi", key = "nbGsi", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "cdEvidencia", key = "cdEvidencia", type = ValidatorType.SIMPLE) },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "hmDifHoraria", key = "hmDifHoraria", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "stDifHorariaPos", key = "stDifHorariaPos", type = ValidatorType.SIMPLE) },

	conversionErrorFields = { @ConversionErrorFieldValidator(fieldName = "hmDifHoraria", key = "hmDifHorariaConversion", type = ValidatorType.SIMPLE) }

	)
	@Override
	public String update() {
		return super.update();
	}

	@Override
	public String destroy() {
		model.setTmEmpresaElim(new Date());
		catalogoGenericoService.actualizar(model);
		return SUCCESS;
	}

	public IEmpresaService getEmpresaService() {
		return (IEmpresaService) catalogoGenericoService;
	}

}

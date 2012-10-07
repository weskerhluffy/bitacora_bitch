package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IProcesoEspecialService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Origen;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName",
		"catalogo-proceso-especial",
		"empSel",
		"${#session[@mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion@EMP_SEL].cdEmpresa}" }) })
public class CatalogoProcesoEspecialController extends
		CatalogoGenericoController<ProcesoEspecial, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2815780342203141348L;

	String empSel;
	List<TipoProcesoEspecial> tiposProcesoEspecialDisponibles;
	List<Origen> origenesDisponibles;

	@Inject
	public CatalogoProcesoEspecialController(
			IProcesoEspecialService procesoEspecialService) {
		super(procesoEspecialService);
		logger.trace("Se creo el controlador Proceso Especial");
	}

	@Override
	public HttpHeaders index() {
		Usuario usuario;
		Empresa empresa;
		String cdEmpresaTemp;
		Empresa emp;

		List<ProcesoEspecial> procesosEspeciales = null;

		procesosEspeciales = new ArrayList<ProcesoEspecial>();

		if (empSel == null) {
			usuario = (Usuario) ActionContext.getContext().getSession()
					.get(NombreObjetoSesion.USUARIO);
			emp = usuario.getEmpresas().get(0);
			cdEmpresaTemp = emp.getCdEmpresa();
			empresa = catalogoGenericoService.traerPorIdGenerico(Empresa.class,
					cdEmpresaTemp);

			list = empresa.getProcesosEspeciales();
		} else {
			empresa = catalogoGenericoService.traerPorIdGenerico(Empresa.class,
					empSel);
			list = empresa.getProcesosEspeciales();
		}

		for (ProcesoEspecial procesoEspecial : list) {
			if (procesoEspecial.getFhProcesoEliminado() == null) {
				procesosEspeciales.add(procesoEspecial);
			}
		}

		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.EMP_SEL, empresa);

		list = procesosEspeciales;
		return new DefaultHttpHeaders("index").disableCaching();
	}

	@Override
	public String editNew() {
		return super.editNew();
	}

	public void validateCreate() {
		if (getProcesoEspecialService().validarProcesoEspecialExistente(model)) {
			addActionError(getText("validarProEsp"));
		}
	}

	@Validations(

	requiredStrings = {
			@RequiredStringValidator(fieldName = "nbProceso", key = "nbProceso", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbGrupo", key = "nbGrupo", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbProcEsp", key = "nbProcEsp", type = ValidatorType.SIMPLE) },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "cdTipProEsp", key = "cdTipProEsp", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "cdOrigen", key = "cdOrigen", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "hmTecnica", key = "hmTecnica", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "hmServicio", key = "hmServicio", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "stFhIniDiaSig", key = "stFhIniDiaSig", type = ValidatorType.SIMPLE) },

	conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "hmTecnica", key = "hmTecnicaConversion", type = ValidatorType.SIMPLE),
			@ConversionErrorFieldValidator(fieldName = "hmServicio", key = "hmServicioConversion", type = ValidatorType.SIMPLE) }

	)
	@Override
	public HttpHeaders create() {
		Empresa empresaCache;
		empresaCache = (Empresa) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.EMP_SEL);
		model.setCdEmpresa(empresaCache.getCdEmpresa());
		return super.create();
	}

	public void validateUpdate() {
		if (getProcesoEspecialService().validarProcesoEspecialExistente(model)) {
			addActionError(getText("validarProEsp"));
		}
	}

	@Validations(

	requiredStrings = {
			@RequiredStringValidator(fieldName = "nbProceso", key = "nbProceso", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbGrupo", key = "nbGrupo", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "nbProcEsp", key = "nbProcEsp", type = ValidatorType.SIMPLE) },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "cdTipProEsp", key = "cdTipProEsp", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "cdOrigen", key = "cdOrigen", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "hmTecnica", key = "hmTecnica", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "hmServicio", key = "hmServicio", type = ValidatorType.SIMPLE),
			@RequiredFieldValidator(fieldName = "stFhIniDiaSig", key = "stFhIniDiaSig", type = ValidatorType.SIMPLE) },

	conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "hmTecnica", key = "hmTecnicaConversion", type = ValidatorType.SIMPLE),
			@ConversionErrorFieldValidator(fieldName = "hmServicio", key = "hmServicioConversion", type = ValidatorType.SIMPLE) }

	)
	@Override
	public String update() {
		return super.update();
	}

	public String destroy() {
		model.setFhProcesoEliminado(new Date());
		catalogoGenericoService.actualizar(model);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public List<Origen> getOrigenesDisponibles() {

		origenesDisponibles = (List<Origen>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.ORIGENES_DISPONIBLES);
		if (origenesDisponibles == null) {
			origenesDisponibles = catalogoGenericoService
					.traerTodosGenerico(Origen.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.ORIGENES_DISPONIBLES,
							origenesDisponibles);
		}

		return origenesDisponibles;
	}

	@SuppressWarnings("unchecked")
	public List<TipoProcesoEspecial> getTiposProcesoEspecialDisponibles() {

		tiposProcesoEspecialDisponibles = (List<TipoProcesoEspecial>) ActionContext
				.getContext().getSession()
				.get(NombreObjetoSesion.TIPOS_PROCESO_ESPECIAL_DISPONIBLES);
		if (tiposProcesoEspecialDisponibles == null) {
			tiposProcesoEspecialDisponibles = catalogoGenericoService
					.traerTodosGenerico(TipoProcesoEspecial.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.TIPOS_PROCESO_ESPECIAL_DISPONIBLES,
							tiposProcesoEspecialDisponibles);
		}
		return tiposProcesoEspecialDisponibles;
	}

	public IProcesoEspecialService getProcesoEspecialService() {
		return (IProcesoEspecialService) catalogoGenericoService;
	}

	public String getEmpSel() {
		return empSel;
	}

	public void setEmpSel(String empSel) {
		this.empSel = empSel;
	}

	public void setTiposProcesoEspecialDisponibles(
			List<TipoProcesoEspecial> tiposProcesoEspecialDisponibles) {
		this.tiposProcesoEspecialDisponibles = tiposProcesoEspecialDisponibles;
	}

	public void setOrigenesDisponibles(List<Origen> origenesDisponibles) {
		this.origenesDisponibles = origenesDisponibles;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoDocumentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoDocumentacionId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Origen;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

//TODO Considerar que cuando cae el cancelado, viene con estado registrado, y si se documentó adelantado, su documentación tiene otro estado
@Named
@Results({
		@Result(name = "success", type = "redirectAction", params = {
				"actionName", "bitacora" }),
		@Result(name = "bitacora", type = "redirectAction", params = {
				"actionName", "bitacora" }),
		@Result(name = "traerProblemas", type = "json", params = {
				"includeProperties", "problemasDisponibles.*" }),
		@Result(name = "traerCanceladoDocumentacions", type = "json", params = {
				"includeProperties", "canceladosDocumentacionsSel.*,JSON,page" }),
		@Result(name = "dummy", type = "json", params = { "excludeProperties",
				".*" }) })
public class DocumentacionAdelantadaController extends
		CatalogoGenericoController<Documentacion, Integer> implements
		ParameterAware {

	private static final long serialVersionUID = -4586120842223895490L;
	private IBitacoraService bitacoraService;
	private Integer idAreaSel = null;
	private Integer cdOrigenSel = null;
	private Usuario usuarioSel;
	private Bitacora bitacoraSel = null;
	private List<CanceladoDocumentacion> canceladosDocumentacionsSel = null;
	private List<Origen> origenesDisponibles = null;
	private List<Area> areasDisponibles = null;
	private List<Problema> problemasDisponibles = null;

	@Inject
	public DocumentacionAdelantadaController(
			IDocumentacionService documentacionService) {
		super(documentacionService);
		ActionContext.getContext().getContextMap()
				.put(ReflectionContextState.CREATE_NULL_OBJECTS, true);
	}

	@Override
	public HttpHeaders index() {
		areasDisponibles = catalogoGenericoService
				.traerTodosGenerico(Area.class);
		origenesDisponibles = catalogoGenericoService
				.traerTodosGenerico(Origen.class);

		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.AREAS_DISPONIBLES, areasDisponibles);
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.ORIGENES_DISPONIBLES,
						origenesDisponibles);
		return new DefaultHttpHeaders("index").disableCaching();
	}

	/**
	 * Se valida lo siguiente:
	 * <ul>
	 * <li>Que los cancelados no hayan sido registrados por el sistema
	 * <li>Si ya fueron registrados, que esten en estado
	 * {@link Estado#REGISTRADO}
	 * <ul>
	 * 
	 * Efectos secundarios: Pasa el nombre del grupo, del proceso y el paso de
	 * cada {@link CanceladoDocumentacion} a mayusculas.
	 */
	public void validateCreate() {
		List<Cancelado> cancelados = null;
		List<CanceladoId> canceladoIds = null;
		logger.trace("Validando creacion");
		if (canceladosDocumentacionsSel != null) {
			canceladoIds = new ArrayList<CanceladoId>();
			for (CanceladoDocumentacion canceladosDocumentacion : canceladosDocumentacionsSel) {
				canceladosDocumentacion.setNbGrupo(canceladosDocumentacion
						.getNbGrupo().toUpperCase());
				canceladosDocumentacion.setNbProceso(canceladosDocumentacion
						.getNbProceso().toUpperCase());
				canceladosDocumentacion.setCdPaso(canceladosDocumentacion
						.getCdPaso().toUpperCase());
				canceladoIds.add(new CanceladoId(canceladosDocumentacion
						.getCdEmpresa(), canceladosDocumentacion.getNbGrupo(),
						canceladosDocumentacion.getNbProceso(),
						canceladosDocumentacion.getCdPaso(),
						canceladosDocumentacion.getFhCiclo(), cdOrigenSel));
			}
			cancelados = getDocumentacionService()
					.validarCanceladoSoloRegistrado(canceladoIds);
			for (Cancelado cancelado : cancelados) {
				addActionError("El cancelado "
						+ cancelado.getId()
						+ " ya fue registrado por el sistema, eliminelo de la lista y documentelo por la via normal");
			}
		}
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.CANCELADOS_DOCUMENTACION_SEL,
						canceladosDocumentacionsSel);

		if (!getDocumentacionService().validarTextoDocumentacion(model)) {
			addActionError("Debe redactar los campos de documentaci�n");
		}
	}

	@SuppressWarnings("unchecked")
	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "model.txSintoma", type = ValidatorType.FIELD, message = "Debe documentar el síntoma."),
			@RequiredStringValidator(fieldName = "model.txImpacto", type = ValidatorType.FIELD, message = "Debe documentar el impacto."),
			@RequiredStringValidator(fieldName = "model.txRecuperacion", type = ValidatorType.FIELD, message = "Debe documentar la recuperación") },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "cdOrigenSel", type = ValidatorType.FIELD, message = "Debe seleccionar un origen"),
			@RequiredFieldValidator(fieldName = "model.cdProblema", type = ValidatorType.FIELD, message = "Debe seleccionar un problema") },

	expressions = { @ExpressionValidator(expression = "!canceladosDocumentacionsSel.isEmpty", message = "Debe seleccionar al menos un cancelado para documentar.") },

	conversionErrorFields = { @ConversionErrorFieldValidator(fieldName = "model.cdProblema", type = ValidatorType.FIELD, message = "Se debe asociar un problema al(los) cancelado(s).") },

	stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "model.txSintoma", maxLength = "700", message = "El síntoma no puede ser de mas de 700 caracteres"),
			@StringLengthFieldValidator(fieldName = "model.txImpacto", maxLength = "700", message = "El impacto no puede ser de mas de 700 caracteres"),
			@StringLengthFieldValidator(fieldName = "model.txRecuperacion", maxLength = "700", message = "La recuperación no puede ser de mas de 700 caracteres") }

	)
	@Override
	public HttpHeaders create() {
		logger.trace("Sintoma " + model.getTxSintoma());
		logger.trace("Recuperación " + model.getTxRecuperacion());
		logger.trace("Impacto " + model.getTxImpacto());
		logger.trace("Area " + idAreaSel);
		Date tiempoActual = new Date();
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);

		model.setCdBitacora(bitacoraSel.getCdBitacora());
		model.setCdUsuario(usuarioSel.getCdUsuario());
		model.setTmDocumentacion(tiempoActual);
		model.setCdEstado(Estado.INSTRUCCIONES_ENVIADAS);

		logger.trace("La documentación que se guardara " + model);
		canceladosDocumentacionsSel = (List<CanceladoDocumentacion>) ActionContext
				.getContext().getSession()
				.get(NombreObjetoSesion.CANCELADOS_DOCUMENTACION_SEL);
		for (CanceladoDocumentacion canceladoDocumentacion : canceladosDocumentacionsSel) {
			canceladoDocumentacion.setId(new CanceladoDocumentacionId(
					bitacoraSel.getCdEmpresa(), canceladoDocumentacion
							.getNbProceso(), canceladoDocumentacion
							.getFhCiclo(), canceladoDocumentacion.getCdPaso(),
					canceladoDocumentacion.getNbGrupo(), cdOrigenSel, null));
		}
		getDocumentacionService().guardarDocumentacion(model,
				canceladosDocumentacionsSel);
		getDocumentacionService().sincronizarDocumentacionAdelantada();

		return new DefaultHttpHeaders("bitacora");
	}

	@SkipValidation
	@SuppressWarnings("unchecked")
	public String traerProblemas() {
		if ((areasDisponibles = (List<Area>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.AREAS_DISPONIBLES)) == null) {
			areasDisponibles = catalogoGenericoService
					.traerTodosGenerico(Area.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.AREAS_DISPONIBLES, areasDisponibles);
		}
		if (areasDisponibles.size() < 1) {
			logger.error("El area " + idAreaSel + " no tiene problemas");
			return "traerProblemas";
		}
		if (idAreaSel == null) {
			idAreaSel = areasDisponibles.get(0).getId();
		}
		logger.trace("El id de area seleccionado para traer problemas es "
				+ idAreaSel);
		for (Area area : areasDisponibles) {
			logger.trace("Comparando " + idAreaSel + " con " + area.getId());
			if (area.getId().equals(idAreaSel)) {
				problemasDisponibles = area.getProblemas();
				break;
			}
		}
		logger.trace("Para el area " + idAreaSel + " el numero de problmas es "
				+ problemasDisponibles.size());
		return "traerProblemas";
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	public String traerCanceladoDocumentacions() {
		canceladosDocumentacionsSel = (List<CanceladoDocumentacion>) ActionContext
				.getContext().getSession()
				.get(NombreObjetoSesion.CANCELADOS_DOCUMENTACION_SEL);
		return "traerCanceladoDocumentacions";
	}

	/**
	 * Validacion común de las operaciones de este controlador, que exista una
	 * en sesión y que no haya sido delegada.
	 */
	public void validate() {
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);

		if (bitacoraSel == null) {
			addActionError("La sesion expir�, por favor salgase y vuelva a entrar.");
			logger.warn("En la sesión falta la bitácora " + bitacoraSel);
		}
		if (bitacoraSel != null) {
			if (bitacoraService.validaBitacoraDelegada(bitacoraSel)) {
				logger.warn("La bitácora " + bitacoraSel
						+ " ya fue delegada a las "
						+ bitacoraSel.getTmDelegacion());
				addActionError("La bitácora de "
						+ bitacoraSel.getEmpresa().getNbEmpresa()
						+ " de la fecha " + bitacoraSel.getFhCicloBitacora()
						+ " fue delegada en " + bitacoraSel.getTmDelegacion());
			}
		} else {
			addActionError("La sesión expiro, no se encuentra la bitácora, reingrese al sistema.");
		}

	}

	@SkipValidation
	public String dummy() {
		return "dummy";
	}

	/**
	 * Se asigna al parametro id el valor 0 debido a que es necesario para que
	 * funcione el componente grid en modo de edición
	 */
	@Override
	public void setParameters(Map<String, String[]> parametros) {
		parametros.put("id", new String[] { "0" });
	}

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a lista de origenes
	public List<Origen> getOrigenesDisponibles() {
		if (origenesDisponibles == null) {
			origenesDisponibles = (List<Origen>) ActionContext.getContext()
					.getSession().get(NombreObjetoSesion.ORIGENES_DISPONIBLES);
		}
		return origenesDisponibles;
	}

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a lista de areas
	public List<Area> getAreasDisponibles() {
		if (areasDisponibles == null) {
			areasDisponibles = (List<Area>) ActionContext.getContext()
					.getSession().get(NombreObjetoSesion.AREAS_DISPONIBLES);
		}
		return areasDisponibles;
	}

	public Integer getIdAreaSel() {
		return idAreaSel;
	}

	public void setIdAreaSel(Integer idAreaSel) {
		this.idAreaSel = idAreaSel;
	}

	public void setAreasDisponibles(List<Area> areasDisponibles) {
		this.areasDisponibles = areasDisponibles;
	}

	public List<Problema> getProblemasDisponibles() {
		return problemasDisponibles;
	}

	public void setProblemasDisponibles(List<Problema> problemasDisponibles) {
		this.problemasDisponibles = problemasDisponibles;
	}

	public Bitacora getBitacoraSel() {
		return bitacoraSel;
	}

	public void setBitacoraSel(Bitacora bitacoraSel) {
		this.bitacoraSel = bitacoraSel;
	}

	public IDocumentacionService getDocumentacionService() {
		return (IDocumentacionService) catalogoGenericoService;
	}

	public void setOrigenesDisponibles(List<Origen> origenesDisponibles) {
		this.origenesDisponibles = origenesDisponibles;
	}

	public Integer getCdOrigenSel() {
		return cdOrigenSel;
	}

	public void setCdOrigenSel(Integer cdOrigenSel) {
		this.cdOrigenSel = cdOrigenSel;
	}

	public List<CanceladoDocumentacion> getCanceladosDocumentacionsSel() {
		if (canceladosDocumentacionsSel == null) {
			canceladosDocumentacionsSel = new ArrayList<CanceladoDocumentacion>();
		}
		return canceladosDocumentacionsSel;
	}

	public void setCanceladosDocumentacionsSel(
			List<CanceladoDocumentacion> canceladosDocumentacionsSel) {
		this.canceladosDocumentacionsSel = canceladosDocumentacionsSel;
	}

	@Override
	public Integer getPage() {
		return super.getPage();
	}

	public String getJSON() {
		return SUCCESS;
	}

	public Usuario getUsuarioSel() {
		return usuarioSel;
	}

	public void setUsuarioSel(Usuario usuarioSel) {
		this.usuarioSel = usuarioSel;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

}

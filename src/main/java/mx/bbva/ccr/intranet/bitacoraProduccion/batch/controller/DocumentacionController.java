package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Evidencia;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.annotations.RemoteProxy;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.CreateIfNull;
import com.opensymphony.xwork2.util.Element;
import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

//TODO Que se pueda actualizar el estado y el problema de forma asincrona
//TODO Modificar el js para utilizar forma-ajax-util.js
//TODO Sincronizar pantallas debido a cambios en problemas y estados 
@RemoteProxy
@Named
@Results({
		@Result(name = "success", type = "redirectAction", params = {
				"actionName", "bitacora" }),
		@Result(name = "bitacora", type = "redirectAction", params = {
				"actionName", "bitacora" }),
		@Result(name = "traerProblemas", type = "json", params = {
				"includeProperties", "problemasDisponibles.*" }),
		@Result(name = "traerEstados", type = "json", params = {
				"includeProperties",
				"JSON,estatusTodos.*,canceladoPrincipalSel.cdEstadoCan" }),
		@Result(name = "anadirRecuperacion", type = "json", params = {
				"includeProperties", "JSON" }),
		@Result(name = "bajarEvidencia", type = "stream", params = {
				"contentType", "${contentType}", "contentDisposition",
				"attachment;filename=${model.ultimaEvidencia.nbEvidencia}" }) })
public class DocumentacionController extends
		CatalogoGenericoController<Documentacion, Integer> implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -575774936864227358L;

	private IBitacoraService bitacoraService = null;

	private HttpServletRequest httpServletRequest;
	private String rutaContexto;
	private Integer idAreaSel = null;
	private Integer idEstatusSel = null;
	private CanceladoId canceladoIdPrincipalSel = null;
	private Cancelado canceladoPrincipalSel = null;
	private List<Area> areasDisponibles = null;
	private List<Problema> problemasDisponibles = null;
	private List<Estado> estatusTodos = null;
	private List<CanceladoId> canceladoIdsSel = null;
	@CreateIfNull(value = true)
	@Element(value = Documentacion.class)
	private List<Cancelado> canceladosSel;
	private Bitacora bitacoraSel = null;
	private Usuario usuarioSel;
	private File file;
	private String contentType;
	private String filename;
	private InputStream inputStream;

	@Inject
	public DocumentacionController(IDocumentacionService documentacionService) {
		super(documentacionService);
	}

	@Override
	public HttpHeaders index() {
		logger.trace("Wake me up, before you go-go");
		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get("usuario");
		// Trayendo areas, cancelados y cancelado principal
		areasDisponibles = catalogoGenericoService
				.traerTodosGenerico(Area.class);
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		bitacoraSel.getEmpresa().getNbEmpresa();

		if (canceladoIdsSel != null) {
			canceladosSel = catalogoGenericoService.traerPorIds(
					Cancelado.class, canceladoIdsSel);
			for (Cancelado cancelado : canceladosSel) {
				logger.trace("Comparando " + cancelado.getId() + " con "
						+ canceladoIdPrincipalSel);
				if (cancelado.getId().equals(canceladoIdPrincipalSel)) {
					canceladoPrincipalSel = cancelado;
				}
			}
		} else {
			if (canceladoIdPrincipalSel != null) {
				canceladoPrincipalSel = catalogoGenericoService
						.traerPorIdGenerico(Cancelado.class,
								canceladoIdPrincipalSel);
				canceladosSel = canceladoPrincipalSel
						.getCanceladosSecundarios();
				canceladosSel.add(canceladoPrincipalSel);
				logger.trace("Del cancelado principal " + canceladoPrincipalSel
						+ " se desprenden " + canceladosSel.size()
						+ " secundarios");
			} else {
				logger.warn("No hay cancelados con los cuales trabajar");
			}
		}

		if (canceladoPrincipalSel != null) {
			idAreaSel = canceladoPrincipalSel.getProblema().getCdArea();
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.CANCELADO_SELECCIONADO_PRINCIPAL,
							canceladoPrincipalSel);
			logger.trace("El cancelado principal a documentar es "
					+ canceladoPrincipalSel);
			model = canceladoPrincipalSel.getUltimaDocumentacion();
			logger.trace("La documentación que se trabajará es " + model);
		}
		if (canceladosSel != null) {
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.CANCELADOS_SELECCIONADOS,
							canceladosSel);
		}

		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.AREAS_DISPONIBLES, areasDisponibles);
		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.ULTIMA_DOCUMENTACION, model);
		return new DefaultHttpHeaders("index").disableCaching();
	}

	/**
	 * Se valida la documentación de un cancelado de manera siguiente:
	 * <ul>
	 * <li>Si no se encontro ningún cancelado seleccionado, se produce un error
	 * de validación y se aborta la operación.
	 * <li>Que la bitacora y el usuario esten en sesión.
	 * <li>Que exista en sesión un cancelado principal
	 * <li>Lo que defina <code>validarCanceladoDocumentable</code> del servicio
	 * de documentación.
	 * <ul>
	 */
	@SuppressWarnings("unchecked")
	public void validateCreate() {
		List<Cancelado> canceladosNoDocumentables = null;
		List<CanceladoId> canceladoIdsSel = null;
		logger.trace("Validando usuario y bitacora de sesion");
		logger.trace("Evidencia " + filename + " archivo " + file);

		canceladosSel = (List<Cancelado>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.CANCELADOS_SELECCIONADOS);
		if (canceladosSel == null || canceladosSel.size() < 1) {
			logger.warn("No hay cancelados seleccionados para documentar");
			addActionError("No se pueden obtener los cancelados, salga y vuelva a entrar de la aplicación");
		} else {
			canceladoIdsSel = new ArrayList<CanceladoId>();
			for (Cancelado cancelado : canceladosSel) {
				canceladoIdsSel.add(cancelado.getId());
			}
			canceladosNoDocumentables = getDocumentacionService()
					.validarCanceladoDocumentable(canceladoIdsSel);
			for (Cancelado cancelado : canceladosNoDocumentables) {
				addActionError("El cancelado "
						+ cancelado.getCdCancelado()
						+ " no se puede documentar con este grupo, regrese a la pantalla principal y elija otro grupo de cancelados");
			}
		}
		if (!getDocumentacionService().validarTextoDocumentacion(model)) {
			addActionError("Debe redactar los campos de documentación");
		}
	}

	@SuppressWarnings("unchecked")
	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "model.txSintoma", type = ValidatorType.FIELD, message = "Debe documentar el síntoma."),
			@RequiredStringValidator(fieldName = "model.txImpacto", type = ValidatorType.FIELD, message = "Debe documentar el impacto."),
			@RequiredStringValidator(fieldName = "model.txRecuperacion", type = ValidatorType.FIELD, message = "Debe documentar la recuperación") },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "model.cdEstado", type = ValidatorType.FIELD, message = "Debe seleccionar un estado."),
			@RequiredFieldValidator(fieldName = "model.cdProblema", type = ValidatorType.FIELD, message = "Debe seleccionar un problema.") },

	conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "model.cdEstado", type = ValidatorType.FIELD, message = "Se debe seleccionar un estado para el(los) cancelado(s)."),
			@ConversionErrorFieldValidator(fieldName = "model.cdProblema", type = ValidatorType.FIELD, message = "Se debe asociar un problema al(los) cancelado(s).") }, stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "model.txSintoma", maxLength = "700", message = "El síntoma no puede ser de mas de 700 caracteres"),
			@StringLengthFieldValidator(fieldName = "model.txImpacto", maxLength = "700", message = "El impacto no puede ser de mas de 700 caracteres"),
			@StringLengthFieldValidator(fieldName = "model.txRecuperacion", maxLength = "700", message = "La recuperación no puede ser de mas de 700 caracteres") },

	expressions = {})
	@Override
	public HttpHeaders create() {
		logger.trace("Sintoma " + model.getTxSintoma());
		logger.trace("Recuperación " + model.getTxRecuperacion());
		logger.trace("Impacto " + model.getTxImpacto());
		logger.trace("Area " + idAreaSel);
		logger.trace("Evidencia " + filename + " archivo " + file);
		Date tiempoActual = new Date();
		Evidencia evidenciaNueva = new Evidencia();
		List<CanceladoId> canceladoIdsSel = new ArrayList<CanceladoId>();
		FileInputStream fileInputStream;
		logger.trace("La ruta de contexto es " + rutaContexto);

		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		canceladoPrincipalSel = (Cancelado) ActionContext.getContext()
				.getSession()
				.get(NombreObjetoSesion.CANCELADO_SELECCIONADO_PRINCIPAL);
		canceladosSel = (List<Cancelado>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.CANCELADOS_SELECCIONADOS);

		for (Cancelado cancelado : canceladosSel) {
			canceladoIdsSel.add(cancelado.getId());
		}

		model.setCdBitacora(bitacoraSel.getCdBitacora());
		model.setCdUsuario(usuarioSel.getCdUsuario());
		model.setTmDocumentacion(tiempoActual);

		logger.trace("La documentación que se guardara " + model.getId());
		getDocumentacionService().guardarDocumentacion(model,
				canceladoPrincipalSel.getId(), canceladoIdsSel);
		canceladoPrincipalSel = catalogoGenericoService.traerPorIdGenerico(
				Cancelado.class, canceladoPrincipalSel.getId());

		if (file != null) {
			evidenciaNueva = new Evidencia();
			evidenciaNueva.setCdDocumentacion(model.getCdDocumentacion());
			evidenciaNueva.setTmEnvioEvid(new Date());
			evidenciaNueva.setBlEvidencia(new byte[(int) file.length()]);
			evidenciaNueva.setNbEvidencia(filename);
			try {
				fileInputStream = new FileInputStream(file);
				fileInputStream.read(evidenciaNueva.getBlEvidencia());
				logger.trace("Guardando archivo de evidencia " + evidenciaNueva);
				catalogoGenericoService.guardarGenerico(evidenciaNueva);
			} catch (FileNotFoundException fnfe) {
				logger.warn("No se pudo procesar el archivo de evidencia "
						+ filename);
				logger.warn(ExceptionUtils.getStackTrace(fnfe));
			} catch (IOException ie) {
				logger.warn("Hubo un error al procesar el archivo " + filename);
				logger.warn(ExceptionUtils.getStackTrace(ie));
			}

		}

		Browser.withPage(rutaContexto + "/documentacion", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso");
				model.setUsuario(usuarioSel);
				ScriptSessions.addFunctionCall("refrescarRecuperaciones",
						canceladoPrincipalSel.getDocumentacions());
			}
		});

		Browser.withPage(rutaContexto + "/bitacora", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en anadir para cancelaciones");
				ScriptSessions.addFunctionCall("refrescarCancelaciones");
			}
		});

		Browser.withPage(rutaContexto + "/bitacora.xhtml", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en anadir para cancelaciones");
				ScriptSessions.addFunctionCall("refrescarCancelaciones");
			}
		});
		return new DefaultHttpHeaders("bitacora");
	}

	/**
	 * Se valida la edición de la documentación de un cancelado de manera
	 * siguiente:
	 * <ul>
	 * <li>Si no se encontro ningún cancelado seleccionado, se produce un error
	 * de validación y se aborta la operación.
	 * <li>Que la bitacora y el usuario esten en sesión.
	 * <li>Que exista en sesión un cancelado principal
	 * <li>Lo que defina <code>validarCanceladoDocumentable</code> del servicio
	 * de documentación.
	 * <ul>
	 */
	public void validateUpdate() {
		Documentacion documentacion = null;

		if (getCanceladoPrincipalSel() != null
				&& getCanceladoPrincipalSel().getUltimaDocumentacion() != null) {
			documentacion = getDocumentacionService()
					.validarCanceladoDocumentable(
							getCanceladoPrincipalSel().getUltimaDocumentacion(),
							getCanceladoPrincipalSel().getId());
			if (documentacion != null) {
				addActionError("El usuario "
						+ documentacion.getCdUsuario()
						+ ": "
						+ documentacion.getUsuario().getTxNombre()
						+ " "
						+ documentacion.getUsuario().getTxAp()
						+ " documentó ya esta cancelación, refresque la pantalla");
			}
		}
		if (!getDocumentacionService().validarTextoDocumentacion(model)) {
			addActionError("Debe redactar los campos de documentación");
		}
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "model.txSintoma", type = ValidatorType.FIELD, message = "Debe documentar el síntoma."),
			@RequiredStringValidator(fieldName = "model.txImpacto", type = ValidatorType.FIELD, message = "Debe documentar el impacto."),
			@RequiredStringValidator(fieldName = "model.txRecuperacion", type = ValidatorType.FIELD, message = "Debe documentar la recuperación") },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "model.cdEstado", type = ValidatorType.FIELD, message = "Debe seleccionar un estado."),
			@RequiredFieldValidator(fieldName = "model.cdProblema", type = ValidatorType.FIELD, message = "Debe seleccionar un problema.") },

	conversionErrorFields = {
			@ConversionErrorFieldValidator(fieldName = "model.cdEstado", type = ValidatorType.FIELD, message = "Se debe seleccionar un estado para el(los) cancelado(s)."),
			@ConversionErrorFieldValidator(fieldName = "model.cdProblema", type = ValidatorType.FIELD, message = "Se debe asociar un problema al(los) cancelado(s).") },

	stringLengthFields = {
			@StringLengthFieldValidator(fieldName = "model.txSintoma", maxLength = "700", message = "El síntoma no puede ser de mas de 700 caracteres"),
			@StringLengthFieldValidator(fieldName = "model.txImpacto", maxLength = "700", message = "El impacto no puede ser de mas de 700 caracteres"),
			@StringLengthFieldValidator(fieldName = "model.txRecuperacion", maxLength = "700", message = "La recuperación no puede ser de mas de 700 caracteres") },

	expressions = {})
	@Override
	public String update() {
		create();
		return SUCCESS;
	}

	public String bajarEvidencia() {

		logger.trace("Enviando evidencia de la documentacion " + model);
		contentType = "application/octet-stream";

		if ((model = (Documentacion) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.ULTIMA_DOCUMENTACION)) != null) {
			inputStream = new ByteArrayInputStream(model.getUltimaEvidencia()
					.getBlEvidencia());
			logger.trace("Se convirtio el arreglo de bytes a " + inputStream);
		} else {
			logger.warn("No esta ninguna documentación en sesión, no se puede generar el archivo de evidencia");
		}
		return "bajarEvidencia";
	}

	// FIXME Traer solo los problemas habilitados. En consecuencia, validar que
	// no se deshabiliten todos los problemas de un área
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

	@SkipValidation
	public String traerEstados() {
		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		canceladoPrincipalSel = (Cancelado) ActionContext.getContext()
				.getSession()
				.get(NombreObjetoSesion.CANCELADO_SELECCIONADO_PRINCIPAL);
		if (usuarioSel == null || canceladoPrincipalSel == null) {
			logger.warn("En traer estados no hay usuario " + usuarioSel
					+ " o cancelado principal " + canceladoPrincipalSel
					+ ", por lo tanto se aborta la operaci�n");
			return "traerEstados";
		}
		estatusTodos = getDocumentacionService().getEstados(usuarioSel,
				canceladoPrincipalSel);
		return "traerEstados";
	}

	public void validateAnadirRecuperacion() {

		logger.trace("El resultado de la validacion en funcion "
				+ getDocumentacionService().validarTextoRecuperacion(model));
		if (!getDocumentacionService().validarTextoRecuperacion(model)) {
			addActionError("Debe redactar la recuperación");
		}
	}

	/**
	 * Añade una sola documentación de recuperación
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "model.txRecuperacion", type = ValidatorType.FIELD, message = "Debe documentar la recuperación") },

	stringLengthFields = { @StringLengthFieldValidator(fieldName = "model.txRecuperacion", maxLength = "700", message = "La recuperación debe tener menos de 700 caracteres") },

	expressions = {})
	public String anadirRecuperacion() {
		Date tiempoActual = new Date();
		List<CanceladoId> canceladoIdsSel = new ArrayList<CanceladoId>();

		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		canceladoPrincipalSel = (Cancelado) ActionContext.getContext()
				.getSession()
				.get(NombreObjetoSesion.CANCELADO_SELECCIONADO_PRINCIPAL);
		canceladosSel = (List<Cancelado>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.CANCELADOS_SELECCIONADOS);

		for (Cancelado cancelado : canceladosSel) {
			canceladoIdsSel.add(cancelado.getId());
		}

		model.setCdBitacora(bitacoraSel.getCdBitacora());
		model.setCdUsuario(usuarioSel.getCdUsuario());
		model.setTmDocumentacion(tiempoActual);
		model.setCdEstado(canceladosSel.get(0).getCdEstadoCan());
		model.setCdProblema(canceladosSel.get(0).getCdProblema());
		model.setTxImpacto(canceladosSel.get(0).getUltimaDocumentacion()
				.getTxImpacto());
		model.setTxSintoma(canceladosSel.get(0).getUltimaDocumentacion()
				.getTxSintoma());
		getDocumentacionService().guardarDocumentacion(model,
				canceladoPrincipalSel.getId(), canceladoIdsSel);

		// Actualizando el cancelado principal en sesion
		canceladoPrincipalSel = catalogoGenericoService.traerPorIdGenerico(
				Cancelado.class, canceladoPrincipalSel.getId());
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.CANCELADO_SELECCIONADO_PRINCIPAL,
						canceladoPrincipalSel);
		Browser.withPage(rutaContexto + "/documentacion", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso");
				model.setUsuario(usuarioSel);
				ScriptSessions.addFunctionCall("refrescarRecuperaciones",
						canceladoPrincipalSel.getDocumentacions());
			}
		});

		Browser.withPage(rutaContexto + "/bitacora", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en anadir para cancelaciones");
				ScriptSessions.addFunctionCall("refrescarCancelaciones");
			}
		});
		Browser.withPage(rutaContexto + "/bitacora.xhtml", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en anadir para cancelaciones");
				ScriptSessions.addFunctionCall("refrescarCancelaciones");
			}
		});
		return "anadirRecuperacion";
	}

	/**
	 * Validaciones comunes a la documentación completa y al chat.
	 */
	public void validate() {
		logger.trace("Evidencia " + filename + " archivo "
				+ (file != null ? file.getTotalSpace() : "0"));
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);

		if (bitacoraSel == null) {
			addActionError("La sesion expir�, por favor salgase y vuelva a entrar.");
			logger.warn("En la sesión falta el usuario " + usuarioSel
					+ " o la bitácora " + bitacoraSel);
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
		if (getCanceladoPrincipalSel() == null) {
			logger.warn("No hay cancelado principal en sesión");
			addActionError("No se ha seleccionado un cancelado principal, por favor regrese a la pantalla principal y elija otro grupo de cancelados");
		}

	}

	/**
	 * Obtiene el cancelado principal en sesion o nulo si no lo hay
	 * 
	 * @return
	 */
	public Cancelado getCanceladoPrincipalSel() {
		if (ActionContext.getContext() != null
				&& ActionContext.getContext().getSession() != null
				&& canceladoPrincipalSel == null) {
			canceladoPrincipalSel = (Cancelado) ActionContext.getContext()
					.getSession()
					.get(NombreObjetoSesion.CANCELADO_SELECCIONADO_PRINCIPAL);
		}
		return canceladoPrincipalSel;
	}

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a lista de áreas disponibles
	public List<Area> getAreasDisponibles() {
		if (areasDisponibles == null) {
			areasDisponibles = (List<Area>) ActionContext.getContext()
					.getSession().get(NombreObjetoSesion.AREAS_DISPONIBLES);
		}
		return areasDisponibles;
	}

	public void setCanceladoIdPrincipalSel(CanceladoId canceladoIdPrincipal) {
		this.canceladoIdPrincipalSel = canceladoIdPrincipal;
	}

	public Integer getIdAreaSel() {
		return idAreaSel;
	}

	public void setIdAreaSel(Integer idAreaSel) {
		this.idAreaSel = idAreaSel;
	}

	public Integer getIdEstatusSel() {
		return idEstatusSel;
	}

	public void setIdEstatusSel(Integer idEstatusSel) {
		this.idEstatusSel = idEstatusSel;
	}

	public CanceladoId getCanceladoIdPrincipalSel() {
		return canceladoIdPrincipalSel;
	}

	public void setCanceladoPrincipalSel(Cancelado canceladoPrincipalSel) {
		this.canceladoPrincipalSel = canceladoPrincipalSel;
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

	public List<Estado> getEstatusTodos() {
		return estatusTodos;
	}

	public void setEstatusTodos(List<Estado> estatusTodos) {
		this.estatusTodos = estatusTodos;
	}

	public List<CanceladoId> getCanceladoIdsSel() {
		return canceladoIdsSel;
	}

	public void setCanceladoIdsSel(List<CanceladoId> canceladoIdsSel) {
		this.canceladoIdsSel = canceladoIdsSel;
	}

	public List<Cancelado> getCanceladosSel() {
		return canceladosSel;
	}

	public void setCanceladosSel(List<Cancelado> canceladosSel) {
		this.canceladosSel = canceladosSel;
	}

	public Bitacora getBitacoraSel() {
		return bitacoraSel;
	}

	public void setBitacoraSel(Bitacora bitacoraSel) {
		this.bitacoraSel = bitacoraSel;
	}

	public Usuario getUsuarioSel() {
		return usuarioSel;
	}

	public void setUsuarioSel(Usuario usuarioSel) {
		this.usuarioSel = usuarioSel;
	}

	public IDocumentacionService getDocumentacionService() {
		return (IDocumentacionService) catalogoGenericoService;
	}

	@Override
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		setHttpServletRequest(httpServletRequest);
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
		rutaContexto = httpServletRequest.getContextPath();
	}

	public String getRutaContexto() {
		return rutaContexto;
	}

	public void setRutaContexto(String rutaContexto) {
		this.rutaContexto = rutaContexto;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setArchivoEvidencia(File file) {
		this.file = file;
	}

	public void setArchivoEvidenciaContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setArchivoEvidenciaFileName(String filename) {
		this.filename = filename;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}

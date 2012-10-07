package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.CanceladoIdConverter;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

//TODO Que al mover cancelados se actualicen las pantallas de los usuarios
@RemoteProxy
@Conversion
@Named
@Results({
		@Result(name = "success", type = "redirectAction", params = {
				"actionName", "bitacora" }),
		@Result(name = "traerCanceladosPendientes", type = "json", params = {
				"includeProperties", "JSON,page,canceladosPendientes.*" }),
		@Result(name = "traerCanceladosClasificados", type = "json", params = {
				"includeProperties", "JSON,page,canceladosClasificados.*" }),
		@Result(name = "moverCancelados", type = "json", params = {
				"excludeProperties", ".*" }) })
public class BitacoraController extends
		CatalogoGenericoController<Bitacora, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4565830120652201846L;
	private Boolean moverComoGrupo = null;
	private Boolean retornaCicloProductivo;
	private Integer tipoCanceladosPendientes = null;
	private Integer idAreaSel = null;
	private String idEmpresaSel = null;
	private String passRecibe;
	private String passAutoriza;
	private String ldapRecibe;
	private String ldapAutoriza;
	private Empresa empresaSeleccionada = null;
	private List<Area> areasDisponibles = null;
	private List<Cancelado> canceladosPendientes = null;
	private List<Cancelado> canceladosClasificados = null;
	private List<CanceladoId> canceladoIdsSel = null;
	private CanceladoId canceladoIdPrincipalSel = null;
	private IDocumentacionService documentacionService = null;

	@Inject
	public BitacoraController(IBitacoraService bitacoraService) {
		super(bitacoraService);
		logger.trace("Se creo el controlador de mierda");
	}

	@Override
	public HttpHeaders index() {

		if (idEmpresaSel != null) {
			empresaSeleccionada = catalogoGenericoService.traerPorIdGenerico(
					Empresa.class, idEmpresaSel);
			ActionContext.getContext().getSession()
					.put(NombreObjetoSesion.EMPRESA, empresaSeleccionada);
		}
		if ((empresaSeleccionada = (Empresa) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.EMPRESA)) != null) {
			idEmpresaSel = empresaSeleccionada.getCdEmpresa();
		}

		list = ((IBitacoraService) catalogoGenericoService)
				.traerUltimasBitacoras(idEmpresaSel);
		logger.trace("se encontraron " + list.size()
				+ " bitacoras para la empresa " + idEmpresaSel);
		// Se almacena en sesión la última bitácora
		model = ((List<Bitacora>) list).get(list.size() - 1);
		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.BITACORA, model);

		areasDisponibles = catalogoGenericoService
				.traerTodosGenerico(Area.class);
		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.AREAS_DISPONIBLES, areasDisponibles);
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public String traerCanceladosPendientes() {
		Map<Integer, List<Cancelado>> canceladosClasificados = null;
		model = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		if (model != null) {
			canceladosPendientes = getBitacoraService().getCanceladosActuales(
					model);
			logger.trace("En traer cancelados pendientes con "
					+ canceladosPendientes.size());
			logger.trace("Hay " + canceladosPendientes.size()
					+ " cancelados pendientes para la bitacacacora"
					+ model.getId());

			canceladosClasificados = getBitacoraService().clasificaCancelados(
					canceladosPendientes);

			canceladosPendientes = getBitacoraService()
					.getCanceladosSinAsignacion(model);

			logger.trace("Se clasificaron los cancelados:");
			for (Integer llave : canceladosClasificados.keySet()) {
				logger.trace("Para " + llave + " hay "
						+ canceladosClasificados.get(llave).size());
			}

			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.CANCELADOS_CLASIFICADOS,
							canceladosClasificados);

		} else {
			logger.warn("No se encontró bitacora en sesión");
		}
		return "traerCanceladosPendientes";
	}

	@SuppressWarnings("unchecked")
	public void validateMoverCancelados() {
		List<Cancelado> cancelados = null;
		cancelados = documentacionService
				.validarCanceladoSoloRegistrado(canceladoIdsSel);
		for (Cancelado cancelado : cancelados) {
			addActionError("El cancelado " + cancelado.getCdCancelado()
					+ " ya fue documentado, elija otro.");
		}
		areasDisponibles = (List<Area>) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.AREAS_DISPONIBLES);
		if (areasDisponibles == null || areasDisponibles.size() < 1) {
			addActionError("Hubo un problema al intentar obtener las areas, por favor, reinicie sesión.");
		} else {
			for (Area area : areasDisponibles) {
				if (idAreaSel.equals(area.getCdArea())) {
					if (area.getProblemas().size() < 1) {
						addActionError("No hay problemas para el área seleccionada "
								+ area.getNbArea()
								+ " por favor registre al menos uno antes de asignar cancelados a esta área");
					}
				}
			}
		}
	}

	/**
	 * Asigna o documenta por primera vez un grupo de cancelados. 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@Validations(

	requiredFields = { @RequiredFieldValidator(fieldName = "idAreaSel", message = "Seleccione un área", type = ValidatorType.SIMPLE) },

	expressions = {
			@ExpressionValidator(expression = "!canceladoIdsSel.isEmpty", message = "Debe seleccionar cancelados para mover"),
			@ExpressionValidator(expression = "canceladoIdPrincipalSel!=null", message = "No hay un cancelado principal seleccionado")

	})
	public String moverCancelados() {
		Documentacion documentacion = new Documentacion();
		Area areaSel = null;
		Bitacora bitacoraSel = null;
		Usuario usuarioSel = null;

		areasDisponibles = (List<Area>) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.AREAS_DISPONIBLES);
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		logger.trace("Se bajaron los datos de sesion");

		for (Area area : areasDisponibles) {
			if (idAreaSel.equals(area.getCdArea())) {
				areaSel = area;
			}
		}
		logger.trace("El área seleccionada es " + areaSel.getNbArea());

		documentacion.setCdBitacora(bitacoraSel.getCdBitacora());
		documentacion.setCdUsuario(usuarioSel.getCdUsuario());
		documentacion.setTmDocumentacion(new Date());
		documentacion
				.setTxImpacto(IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO);
		documentacion
				.setTxRecuperacion(IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO);
		documentacion
				.setTxSintoma(IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO);
		documentacion.setCdEstado(Estado.INSTRUCCIONES_SOLICITADAS);
		documentacion.setCdProblema(areaSel.getProblemas().get(0)
				.getCdProblema());
		logger.trace("Se llenaron los datos de la documentacion");
		if (moverComoGrupo != null) {
			documentacionService.guardarDocumentacionInicial(documentacion,
					canceladoIdPrincipalSel, canceladoIdsSel);
		} else {
			documentacionService.guardarDocumentacionIndividual(documentacion,
					canceladoIdsSel);
		}
		logger.trace("Se grabo la documentacion");
		return "moverCancelados";
	}

	@SuppressWarnings("unchecked")
	@RemoteMethod
	public List<Cancelado> traerCanceladosClasificadosDWR(
			Integer tipoCanceladosPendientes) {
		Map<Integer, List<Cancelado>> canceladosClasificados = null;
		List<Cancelado> canceladosPrincipales = null;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		if ((canceladosClasificados = (Map<Integer, List<Cancelado>>) req
				.getSession().getAttribute(
						NombreObjetoSesion.CANCELADOS_CLASIFICADOS)) != null) {
			logger.trace("Los cancelados solicitados son "
					+ tipoCanceladosPendientes);
			this.canceladosClasificados = canceladosClasificados
					.get(tipoCanceladosPendientes);
			canceladosPrincipales = new ArrayList<Cancelado>();
			for (Cancelado cancelado : this.canceladosClasificados) {
				if (cancelado.getCanceladoPrincipal() == null) {
					canceladosPrincipales.add(cancelado);
				}
			}
			logger.trace("Se encontraron " + this.canceladosClasificados.size()
					+ " cancelados");
		} else {
			logger.warn("No se encontró en sesion los cancelados clasficados.");
		}
		return canceladosPrincipales;
	}

	/**
	 * Cuando se arrastra un cancelado de un rubro de clasificación a otro.
	 * @param cancelado El cancelado principal
	 * @param documentacion La última documentación encontrada hasta el momento de ejecutar esta función
	 * @param tipoCanceladoDestino El rubro de documentación a donde se pretende mover el grupo de cancelados.
	 * @param idAreaDestino
	 * @return {@link CanceladoIdConverter}
	 */
	@RemoteMethod
	public Cancelado cambiarEstadoCanceladosDWR(Cancelado cancelado,
			Documentacion documentacion, Integer tipoCanceladoDestino,
			Integer idAreaDestino) {
		Cancelado canceladoActualizado = null;
		Integer siguienteEstado = null;
		Usuario usuario = null;
		logger.trace("En cambiarEstadoCanceladosDWR");
		logger.trace("Se trabajara el canceldo " + cancelado);
		logger.trace("con última documentación " + documentacion);
		logger.trace("con el area destino " + idAreaDestino);
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		usuario = (Usuario) req.getSession().getAttribute(
				NombreObjetoSesion.USUARIO);
		logger.trace("Con el usuario " + usuario);

		if ((getDocumentacionService().validarCanceladoDocumentable(
				documentacion, cancelado.getCanceladoIdPrincipal()) == null)
				&& (siguienteEstado = getDocumentacionService()
						.validarMovimientoCancelado(cancelado, documentacion,
								usuario, tipoCanceladoDestino, idAreaDestino)) != null) {
			canceladoActualizado = getDocumentacionService().moverCancelado(
					cancelado, documentacion, usuario, siguienteEstado,
					idAreaDestino, tipoCanceladoDestino);
		}

		return canceladoActualizado;
	}

	@Override
	public String edit() {
		logger.trace("Gestionando bitacora " + model);
		return super.edit();
	}

	public void validateUpdate() {
		if ((retornaCicloProductivo == null || !retornaCicloProductivo)) {
			logger.trace("No Se retornara ciclo productivo, el usuario que recibe "
					+ model.getCdUsuRecibe() + " su passwd " + passRecibe);
			if (ldapRecibe == null || ldapRecibe.matches("")) {
				addActionError(getText("cdLdapUsuarioRecibe"));
			}
			if ((passRecibe == null || passRecibe.matches(""))) {
				addActionError(getText("passwordRecibe"));
			}
		} else {
			if (getBitacoraService().validaBitacoraDelegada(model)) {
				logger.warn("La bitácora " + model + " ya fue delegada a las "
						+ model.getTmDelegacion());
				addActionError("La bitácora de "
						+ model.getEmpresa().getNbEmpresa() + " de la fecha "
						+ model.getFhCicloBitacora() + " fue delegada en "
						+ model.getTmDelegacion());
			}

		}
	}

	// FIXME Validar usuarios y contraseñas con el LDAP
	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "ldapAutoriza", key = "cdLdapUsuarioAutoriza", type = ValidatorType.SIMPLE),
			@RequiredStringValidator(fieldName = "passAutoriza", key = "passwordAutoriza") })
	public String update() {
		Bitacora bitacora = null;
		logger.trace("Actualizando la btiacora " + model);
		logger.trace("Se retornara ciclo? " + retornaCicloProductivo);
		if (retornaCicloProductivo) {
			bitacora = getBitacoraService()
					.retornaBitacora(model, ldapAutoriza);
		} else {
			bitacora = getBitacoraService().delegaBitacora(model, ldapAutoriza,
					ldapRecibe);
		}

		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.BITACORA, bitacora);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public void validate() {
		areasDisponibles = (List<Area>) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.AREAS_DISPONIBLES);
	}

	public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
		this.empresaSeleccionada = empresaSeleccionada;
	}

	public Empresa getEmpresaSeleccionada() {
		return empresaSeleccionada;
	}

	public String getJSON() {
		return SUCCESS;
	}

	public IBitacoraService getBitacoraService() {
		return (IBitacoraService) catalogoGenericoService;
	}

	@Override
	public Integer getPage() {
		return super.getPage();
	}

	public List<Cancelado> getCanceladosPendientes() {
		return canceladosPendientes;
	}

	public void setCanceladosPendientes(List<Cancelado> canceladosPendientes) {
		this.canceladosPendientes = canceladosPendientes;
	}

	public Integer getTipoCanceladosPendientes() {
		return tipoCanceladosPendientes;
	}

	public void setTipoCanceladosPendientes(Integer tipoCanceladosPendientes) {
		this.tipoCanceladosPendientes = tipoCanceladosPendientes;
	}

	public List<Cancelado> getCanceladosClasificados() {
		return canceladosClasificados;
	}

	public void setCanceladosClasificados(List<Cancelado> canceladosClasificados) {
		this.canceladosClasificados = canceladosClasificados;
	}

	public List<Area> getAreasDisponibles() {
		return areasDisponibles;
	}

	public void setAreasDisponibles(List<Area> areasDisponibles) {
		this.areasDisponibles = areasDisponibles;
	}

	public Integer getIdAreaSel() {
		return idAreaSel;
	}

	public void setIdAreaSel(Integer idAreaSel) {
		this.idAreaSel = idAreaSel;
	}

	public Boolean getMoverComoGrupo() {
		return moverComoGrupo;
	}

	public void setMoverComoGrupo(Boolean moverComoGrupo) {
		this.moverComoGrupo = moverComoGrupo;
	}

	public IDocumentacionService getDocumentacionService() {
		return documentacionService;
	}

	public void setDocumentacionService(
			IDocumentacionService documentacionService) {
		this.documentacionService = documentacionService;
	}

	public CanceladoId getCanceladoIdPrincipalSel() {
		return canceladoIdPrincipalSel;
	}

	public void setCanceladoIdPrincipalSel(CanceladoId canceladoIdPrincipalSel) {
		this.canceladoIdPrincipalSel = canceladoIdPrincipalSel;
	}

	public List<CanceladoId> getCanceladoIdsSel() {
		return canceladoIdsSel;
	}

	public void setCanceladoIdsSel(List<CanceladoId> canceladoIdsSel) {
		this.canceladoIdsSel = canceladoIdsSel;
	}

	public String getPassRecibe() {
		return passRecibe;
	}

	public void setPassRecibe(String passRecibe) {
		this.passRecibe = passRecibe;
	}

	public String getPassAutoriza() {
		return passAutoriza;
	}

	public void setPassAutoriza(String passAutoriza) {
		this.passAutoriza = passAutoriza;
	}

	public Boolean getRetornaCicloProductivo() {
		return retornaCicloProductivo;
	}

	public void setRetornaCicloProductivo(Boolean retornaCicloProductivo) {
		this.retornaCicloProductivo = retornaCicloProductivo;
	}

	public String getLdapRecibe() {
		return ldapRecibe;
	}

	public void setLdapRecibe(String ldapRecibe) {
		this.ldapRecibe = ldapRecibe;
	}

	public String getLdapAutoriza() {
		return ldapAutoriza;
	}

	public void setLdapAutoriza(String ldapAutoriza) {
		this.ldapAutoriza = ldapAutoriza;
	}

	public String getIdEmpresaSel() {
		return idEmpresaSel;
	}

	public void setIdEmpresaSel(String idEmpresaSel) {
		this.idEmpresaSel = idEmpresaSel;
	}

}

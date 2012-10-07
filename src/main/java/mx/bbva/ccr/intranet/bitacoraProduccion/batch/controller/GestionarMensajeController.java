package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IMensajeService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoMensaje;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "bitacora" }) })
public class GestionarMensajeController extends
		CatalogoGenericoController<Mensaje, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1704399203161710779L;
	private static Logger logger = Logger
			.getLogger(GestionarMensajeController.class);
	private IBitacoraService bitacoraService;
	private List<TipoMensaje> tipoMensajes;
	private Map<Integer, List<Mensaje>> mensajesVigentesClasificados;
	private Integer idTipoMensajeSel;
	private TipoMensaje tipoMensajeSel;

	@Inject
	public GestionarMensajeController(IMensajeService mensajeService) {
		super(mensajeService);
		entidad = "mensaje";
	}

	@Override
	public HttpHeaders index() {
		Bitacora bitacora;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		tipoMensajes = getMensajeService().getTipoMensajesHabilitados();
		mensajesVigentesClasificados = getMensajeService().getMensajesVigentes(
				bitacora);
		return new DefaultHttpHeaders("index").disableCaching();
	}

	@Override
	public String editNew() {
		logger.trace("El id del tipo de mensaje nuevo es " + idTipoMensajeSel);
		tipoMensajeSel = catalogoGenericoService.traerPorIdGenerico(
				TipoMensaje.class, idTipoMensajeSel);
		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.TIPO_MENSAJE, tipoMensajeSel);

		return super.editNew();
	}

	public void validateCreate() {
		tipoMensajeSel = (TipoMensaje) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.TIPO_MENSAJE);
		if (tipoMensajeSel == null) {
			addActionError("No se encontró en sesión el tipo de mensaje con el que se trabajara, reinicie sesión");
			logger.error("No se encontró el tipo de mensaje en sesión");
		}
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "txMensaje", key = "txDocumentacion", type = ValidatorType.SIMPLE) })
	@Override
	public HttpHeaders create() {
		Bitacora bitacora;
		Usuario usuario;
		HttpHeaders httpHeaders;

		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		usuario = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		tipoMensajeSel = (TipoMensaje) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.TIPO_MENSAJE);
		model.setCdTipoMsg(tipoMensajeSel.getId());
		model.setCdBitacora(bitacora.getId());
		model.setTmMensaje(new Date());
		model.setCdUsuario(usuario.getCdUsuario());
		logger.trace("Creando documentacion " + model);

		mensajesVigentesClasificados = getMensajeService().getMensajesVigentes(
				bitacora);

		httpHeaders = super.create();

		operacion = getText("operacionRegistro");
		idEntidad = model.getId().toString();
		addActionMessage(getText("operacionExitosa"));

		logger.trace("El mensaje creado es " + model);
		logger.trace("Su id de entidad es " + model.getId());

		return httpHeaders;
	}

	@Override
	public String edit() {
		logger.trace("El padre con el que se trabajara " + model);
		model.setTxMensaje(model.getUltimoMensajeSecundario() != null ? model
				.getUltimoMensajeSecundario().getTxMensaje() : model
				.getTxMensaje());
		return super.edit();
	}

	@Override
	public String update() {
		Usuario usuario;
		Mensaje mensajeSecundario;

		usuario = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);

		mensajeSecundario = new Mensaje();
		mensajeSecundario.setTxMensaje(model.getTxMensaje());
		mensajeSecundario.setCdBitacora(model.getCdBitacora());
		mensajeSecundario.setCdTipoMsg(model.getCdTipoMsg());
		mensajeSecundario.setCdMensajePri(model.getCdMensaje());
		mensajeSecundario.setTmMensaje(new Date());
		mensajeSecundario.setCdUsuario(usuario.getCdUsuario());
		logger.trace("El mensaje padre es " + model);
		logger.trace("El mensaje secundario que se guardara "
				+ mensajeSecundario);
		catalogoGenericoService.guardar(mensajeSecundario);

		operacion = getText("operacionEdicion");
		idEntidad = model.getId().toString();
		addActionMessage(getText("operacionExitosa"));
		return SUCCESS;
	}

	@Override
	public String deleteConfirm() {
		model.setTxMensaje(model.getUltimoMensajeSecundario() != null ? model
				.getUltimoMensajeSecundario().getTxMensaje() : model
				.getTxMensaje());
		return super.deleteConfirm();
	}

	@Override
	public String destroy() {
		model.setTmMsgBorrado(new Date());
		catalogoGenericoService.actualizar(model);

		operacion = getText("operacionEliminacion");
		idEntidad = model.getId().toString();
		addActionMessage(getText("operacionExitosa"));
		return SUCCESS;
	}

	public String confirmarCerrarMensaje() {
		model.setTxMensaje(model.getUltimoMensajeSecundario() != null ? model
				.getUltimoMensajeSecundario().getTxMensaje() : model
				.getTxMensaje());
		return "cerrar-mensaje";
	}

	public String cerrarMensaje() {
		model = catalogoGenericoService.traerPorId(idSel);
		model.setTmMensajeCerrado(new Date());
		logger.trace("El mensaje que se cerrara" + model);
		catalogoGenericoService.actualizar(model);

		operacion = getText("operacionCerrar");
		idEntidad = model.getId().toString();
		addActionMessage(getText("operacionExitosa"));
		return SUCCESS;
	}

	public void validate() {
		Bitacora bitacoraSel;
		Usuario usuarioSel;
		bitacoraSel = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		usuarioSel = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
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

	}

	public IMensajeService getMensajeService() {
		return (IMensajeService) catalogoGenericoService;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

	public Map<Integer, List<Mensaje>> getMensajesVigentesClasificados() {
		return mensajesVigentesClasificados;
	}

	public void setMensajesVigentesClasificados(
			Map<Integer, List<Mensaje>> mensajesVigentesClasificados) {
		this.mensajesVigentesClasificados = mensajesVigentesClasificados;
	}

	public List<TipoMensaje> getTipoMensajes() {
		return tipoMensajes;
	}

	public void setTipoMensajes(List<TipoMensaje> tipoMensajes) {
		this.tipoMensajes = tipoMensajes;
	}

	public Integer getIdTipoMensajeSel() {
		return idTipoMensajeSel;
	}

	public void setIdTipoMensajeSel(Integer idTipoMensajeSel) {
		this.idTipoMensajeSel = idTipoMensajeSel;
	}

	public TipoMensaje getTipoMensajeSel() {
		return tipoMensajeSel;
	}

	public void setTipoMensajeSel(TipoMensaje tipoMensajeSel) {
		this.tipoMensajeSel = tipoMensajeSel;
	}

}

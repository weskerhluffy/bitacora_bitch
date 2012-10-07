package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionHoldeadoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.HoldeadoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "bitacora" }) })
public class DocumentacionHoldeadoController extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3640941195539703174L;
	private static Logger logger = Logger
			.getLogger(DocumentacionHoldeadoController.class);
	private IDocumentacionHoldeadoService documentacionHoldeadoService;
	private IBitacoraService bitacoraService;

	private HoldeadoId holdeadoIdSel;
	private List<Holdeado> holdeadosVigentes;
	private Holdeado holdeado;
	private DocumentacionHoldeado model;

	public String index() {
		Bitacora bitacora;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);

		holdeadosVigentes = getDocumentacionHoldeadoService()
				.getHoldeadosVigentes(bitacora);
		logger.trace("Se encontraron " + holdeadosVigentes.size()
				+ " holdeados");
		for (Holdeado holdeado : holdeadosVigentes) {
			logger.trace("Holdeado " + holdeado);
			logger.trace("Con documentaciones ");
			for (DocumentacionHoldeado documentacionHoldeado : holdeado
					.getDocumentacionHoldeados()) {
				logger.trace("Documentacion " + documentacionHoldeado);
			}
		}
		return "index";
	}

	public String editNew() {
		logger.trace("Sacando las documentaciones de " + holdeadoIdSel);
		holdeado = getDocumentacionHoldeadoService().traerPorIdGenerico(
				Holdeado.class, holdeadoIdSel);
		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.HOLDEADO, holdeado);
		return "editNew";
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "model.txDocumentacion", key = "txDocumentacion", type = ValidatorType.SIMPLE) })
	public String create() {
		Bitacora bitacora;
		Usuario usuario;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		holdeado = (Holdeado) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.HOLDEADO);
		usuario = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		model.setCdUsuario(usuario.getCdUsuario());
		model.setCdBitacora(bitacora.getCdBitacora());
		getDocumentacionHoldeadoService().documentarHoldeado(holdeado, model);
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

	public Holdeado getHoldeado() {
		if (holdeado == null) {
			holdeado = (Holdeado) ActionContext.getContext().getSession()
					.get(NombreObjetoSesion.HOLDEADO);
		}
		return holdeado;
	}

	public IDocumentacionHoldeadoService getDocumentacionHoldeadoService() {
		return documentacionHoldeadoService;
	}

	public List<Holdeado> getHoldeadosVigentes() {
		return holdeadosVigentes;
	}

	public void setHoldeadosVigentes(List<Holdeado> holdeadosVigentes) {
		this.holdeadosVigentes = holdeadosVigentes;
	}

	public void setDocumentacionHoldeadoService(
			IDocumentacionHoldeadoService documentacionHoldeadoService) {
		this.documentacionHoldeadoService = documentacionHoldeadoService;
	}

	public HoldeadoId getHoldeadoIdSel() {
		return holdeadoIdSel;
	}

	public void setHoldeadoIdSel(HoldeadoId holdeadoIdSel) {
		this.holdeadoIdSel = holdeadoIdSel;
	}

	public void setHoldeado(Holdeado holdeado) {
		this.holdeado = holdeado;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

	public DocumentacionHoldeado getModel() {
		return model;
	}

	public void setModel(DocumentacionHoldeado model) {
		this.model = model;
	}

}

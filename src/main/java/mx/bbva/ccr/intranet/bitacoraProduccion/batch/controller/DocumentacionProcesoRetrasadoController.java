package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionProcesoRetrasadoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionProcesoRetrasado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;
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
public class DocumentacionProcesoRetrasadoController extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3640941195539703174L;
	private static Logger logger = Logger
			.getLogger(DocumentacionProcesoRetrasadoController.class);
	private IDocumentacionProcesoRetrasadoService documentacionProcesoRetrasadoService;
	private IBitacoraService bitacoraService;

	private List<ProcesoRetrasado> procesoRetrasadosVigentes;
	private List<Integer> idsProcesosRetrasadosSel;
	private List<ProcesoRetrasado> procesoRetrasadosSel;
	private DocumentacionProcesoRetrasado model;
	private Boolean cerrarDocumentacionProcesoRetrasado;
	private String operacion;
	private String idEntidad;
	protected String entidad;

	public String index() {
		Bitacora bitacora;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);

		procesoRetrasadosVigentes = getDocumentacionProcesoRetrasadoService()
				.getProcesoRetrasadosVigentes(bitacora);
		logger.trace("Se encontraron " + procesoRetrasadosVigentes.size()
				+ " procesoRetrasados");
		return "index";
	}

	public String editNew() {
		logger.trace("Sacando las documentaciones de "
				+ idsProcesosRetrasadosSel);
		Collections.sort(idsProcesosRetrasadosSel);
		procesoRetrasadosSel = getDocumentacionProcesoRetrasadoService()
				.traerPorIds(ProcesoRetrasado.class, idsProcesosRetrasadosSel);
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.IDS_PROCESOS_RETRASADOS,
						idsProcesosRetrasadosSel);
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.PROCESOS_RETRASADOS,
						procesoRetrasadosSel);
		return "editNew";
	}

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a lista de enteros
	public void validateCreate() {
		List<ProcesoRetrasado> procesoRetrasadosYaDocumentados;
		List<Integer> idProcesosRetrasadosYaDocumentados;
		idsProcesosRetrasadosSel = (List<Integer>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.IDS_PROCESOS_RETRASADOS);
		if (idsProcesosRetrasadosSel != null) {
			idProcesosRetrasadosYaDocumentados = new ArrayList<Integer>();
			if ((procesoRetrasadosYaDocumentados = getDocumentacionProcesoRetrasadoService()
					.validaDocumentarProcesosRetrasados(
							idsProcesosRetrasadosSel)).size() > 0) {
				for (ProcesoRetrasado procesoRetrasado : procesoRetrasadosYaDocumentados) {
					idProcesosRetrasadosYaDocumentados.add(procesoRetrasado
							.getCdProRet());
				}
				idEntidad = CcrUtil.implode(idProcesosRetrasadosYaDocumentados,
						",");
				addActionError(getText("procesosYaDocumentados"));
			}
		}

	}

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a lista de enteros
	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "model.txDocumentacion", key = "txDocumentacion", type = ValidatorType.SIMPLE) })
	public String create() {
		Bitacora bitacora;
		Usuario usuario;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		usuario = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		idsProcesosRetrasadosSel = (List<Integer>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.IDS_PROCESOS_RETRASADOS);

		logger.trace("Se va a cerrar la documentacion "
				+ cerrarDocumentacionProcesoRetrasado);
		model.setCdUsuario(usuario.getCdUsuario());
		model.setCdBitacora(bitacora.getCdBitacora());
		getDocumentacionProcesoRetrasadoService().documentarProcesoRetrasado(
				idsProcesosRetrasadosSel, model,
				cerrarDocumentacionProcesoRetrasado);

		entidad = "procesos retrasados";
		operacion = getText("operacionDocumentar");
		Collections.sort(idsProcesosRetrasadosSel);
		idEntidad = CcrUtil.implode(idsProcesosRetrasadosSel, ",");
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

	public IDocumentacionProcesoRetrasadoService getDocumentacionProcesoRetrasadoService() {
		return documentacionProcesoRetrasadoService;
	}

	public List<ProcesoRetrasado> getProcesoRetrasadosVigentes() {
		return procesoRetrasadosVigentes;
	}

	public void setProcesoRetrasadosVigentes(
			List<ProcesoRetrasado> procesoRetrasadosVigentes) {
		this.procesoRetrasadosVigentes = procesoRetrasadosVigentes;
	}

	public void setDocumentacionProcesoRetrasadoService(
			IDocumentacionProcesoRetrasadoService documentacionProcesoRetrasadoService) {
		this.documentacionProcesoRetrasadoService = documentacionProcesoRetrasadoService;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

	public DocumentacionProcesoRetrasado getModel() {
		return model;
	}

	public void setModel(DocumentacionProcesoRetrasado model) {
		this.model = model;
	}

	public Boolean getCerrarDocumentacionProcesoRetrasado() {
		return cerrarDocumentacionProcesoRetrasado;
	}

	public void setCerrarDocumentacionProcesoRetrasado(
			Boolean cerrarDocumentacionProcesoRetrasado) {
		this.cerrarDocumentacionProcesoRetrasado = cerrarDocumentacionProcesoRetrasado;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(String idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public List<Integer> getIdsProcesosRetrasadosSel() {
		return idsProcesosRetrasadosSel;
	}

	public void setIdsProcesosRetrasadosSel(
			List<Integer> idsProcesosRetrasadosSel) {
		this.idsProcesosRetrasadosSel = idsProcesosRetrasadosSel;
	}

	public List<ProcesoRetrasado> getProcesoRetrasadosSel() {
		return procesoRetrasadosSel;
	}

	public void setProcesoRetrasadosSel(
			List<ProcesoRetrasado> procesoRetrasadosSel) {
		this.procesoRetrasadosSel = procesoRetrasadosSel;
	}

}

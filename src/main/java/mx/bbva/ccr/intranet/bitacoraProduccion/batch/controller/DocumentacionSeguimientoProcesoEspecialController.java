package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

//import java.util.Date;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.SeguimientoProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionSeguimientoProcesoEspecialService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecialId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.ConversionType;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "bitacora" }) })
public class DocumentacionSeguimientoProcesoEspecialController extends
		ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3271619854788803453L;

	private static Logger logger = Logger
			.getLogger(DocumentacionSeguimientoProcesoEspecialController.class);
	private IDocumentacionSeguimientoProcesoEspecialService documentacionSeguimientoProcesoEspecialService;
	private IBitacoraService bitacoraService;
	private SeguimientoProcesoEspecialId seguimientoProcesoEspecialIdSel;

	private List<SeguimientoProcesoEspecial> seguimientoProcesoEspecialsVigentes;
	private SeguimientoProcesoEspecial seguimientoProcesoEspecial;
	private Boolean cerrarDocumentacionSeguimientoProcesoEspecial;
	private DocumentacionSeguimientoProcesoEspecial model;
	private String operacion;
	private String idEntidad;
	protected String entidad;
	private SeguimientoProcesoEspecialDao seguimientoProcesoEspecialDao;
	private Date fechaSel;
	private Boolean usarODate;
	private Integer tipoProcesoEspecialSel;

	public String index() {
		Bitacora bitacora;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);

		if (usarODate == null) {
			usarODate = false;
		}
		if (fechaSel == null) {
			fechaSel = new Date();
		}
		if (tipoProcesoEspecialSel == null) {
			tipoProcesoEspecialSel = TipoProcesoEspecial.HITO;
		}
		seguimientoProcesoEspecialsVigentes = getDocumentacionSeguimientoProcesoEspecialService()
				.getSeguimientoProcesosEspeciales(bitacora, fechaSel,
						usarODate, tipoProcesoEspecialSel);

		logger.trace("Se encontraron "
				+ seguimientoProcesoEspecialsVigentes.size()
				+ " procesoEspecials");
		for (SeguimientoProcesoEspecial seguimientoProcesoEspecial : seguimientoProcesoEspecialsVigentes) {
			logger.trace("SeguimientoProcesoEspecial "
					+ seguimientoProcesoEspecial);
			logger.trace("Con documentaciones ");
			for (DocumentacionSeguimientoProcesoEspecial documentacionSeguimientoProcesoEspecial : seguimientoProcesoEspecial
					.getDocumentacionSeguimientoProcesoEspecials()) {
				logger.trace("Documentacion "
						+ documentacionSeguimientoProcesoEspecial);
			}
		}
		return "index";
	}

	public String editNew() {
		logger.trace("Sacando las documentaciones de "
				+ seguimientoProcesoEspecialIdSel);
		seguimientoProcesoEspecial = getDocumentacionSeguimientoProcesoEspecialService()
				.traerPorIdGenerico(SeguimientoProcesoEspecial.class,
						seguimientoProcesoEspecialIdSel);
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.SEGUIMIENTO_PROCESO_ESPECIAL,
						seguimientoProcesoEspecial);
		return "editNew";
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "model.txDocumentacion", key = "txDocumentacion", type = ValidatorType.SIMPLE) })
	public String create() {
		Bitacora bitacora;
		Usuario usuario;
		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		usuario = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		seguimientoProcesoEspecial = (SeguimientoProcesoEspecial) ActionContext
				.getContext().getSession()
				.get(NombreObjetoSesion.SEGUIMIENTO_PROCESO_ESPECIAL);

		logger.trace("Se va a cerrar la documentacion "
				+ cerrarDocumentacionSeguimientoProcesoEspecial);
		model.setCdBitacora(bitacora.getCdBitacora());
		model.setCdUsuario(usuario.getCdUsuario());
		getDocumentacionSeguimientoProcesoEspecialService()
				.documentarSeguimientoProcesoEspecial(
						seguimientoProcesoEspecial, model);

		entidad = "seguimiento proceso especial";
		operacion = getText("operacionDocumentar");
		idEntidad = seguimientoProcesoEspecial.getId().getCdEmpresa() + ","
				+ seguimientoProcesoEspecial.getId().getNbProceso() + ","
				+ seguimientoProcesoEspecial.getId().getNbGrupo() + ","
				+ seguimientoProcesoEspecial.getId().getFhCiclo();
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
			addActionError("La sesion expiró, por favor salgase y vuelva a entrar.");
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

	public SeguimientoProcesoEspecial getSeguimientoProcesoEspecial() {
		if (seguimientoProcesoEspecial == null) {
			seguimientoProcesoEspecial = (SeguimientoProcesoEspecial) ActionContext
					.getContext().getSession()
					.get(NombreObjetoSesion.SEGUIMIENTO_PROCESO_ESPECIAL);
		}
		return seguimientoProcesoEspecial;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.TiempoConverter", type = ConversionType.CLASS)
	public void setFechaSel(Date fechaSel) {
		this.fechaSel = fechaSel;
	}

	public IDocumentacionSeguimientoProcesoEspecialService getDocumentacionSeguimientoProcesoEspecialService() {
		return documentacionSeguimientoProcesoEspecialService;
	}

	public void setDocumentacionSeguimientoProcesoEspecialService(
			IDocumentacionSeguimientoProcesoEspecialService documentacionSeguimientoProcesoEspecialService) {
		this.documentacionSeguimientoProcesoEspecialService = documentacionSeguimientoProcesoEspecialService;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

	public List<SeguimientoProcesoEspecial> getSeguimientoProcesoEspecialsVigentes() {
		return seguimientoProcesoEspecialsVigentes;
	}

	public void setSeguimientoProcesoEspecialsVigentes(
			List<SeguimientoProcesoEspecial> seguimientoProcesoEspecialsVigentes) {
		this.seguimientoProcesoEspecialsVigentes = seguimientoProcesoEspecialsVigentes;
	}

	public void setSeguimientoProcesoEspecial(
			SeguimientoProcesoEspecial seguimientoProcesoEspecial) {
		this.seguimientoProcesoEspecial = seguimientoProcesoEspecial;
	}

	public Boolean getCerrarDocumentacionSeguimientoProcesoEspecial() {
		return cerrarDocumentacionSeguimientoProcesoEspecial;
	}

	public void setCerrarDocumentacionSeguimientoProcesoEspecial(
			Boolean cerrarDocumentacionSeguimientoProcesoEspecial) {
		this.cerrarDocumentacionSeguimientoProcesoEspecial = cerrarDocumentacionSeguimientoProcesoEspecial;
	}

	public DocumentacionSeguimientoProcesoEspecial getModel() {
		return model;
	}

	public void setModel(DocumentacionSeguimientoProcesoEspecial model) {
		this.model = model;
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

	public SeguimientoProcesoEspecialDao getSeguimientoProcesoEspecialDao() {
		return seguimientoProcesoEspecialDao;
	}

	public void setSeguimientoProcesoEspecialDao(
			SeguimientoProcesoEspecialDao seguimientoProcesoEspecialDao) {
		this.seguimientoProcesoEspecialDao = seguimientoProcesoEspecialDao;
	}

	public SeguimientoProcesoEspecialId getSeguimientoProcesoEspecialIdSel() {
		return seguimientoProcesoEspecialIdSel;
	}

	public void setSeguimientoProcesoEspecialIdSel(
			SeguimientoProcesoEspecialId seguimientoProcesoEspecialIdSel) {
		this.seguimientoProcesoEspecialIdSel = seguimientoProcesoEspecialIdSel;
	}

	public Date getFechaSel() {
		return fechaSel;
	}

	public Boolean getUsarODate() {
		return usarODate;
	}

	public void setUsarODate(Boolean usarODate) {
		this.usarODate = usarODate;
	}

	public Integer getTipoProcesoEspecialSel() {
		return tipoProcesoEspecialSel;
	}

	public void setTipoProcesoEspecialSel(Integer tipoProcesoEspecialSel) {
		this.tipoProcesoEspecialSel = tipoProcesoEspecialSel;
	}

}

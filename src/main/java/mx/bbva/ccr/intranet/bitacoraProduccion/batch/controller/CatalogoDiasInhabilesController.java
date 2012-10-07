package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDiaInhabilService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabilId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.directwebremoting.annotations.RemoteProxy;

import com.opensymphony.xwork2.ActionContext;

@RemoteProxy
@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "catalogo-dias-inhabiles" }) })
public class CatalogoDiasInhabilesController extends
		CatalogoGenericoController<DiaInhabil, DiaInhabilId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1584874117910738299L;
	private List<String> fechasSel;
	private List<String> fechasOrg;
	private String empSel;

	@Inject
	public CatalogoDiasInhabilesController(IDiaInhabilService diaInhabilService) {
		super(diaInhabilService);
		logger.trace("Se creo el controlador DiaIhabil");
	}

	@Override
	public HttpHeaders index() {
		Usuario usuario;
		Empresa empresa;
		if (empSel == null) {
			usuario = (Usuario) ActionContext.getContext().getSession()
					.get(NombreObjetoSesion.USUARIO);
			empresa = catalogoGenericoService.traerPorIdGenerico(Empresa.class,
					usuario.getEmpresas().get(0).getCdEmpresa());
		} else {
			empresa = catalogoGenericoService.traerPorIdGenerico(Empresa.class,
					empSel);
		}
		list = empresa.getDiasInhabiles();

		return new DefaultHttpHeaders("index").disableCaching();
	}

	@Override
	public HttpHeaders create() {
		Usuario usuario;

		getDiaInhabilService()
				.agregarFechaInhabil(empSel, fechasOrg, fechasSel);
		ActionContext
				.getContext()
				.getSession()
				.put(NombreObjetoSesion.EMPRESAS_DISPONIBLES,
						catalogoGenericoService
								.traerTodosGenerico(Empresa.class));
		usuario = (Usuario) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.USUARIO);
		usuario = catalogoGenericoService.traerPorIdGenerico(Usuario.class,
				usuario.getId());
		ActionContext.getContext().getSession()
				.put(NombreObjetoSesion.USUARIO, usuario);
		return new DefaultHttpHeaders("success").setLocationId(model.getId());
	}

	public IDiaInhabilService getDiaInhabilService() {
		return (IDiaInhabilService) catalogoGenericoService;
	}

	public List<String> getFechasSel() {
		return fechasSel;
	}

	public void setFechasSel(List<String> fechasSel) {
		this.fechasSel = fechasSel;
	}

	public List<String> getFechasOrg() {
		return fechasOrg;
	}

	public void setFechasOrg(List<String> fechasOrg) {
		this.fechasOrg = fechasOrg;
	}

	public String getEmpSel() {
		return empSel;
	}

	public void setEmpSel(String empSel) {
		this.empSel = empSel;
	}

}

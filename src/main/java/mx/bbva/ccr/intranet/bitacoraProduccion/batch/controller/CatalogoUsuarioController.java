package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IUsuarioService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Perfil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteProxy;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

//TODO Ponerle accesos a empresas
@RemoteProxy
@Named
@Results({ @Result(name = "success", type = "redirectAction", params = {
		"actionName", "catalogo-usuario" }) })
public class CatalogoUsuarioController extends
		CatalogoGenericoController<Usuario, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7766320502282608749L;
	private Boolean usuarioHabilitado;
	private Integer perfilSel;
	private List<Integer> permisosSel;
	private List<String> empresasSel;
	private List<Perfil> perfilesDisponibles;
	private List<Permiso> permisosDisponibles;
	private List<Area> areasDisponibles;
	private List<Empresa> empresasDisponibles;

	@Inject
	public CatalogoUsuarioController(IUsuarioService usuarioService) {
		super(usuarioService);
	}

	@Override
	public HttpHeaders index() {
		HttpHeaders httpHeaders;
		List<Usuario> usuarios;
		httpHeaders = super.index();
		usuarios = new ArrayList<Usuario>();
		for (Usuario usuario : list) {
			if (usuario.getTmUsuarioElim() == null) {
				usuarios.add(usuario);
			}
		}
		list = usuarios;

		return httpHeaders;
	}

	@Override
	public String editNew() {
		return super.editNew();
	}

	public void validateCreate() {
		if (getUsuarioService().validarLdapUnico(model)) {
			addActionError(getText("validarLdapUnico"));
		}
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "cdLdap", type = ValidatorType.FIELD, key = "cdLdap"),
			@RequiredStringValidator(fieldName = "txNombre", type = ValidatorType.FIELD, key = "txNombre"),
			@RequiredStringValidator(fieldName = "txAp", type = ValidatorType.FIELD, key = "txAp"),
			@RequiredStringValidator(fieldName = "txAm", type = ValidatorType.FIELD, key = "txAm") },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "permisosSel", type = ValidatorType.FIELD, key = "permisosSel"),
			@RequiredFieldValidator(fieldName = "empresasSel", type = ValidatorType.FIELD, key = "empresasSel"),
			@RequiredFieldValidator(fieldName = "cdPerfil", type = ValidatorType.FIELD, key = "cdPerfil"),
			@RequiredFieldValidator(fieldName = "cdArea", type = ValidatorType.FIELD, key = "cdArea") })
	@Override
	public HttpHeaders create() {
		getUsuarioService().agregarUsuario(model, permisosSel, empresasSel);
		return new DefaultHttpHeaders("success").setLocationId(model.getId());
	}

	@Override
	public String edit() {
		permisosSel = new ArrayList<Integer>();
		for (Permiso permiso : model.getPermisos()) {
			permisosSel.add(permiso.getId());
		}
		empresasSel = new ArrayList<String>();
		for (Empresa empresa : model.getEmpresas()) {
			empresasSel.add(empresa.getCdEmpresa());
		}
		return super.edit();
	}

	public void validateUpdate() {
		if (getUsuarioService().validarLdapUnico(model)) {
			addActionError(getText("validarLdapUnico"));
		}
	}

	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "txNombre", type = ValidatorType.FIELD, key = "txNombre"),
			@RequiredStringValidator(fieldName = "txAp", type = ValidatorType.FIELD, key = "txAp"),
			@RequiredStringValidator(fieldName = "txAm", type = ValidatorType.FIELD, key = "txAm") },

	requiredFields = {
			@RequiredFieldValidator(fieldName = "permisosSel", type = ValidatorType.FIELD, key = "permisosSel"),
			@RequiredFieldValidator(fieldName = "empresasSel", type = ValidatorType.FIELD, key = "empresasSel"),
			@RequiredFieldValidator(fieldName = "cdPerfil", type = ValidatorType.FIELD, key = "cdPerfil"),
			@RequiredFieldValidator(fieldName = "cdArea", type = ValidatorType.FIELD, key = "cdArea") })
	@Override
	public String update() {
		model.setTmUsuarioElim(usuarioHabilitado != null && usuarioHabilitado ? null
				: model.getTmUsuarioElim());
		getUsuarioService().actualizarUsuario(model, permisosSel, empresasSel);
		
		return SUCCESS;
	}

	@Override
	public String destroy() {
		model.setTmUsuarioElim(new Date());
		catalogoGenericoService.actualizar(model);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public List<Permiso> traerPermisosDWR(Integer cdPerfilSel) {
		List<Permiso> permisos = null;
		Perfil perfilSel = null;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest req = ctx.getHttpServletRequest();
		perfilesDisponibles = (List<Perfil>) req.getSession().getAttribute(
				NombreObjetoSesion.PERFILES_DISPONIBLES);
		logger.trace("en traerPermisosDWR se encontraron "
				+ perfilesDisponibles + " perfiles");
		logger.trace("Buscando entre ellos " + cdPerfilSel);
		if (perfilesDisponibles != null) {
			for (Perfil perfil : perfilesDisponibles) {
				if (perfil.getCdPerfil().equals(cdPerfilSel)) {
					perfilSel = perfil;
					break;
				}
			}
			if (perfilSel != null) {
				permisos = perfilSel.getPermisos();
			}
		}
		return permisos;
	}

	@SuppressWarnings("unchecked")
	public List<Permiso> getPermisosDisponibles() {

		permisosDisponibles = (List<Permiso>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.PERMISOS_DISPONIBLES);
		if (permisosDisponibles == null) {
			permisosDisponibles = catalogoGenericoService
					.traerTodosGenerico(Permiso.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.PERMISOS_DISPONIBLES,
							permisosDisponibles);
		}
		return permisosDisponibles;
	}

	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfilesDisponibles() {

		perfilesDisponibles = (List<Perfil>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.PERFILES_DISPONIBLES);
		if (perfilesDisponibles == null) {
			perfilesDisponibles = catalogoGenericoService
					.traerTodosGenerico(Perfil.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.PERFILES_DISPONIBLES,
							perfilesDisponibles);
		}
		return perfilesDisponibles;
	}

	@SuppressWarnings("unchecked")
	public List<Area> getAreasDisponibles() {
		areasDisponibles = (List<Area>) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.AREAS_DISPONIBLES);
		if (areasDisponibles == null) {
			areasDisponibles = catalogoGenericoService
					.traerTodosGenerico(Area.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.AREAS_DISPONIBLES, areasDisponibles);
		}
		return areasDisponibles;
	}

	@SuppressWarnings("unchecked")
	public List<Empresa> getEmpresasDisponibles() {
		empresasDisponibles = (List<Empresa>) ActionContext.getContext()
				.getSession().get(NombreObjetoSesion.EMPRESAS_DISPONIBLES);
		if (empresasDisponibles == null) {
			empresasDisponibles = catalogoGenericoService
					.traerTodosGenerico(Empresa.class);
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.EMPRESAS_DISPONIBLES,
							empresasDisponibles);
		}
		return empresasDisponibles;
	}

	public void setPerfilesDisponibles(List<Perfil> perfilesDisponibles) {
		this.perfilesDisponibles = perfilesDisponibles;
	}

	public void setPermisosDisponibles(List<Permiso> permisosDisponibles) {
		this.permisosDisponibles = permisosDisponibles;
	}

	public Integer getPerfilSel() {
		return perfilSel;
	}

	public void setPerfilSel(Integer perfilSel) {
		this.perfilSel = perfilSel;
	}

	public List<Integer> getPermisosSel() {
		return permisosSel;
	}

	public void setPermisosSel(List<Integer> permisosSel) {
		this.permisosSel = permisosSel;
	}

	public IUsuarioService getUsuarioService() {
		return (IUsuarioService) catalogoGenericoService;
	}

	public void setAreasDisponibles(List<Area> areasDisponibles) {
		this.areasDisponibles = areasDisponibles;
	}

	public Boolean getUsuarioHabilitado() {
		return usuarioHabilitado;
	}

	public void setUsuarioHabilitado(Boolean usuarioHabilitado) {
		this.usuarioHabilitado = usuarioHabilitado;
	}

	public List<String> getEmpresasSel() {
		return empresasSel;
	}

	public void setEmpresasSel(List<String> empresasSel) {
		this.empresasSel = empresasSel;
	}

	public void setEmpresasDisponibles(List<Empresa> empresasDisponibles) {
		this.empresasDisponibles = empresasDisponibles;
	}

}

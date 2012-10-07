package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IUsuarioService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("struts-default")
@Named
public class LoginController extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8364705521418005702L;
	private Logger logger = Logger.getLogger(LoginController.class);
	private String login;
	private IUsuarioService usuarioService;

	public void validateCreate() {
		Usuario usuarioEjemplo;
		List<Usuario> usuariosEncontrados;
		logger.trace("Validando entrada");
		usuarioEjemplo = new Usuario();
		usuarioEjemplo.setCdLdap(login);
		usuariosEncontrados = usuarioService
				.encontrarPorInstanciaEjemplo(usuarioEjemplo);
		if (usuariosEncontrados.size() != 1) {
			addActionError(getText("usuarioNoValido"));
		} else {
			ActionContext
					.getContext()
					.getSession()
					.put(NombreObjetoSesion.USUARIO, usuariosEncontrados.get(0));
		}
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "login", type = ValidatorType.SIMPLE, key = "login") })
	public String create() {
		logger.trace("Ingresando al sistema con el usuario "
				+ ActionContext.getContext().getSession()
						.get(NombreObjetoSesion.USUARIO));
		return "success";
	}

	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "login", type = ValidatorType.SIMPLE, key = "login") })
	public String index() {
		return create();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public IUsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}

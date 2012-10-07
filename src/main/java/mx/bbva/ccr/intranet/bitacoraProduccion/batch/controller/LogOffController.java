package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

/**import java.util.List;*/

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
/**import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;*/

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("struts-default")
@Named
public class LogOffController extends ActionSupport {

	
	private static final long serialVersionUID = 8364123521418005702L;
	private Logger logger = Logger.getLogger(LogOffController.class);

public String create() {
		logger.trace("Salida del usuario "
				+ ActionContext.getContext().getSession().remove(NombreObjetoSesion.USUARIO));
		return "success";
	}

	public String index() {
		return create();
	}
}
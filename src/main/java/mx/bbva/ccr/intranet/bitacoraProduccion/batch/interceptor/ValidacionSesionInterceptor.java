package mx.bbva.ccr.intranet.bitacoraProduccion.batch.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.log4j.Logger;
import org.apache.struts2.rest.RestActionInvocation;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ValidacionSesionInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3821820221604600931L;
	private IBitacoraService bitacoraService = null;
	private Logger logger = Logger.getLogger(ValidacionSesionInterceptor.class);

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocacion) throws Exception {
		logger.trace("Interceptando");
		Map<String, Object> session = ActionContext.getContext().getSession();

		if (session.get(NombreObjetoSesion.EMPRESAS_DISPONIBLES) == null) {
			List<Empresa> empresasDisponibles=new ArrayList<Empresa>();
			List<Empresa> empresasFiltradas=new ArrayList<Empresa>();
			empresasDisponibles=bitacoraService.traerTodosGenerico(Empresa.class);
			for(Empresa empresa:empresasDisponibles){
				if(empresa.getTmEmpresaElim()==null){
					empresasFiltradas.add(empresa);
				}
			}
			session.put(NombreObjetoSesion.EMPRESAS_DISPONIBLES,
					empresasFiltradas);
		}

		if (session.get(NombreObjetoSesion.USUARIO) != null) {
			logger.trace("El usuario no es nulo, se redireccionará "
					+ ((RestActionInvocation) invocacion).getProxy()
							.getMethod());
			return invocacion.invoke();
		} else {
			logger.trace("No hay usuario en sesion");
			return "login";
		}
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

}

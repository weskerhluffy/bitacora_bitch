package mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller;

import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.ICargaDatosService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.NombreObjetoSesion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CargaDatos;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Named
public class MostrarCargasController extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -786122434983391337L;

	private ICargaDatosService cargaDatosService;

	private List<CargaDatos> list;

	public String index() {
		Bitacora bitacora;

		bitacora = (Bitacora) ActionContext.getContext().getSession()
				.get(NombreObjetoSesion.BITACORA);
		list = cargaDatosService.getStatusUltimasCargas(bitacora);
		return "index";
	}

	public ICargaDatosService getCargaDatosService() {
		return cargaDatosService;
	}

	public void setCargaDatosService(ICargaDatosService cargaDatosService) {
		this.cargaDatosService = cargaDatosService;
	}

	public List<CargaDatos> getList() {
		return list;
	}

	public void setList(List<CargaDatos> list) {
		this.list = list;
	}

}

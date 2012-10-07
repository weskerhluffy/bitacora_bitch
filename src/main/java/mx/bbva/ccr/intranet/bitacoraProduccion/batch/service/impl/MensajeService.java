package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.MensajeDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IMensajeService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.TipoMensaje;

@Singleton
@Named("mensajeService")
public class MensajeService extends GenericoService<Mensaje, Integer> implements
		IMensajeService {

	@Transactional
	@Override
	public Map<Integer, List<Mensaje>> getMensajesVigentes(Bitacora bitacora) {
		List<Mensaje> mensajes;
		Map<Integer, List<Mensaje>> mensajesVigentesClasificados = null;
		mensajes = getMensajeDao().getMensajesVigentes(bitacora);
		mensajesVigentesClasificados = new HashMap<Integer, List<Mensaje>>();
		for (Mensaje mensaje : mensajes) {
			if (mensajesVigentesClasificados.get(mensaje.getCdTipoMsg()) == null) {
				mensajesVigentesClasificados.put(mensaje.getCdTipoMsg(),
						new ArrayList<Mensaje>());
			}
			mensajesVigentesClasificados.get(mensaje.getCdTipoMsg()).add(
					mensaje);
		}
		return mensajesVigentesClasificados;
	}

	@Transactional
	@Override
	public List<TipoMensaje> getTipoMensajesHabilitados() {
		List<TipoMensaje> tipoMensajes = null;

		tipoMensajes = new ArrayList<TipoMensaje>();
		for (TipoMensaje tipoMensaje : traerTodosGenerico(TipoMensaje.class)) {
			if (tipoMensaje.getTmTipoMsgElim() == null) {
				tipoMensajes.add(tipoMensaje);
			}
		}
		return tipoMensajes;
	}

	public void setMensajeDao(MensajeDao mensajeDao) {
		setEntidadDao(mensajeDao);
	}

	public MensajeDao getMensajeDao() {
		return (MensajeDao) entidadDao;
	}

}

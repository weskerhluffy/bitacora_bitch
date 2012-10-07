package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.BitacoraDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionProcesoRetrasado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Mensaje;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Turno;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.springframework.transaction.annotation.Transactional;

@Singleton
@Named("bitacoraService")
public class BitacoraService extends GenericoService<Bitacora, Integer>
		implements IBitacoraService {

	private static final Calendar calendario = GregorianCalendar.getInstance();

	public void setBitacoraDao(BitacoraDao bitacoraDao) {
		setEntidadDao(bitacoraDao);
	}

	@Transactional
	@Override
	public List<Bitacora> traerUltimasBitacoras(String idEmpresa) {
		logger.trace("En traer últimas bitacoras");
		return ((BitacoraDao) entidadDao).getUltimasBitacoras(idEmpresa, 3);
	}

	@Transactional
	@Override
	public List<Cancelado> getCanceladosActuales(Bitacora bitacora) {
		logger.trace("En getCanceladosActuales de bitácora " + bitacora);

		return ((BitacoraDao) entidadDao).getCanceladosAsignados(bitacora);
	}

	@Transactional
	@Override
	public Map<Integer, List<Cancelado>> clasificaCancelados(
			List<Cancelado> cancelados) {
		logger.trace("En clasifica cancelados ");
		Map<Integer, List<Cancelado>> canceladosClasificados = new HashMap<Integer, List<Cancelado>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8719006951376290764L;
			{
				put(DESDE_FU, new ArrayList<Cancelado>());
				put(PARA_FU, new ArrayList<Cancelado>());
				put(DESDE_CCR, new ArrayList<Cancelado>());
				put(PARA_CCR, new ArrayList<Cancelado>());
				put(GSI, new ArrayList<Cancelado>());
				put(EN_VALIDACION, new ArrayList<Cancelado>());
				put(PRODUCCION, new ArrayList<Cancelado>());
			}
		};
		for (Cancelado cancelado : cancelados) {
			logger.trace("Clasificadndo el cancelado " + cancelado);
			logger.trace("con ultima documentacion "
					+ cancelado.getUltimaDocumentacion());
			switch (cancelado.getCdEstadoCan()) {
			case Estado.INSTRUCCIONES_ENVIADAS:
				if (cancelado.getUltimaDocumentacion().getUsuario().getCdArea()
						.equals(Area.FRENTE_UNICO)) {
					canceladosClasificados.get(DESDE_FU).add(cancelado);
				} else {
					if (cancelado.getUltimaDocumentacion().getUsuario()
							.getCdArea().equals(Area.PRODUCCION)) {
						canceladosClasificados.get(PRODUCCION).add(cancelado);
					} else {
						canceladosClasificados.get(DESDE_CCR).add(cancelado);
					}
				}
				break;
			case Estado.INSTRUCCIONES_SOLICITADAS:
				if (cancelado.getUltimaDocumentacion().getProblema()
						.getCdArea().equals(Area.FRENTE_UNICO)) {
					canceladosClasificados.get(PARA_FU).add(cancelado);
				} else {
					canceladosClasificados.get(PARA_CCR).add(cancelado);
				}
				break;
			case Estado.INSTRUCCIONES_ATENDIDAS:
				canceladosClasificados.get(EN_VALIDACION).add(cancelado);
				break;
			case Estado.ENVIADO_UNICENTER:
				canceladosClasificados.get(GSI).add(cancelado);
				break;
			default:
				break;
			}
		}
		return canceladosClasificados;
	}

	@Transactional
	@Override
	public List<Cancelado> getCanceladosSinAsignacion(Bitacora bitacora) {
		return ((BitacoraDao) entidadDao).getCanceladosNoAsignados(bitacora);
	}

	@Transactional
	@Override
	public Boolean validaBitacoraDelegada(Bitacora bitacora) {
		bitacora = traerPorId(bitacora.getId());
		return bitacora.getTmDelegacion() != null;
	}

	@Transactional
	@Override
	public Bitacora delegaBitacora(Bitacora bitacora, String ldapAutoriza,
			String ldapRecibe) {
		Date tiempoActual;
		Date cicloProductivoSiguiente;
		Turno turnoSiguiente;
		Bitacora bitacoraNueva = null;
		Usuario usuarioAutoriza;
		Usuario usuarioRecibe;

		tiempoActual = new Date();
		cicloProductivoSiguiente = bitacora.getFhCicloBitacora();
		turnoSiguiente = bitacora.getTurno().getTurnoSiguiente();

		ldapAutoriza = ldapAutoriza.toUpperCase();
		ldapRecibe = ldapRecibe.toUpperCase();
		usuarioAutoriza = new Usuario();
		usuarioAutoriza.setCdLdap(ldapAutoriza);
		usuarioAutoriza = encontrarPorInstanciaEjemploGenerico(usuarioAutoriza)
				.get(0);
		usuarioRecibe = new Usuario();
		usuarioRecibe.setCdLdap(ldapRecibe);
		usuarioRecibe = encontrarPorInstanciaEjemploGenerico(usuarioRecibe)
				.get(0);

		if (turnoSiguiente.getCdTurno().equals(Turno.MATUTINO)) {
			calendario.setTime(cicloProductivoSiguiente);
			calendario.roll(GregorianCalendar.DATE, 1);
			cicloProductivoSiguiente = calendario.getTime();
		}

		bitacora.setTmDelegacion(tiempoActual);
		bitacora.setCdUsuRecibe(usuarioRecibe.getCdUsuario());
		bitacora.setCdUsuAutoriza(usuarioAutoriza.getCdUsuario());

		bitacoraNueva = new Bitacora();
		bitacoraNueva.setCdEmpresa(bitacora.getCdEmpresa());
		bitacoraNueva.setFhCicloBitacora(cicloProductivoSiguiente);
		bitacoraNueva.setCdTurno(turnoSiguiente.getCdTurno());
		bitacoraNueva.setTmBitacora(tiempoActual);
		bitacoraNueva.setCdUsuDelega(usuarioRecibe.getCdUsuario());

		logger.trace("Apunto de delegar " + bitacora);
		logger.trace("La bitacora actual sera " + bitacoraNueva);
		actualizar(bitacora);
		bitacoraNueva = guardar(bitacoraNueva);

		return bitacoraNueva;
	}

	@Transactional
	@Override
	public Bitacora retornaBitacora(Bitacora bitacora, String ldapAutoriza) {

		Date cicloAnterior;
		Turno turnoAnterior;
		Bitacora bitacoraAnterior = null;
		List<Bitacora> bitacoras;

		turnoAnterior = bitacora.getTurno().getTurnoAnterior();
		cicloAnterior = bitacora.getFhCicloBitacora();
		if (turnoAnterior.getCdTurno().equals(Turno.NOCTURNO)) {
			calendario.setTime(cicloAnterior);
			calendario.roll(GregorianCalendar.DATE, -1);
			cicloAnterior = calendario.getTime();
		}

		bitacoraAnterior = new Bitacora();
		bitacoraAnterior.setCdTurno(turnoAnterior.getCdTurno());
		bitacoraAnterior.setFhCicloBitacora(cicloAnterior);
		bitacoras = encontrarPorInstanciaEjemplo(bitacoraAnterior);

		if (bitacoras != null && bitacoras.size() > 0) {
			bitacoraAnterior = bitacoras.get(0);
			logger.trace("Se encontro la bitacora anterior a " + bitacora
					+ " es " + bitacoraAnterior);
		} else {
			logger.error("No se encontraron bitácora anteriores para retornar a "
					+ bitacora);
		}

		for (Documentacion documentacion : bitacora.getDocumentacions()) {
			documentacion.setCdBitacora(bitacoraAnterior.getCdBitacora());
			actualizarGenerico(documentacion);
		}

		for (Mensaje mensaje : bitacora.getMensajes()) {
			mensaje.setCdBitacora(bitacoraAnterior.getCdBitacora());
			actualizarGenerico(mensaje);
		}

		for (DocumentacionHoldeado documentacionHoldeado : bitacora
				.getDocumentacionHoldeados()) {
			documentacionHoldeado.setCdBitacora(bitacoraAnterior
					.getCdBitacora());
			actualizarGenerico(documentacionHoldeado);
		}

		for (DocumentacionProcesoRetrasado documentacionProcesoRetrasado : bitacora
				.getDocumentacionProcesoRetrasados()) {
			documentacionProcesoRetrasado.setCdBitacora(bitacoraAnterior
					.getCdBitacora());
			actualizarGenerico(documentacionProcesoRetrasado);
		}

		for (DocumentacionSeguimientoProcesoEspecial documentacionSeguimientoProcesoEspecial : bitacora
				.getDocumentacionEspeciales()) {
			documentacionSeguimientoProcesoEspecial
					.setCdBitacora(bitacoraAnterior.getCdBitacora());
			actualizarGenerico(documentacionSeguimientoProcesoEspecial);
		}

		bitacoraAnterior.setCdUsuAutoriza(null);
		bitacoraAnterior.setCdUsuRecibe(null);
		bitacoraAnterior.setTmDelegacion(null);

		actualizar(bitacoraAnterior);

		eliminar(bitacora);

		return bitacoraAnterior;
	}
}

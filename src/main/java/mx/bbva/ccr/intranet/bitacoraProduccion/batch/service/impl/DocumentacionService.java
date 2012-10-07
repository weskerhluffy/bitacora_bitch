package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DocumentacionDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoDocumentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Problema;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Singleton
@Named("documentacionService")
public class DocumentacionService extends
		GenericoService<Documentacion, Integer> implements
		IDocumentacionService {
	public void setDocumentacionDao(DocumentacionDao documentacionDao) {
		setEntidadDao(documentacionDao);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void guardarDocumentacion(Documentacion documentacion,
			CanceladoId canceladoIdPrincipal, List<CanceladoId> canceladoIds) {
		Documentacion documentacionPersistida = null;
		List<Cancelado> cancelados = null;
		CanceladoDocumentacion canceladoDocumentacion = null;

		if (documentacion.getCdEstado()
				.equals(Estado.INSTRUCCIONES_SOLICITADAS)
				&& traerPorIdGenerico(Problema.class,
						documentacion.getCdProblema()).getCdArea().equals(
						Area.PRODUCCION)) {
			documentacion.setCdEstado(Estado.INSTRUCCIONES_ENVIADAS);
		}

		logger.trace("Documentacion antes de persistir " + documentacion);
		documentacionPersistida = guardar(documentacion);
		logger.trace("Documentacion persistida " + documentacionPersistida);
		// Guardando las relaciones cancelado-documentacion
		for (CanceladoId canceladoId : canceladoIds) {
			canceladoDocumentacion = new CanceladoDocumentacion(canceladoId,
					documentacionPersistida.getId());
			guardarGenerico(canceladoDocumentacion);
			logger.trace("Relacion cancelado-documentacion "
					+ canceladoDocumentacion + " persistida");
		}
		cancelados = traerPorIds(Cancelado.class, canceladoIds);
		// Cambiando el estado, problema y cancelado principal de los cancelados
		for (Cancelado cancelado : cancelados) {
			if (!cancelado.getId().equals(canceladoIdPrincipal)) {
				cancelado.setCdEmpresaPri(canceladoIdPrincipal.getCdEmpresa());
				cancelado.setNbProcesoPri(canceladoIdPrincipal.getNbProceso());
				cancelado.setFhCicloPri(canceladoIdPrincipal.getFhCiclo());
				cancelado.setCdPasoPri(canceladoIdPrincipal.getCdPaso());
				cancelado.setNbGrupoPri(canceladoIdPrincipal.getNbGrupo());
				cancelado.setCdOrigenPri(canceladoIdPrincipal.getCdOrigen());
			}
			cancelado.setCdEstadoCan(documentacionPersistida.getCdEstado());
			cancelado.setCdProblema(documentacionPersistida.getCdProblema());
			actualizarGenerico(cancelado);
		}
		logger.trace("Estado de cancelados actualizados");
	}

	@Transactional
	@Override
	public List<Estado> getEstados(Usuario usuario, Cancelado canceladoOriginal) {
		List<Estado> estatusTmp = null;
		List<Estado> estados = null;
		Boolean esOperador = false;
		Boolean esSoporte = false;
		Cancelado cancelado = null;

		estatusTmp = traerTodosGenerico(Estado.class);
		estados = new ArrayList<Estado>();
		Map<Integer, Estado> mapaEstados = new HashMap<Integer, Estado>();

		for (Estado estatus : estatusTmp) {
			mapaEstados.put(estatus.getCdEstado(), estatus);
		}

		for (Permiso modulo : usuario.getPermisos()) {
			if (modulo.getId().intValue() == Permiso.DOCUMENTAR_OPERACION) {
				esOperador = true;
			}
			if (modulo.getId().intValue() == Permiso.DOCUMENTAR_SOPORTE) {
				esSoporte = true;
			}
		}

		cancelado = traerPorIdGenerico(Cancelado.class,
				canceladoOriginal.getId());
		logger.trace("Entrando a getEstados");
		logger.trace("El estado con el que se llega "
				+ cancelado.getCdEstadoCan());
		logger.trace("Es usuario operador " + esOperador);
		logger.trace("Es usuario FU " + esSoporte);
		if (cancelado.getCdEstadoCan().intValue() != Estado.REGISTRADO) {
			estados.add(traerPorIdGenerico(Estado.class,
					cancelado.getCdEstadoCan()));
		}
		if (cancelado.getCdEstadoCan().intValue() == Estado.REGISTRADO) {
			if (esOperador) {
				estados.add(mapaEstados.get(Estado.INSTRUCCIONES_SOLICITADAS));
				estados.add(mapaEstados.get(Estado.INSTRUCCIONES_ATENDIDAS));
				estados.add(mapaEstados.get(Estado.ENVIADO_UNICENTER));
			}
			if (esSoporte) {
				estados.add(mapaEstados.get(Estado.INSTRUCCIONES_ENVIADAS));
			}
		} else {
			if (cancelado.getCdEstadoCan().intValue() == Estado.INSTRUCCIONES_SOLICITADAS) {
				if (esOperador) {
					estados.add(mapaEstados.get(Estado.INSTRUCCIONES_ATENDIDAS));
				}
			} else {
				if (cancelado.getCdEstadoCan().intValue() == Estado.INSTRUCCIONES_ENVIADAS) {
					if (esOperador) {
						estados.add(mapaEstados.get(Estado.ENVIADO_UNICENTER));
						estados.add(mapaEstados
								.get(Estado.INSTRUCCIONES_ATENDIDAS));
					}
				} else {
					if (cancelado.getCdEstadoCan().intValue() == Estado.INSTRUCCIONES_ATENDIDAS) {
						if (esOperador) {
							estados.add(mapaEstados
									.get(Estado.ENVIADO_UNICENTER));
							estados.add(mapaEstados
									.get(Estado.INSTRUCCIONES_SOLICITADAS));
						}
						if (esSoporte) {
							estados.add(mapaEstados
									.get(Estado.INSTRUCCIONES_ENVIADAS));
						}
					} else {
						logger.error("El estado del cancelado no permite modificarlo, se redireccionara al usuario a la pagina principal");
						return null;
					}
				}

			}
		}
		logger.trace("Saliendo de getEstados");
		return estados;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void guardarDocumentacion(Documentacion documentacion,
			List<CanceladoDocumentacion> canceladoDocumentacions) {
		Documentacion documentacionPersistida = null;
		logger.trace("La documentación antes de persistir es " + documentacion);
		documentacionPersistida = guardar(documentacion);
		logger.trace("La documentación persistida es "
				+ documentacionPersistida);

		for (CanceladoDocumentacion canceladoDocumentacion : canceladoDocumentacions) {
			canceladoDocumentacion.getId().setCdDocumentacion(
					documentacionPersistida.getId());
			logger.trace("El candoc que se persistirá "
					+ canceladoDocumentacion.getId());
			guardarGenerico(canceladoDocumentacion);
		}
	}

	@Transactional
	@Override
	public List<Cancelado> validarCanceladoDocumentable(
			List<CanceladoId> canceladoIds) {
		Cancelado canceladoPadre;
		Documentacion documentacionComun;
		Boolean agrupacionValida;
		List<Cancelado> cancelados = null;
		cancelados = traerPorIds(Cancelado.class, canceladoIds);

		canceladoPadre = cancelados.get(0).getCanceladoPrincipal();
		documentacionComun = cancelados.get(0).getUltimaDocumentacion();
		List<Cancelado> canceladosNoDocumentables = new ArrayList<Cancelado>();

		logger.trace("El padre de " + cancelados.get(0).getCanceladoPrincipal()
				+ " es " + canceladoPadre);
		for (Cancelado cancelado : cancelados) {

			agrupacionValida = true;
			if ((cancelado.getCanceladoPrincipal() == null && canceladoPadre != null)
					|| (cancelado.getCanceladoPrincipal() != null && canceladoPadre == null)) {
				logger.warn("El principal de referencia " + canceladoPadre
						+ " y el del cancelado " + cancelado + " que es "
						+ cancelado.getCanceladoPrincipal()
						+ " uno es nulo y otro no");
				agrupacionValida = false;
			} else {
				if ((cancelado.getCanceladoPrincipal() != null && canceladoPadre != null)
						&& (!cancelado.getCanceladoPrincipal().getId()
								.equals(canceladoPadre.getId()))) {
					logger.warn("El principal del cancelado "
							+ cancelado.getCanceladoPrincipal().getId()
							+ " y el principal de referencia "
							+ canceladoPadre.getId() + " difieren");
					agrupacionValida = false;
				}
				if ((cancelado.getUltimaDocumentacion().getId() == null && documentacionComun
						.getId() != null)
						|| (cancelado.getUltimaDocumentacion().getId() != null && documentacionComun
								.getId() == null)
						|| (cancelado.getUltimaDocumentacion().getId() != null
								&& documentacionComun.getId() != null && !cancelado
								.getUltimaDocumentacion().getId()
								.equals(documentacionComun.getId()))) {
					logger.warn("El cancelado " + cancelado + " difiere a "
							+ canceladoPadre + " ya fue documentado");
					logger.trace("Sus ultimas documentaciones son "
							+ cancelado.getUltimaDocumentacion().getId()
							+ " y " + documentacionComun.getId());
					agrupacionValida = false;
				}
			}
			if (!agrupacionValida) {
				canceladosNoDocumentables.add(cancelado);
			}
		}
		return canceladosNoDocumentables;
	}

	@Transactional
	@Override
	public Documentacion validarCanceladoDocumentable(
			Documentacion documentacion, CanceladoId canceladoIdPrincipal) {
		Documentacion documentacionActual = null;
		Cancelado cancelado = null;
		cancelado = traerPorIdGenerico(Cancelado.class, canceladoIdPrincipal);
		documentacionActual = cancelado.getUltimaDocumentacion();
		if (documentacionActual.getTmDocumentacion().getTime() == documentacion
				.getTmDocumentacion().getTime()) {
			documentacionActual = null;
		}
		return documentacionActual;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	@Override
	public void anadirDocumentacion(Documentacion documentacion,
			CanceladoId canceladoIdPrincipal, List<CanceladoId> canceladoIds) {
		Documentacion documentacionUltima = null;

		documentacionUltima = traerPorIdGenerico(Cancelado.class,
				canceladoIdPrincipal).getUltimaDocumentacion();
		documentacion.setCdEstado(documentacionUltima.getCdEstado());
		documentacion.setCdProblema(documentacionUltima.getCdProblema());
		guardarDocumentacion(documentacion, canceladoIdPrincipal, canceladoIds);

	}

	@Transactional
	@Override
	public List<Cancelado> validarCanceladoSoloRegistrado(
			List<CanceladoId> canceladoIdsSel) {
		logger.trace("En validarCanceladoSoloRegistrado");
		List<Cancelado> cancelados;
		List<Cancelado> canceladosDocumentados = new ArrayList<Cancelado>();
		cancelados = traerPorIds(Cancelado.class, canceladoIdsSel);
		for (Cancelado cancelado : cancelados) {
			if (cancelado.getId() != null
					&& cancelado.getCdEstadoCan() != Estado.REGISTRADO) {
				logger.warn("El cancelado " + cancelado
						+ " ya fue tratado por el sistema");
				canceladosDocumentados.add(cancelado);
			}
		}
		return canceladosDocumentados;
	}

	@Transactional
	@Override
	public Integer validarMovimientoCancelado(Cancelado canceladoPrincipal,
			Documentacion documentacionUltima, Usuario usuario,
			Integer tipoCanceladoDestino, Integer idAreaDestino) {
		logger.trace("En validarMovimientoCancelado");
		Integer siguienteEstado = null;
		Boolean esSoporte = false;
		Boolean esOperador = false;
		Area areaProblemaActual = null;
		for (Permiso permiso : usuario.getPermisos()) {
			if (permiso.getCdPermiso().equals(Permiso.DOCUMENTAR_OPERACION)) {
				esOperador = true;
			}
			if (permiso.getCdPermiso().equals(Permiso.DOCUMENTAR_SOPORTE)) {
				esSoporte = true;
			}
		}
		logger.trace("Es soporte " + esSoporte);
		logger.trace("Es operador " + esOperador);
		logger.trace("el cancealdo principal " + canceladoPrincipal);
		logger.trace("El tipo de destino " + tipoCanceladoDestino);
		logger.trace("El usuario " + usuario);

		if (idAreaDestino == null) {
			switch (tipoCanceladoDestino) {
			case IBitacoraService.PRODUCCION:
				idAreaDestino = Area.PRODUCCION;
				break;
			case IBitacoraService.PARA_FU:
				idAreaDestino = Area.FRENTE_UNICO;
				break;
			case IBitacoraService.DESDE_CCR:
				idAreaDestino = traerPorIdGenerico(Problema.class,
						canceladoPrincipal.getCdProblema()).getCdArea();
				break;
			case IBitacoraService.DESDE_FU:
				idAreaDestino = Area.FRENTE_UNICO;
				break;
			default:
				logger.error("No se pudo determinar el área destino ");
				return null;
			}
		}
		areaProblemaActual = traerPorIdGenerico(Problema.class,
				canceladoPrincipal.getCdProblema()).getArea();

		logger.trace("El area calculada " + idAreaDestino);
		switch (canceladoPrincipal.getCdEstadoCan()) {
		case Estado.INSTRUCCIONES_SOLICITADAS:
			if ((tipoCanceladoDestino.equals(IBitacoraService.DESDE_FU) || tipoCanceladoDestino
					.equals(IBitacoraService.DESDE_CCR))
					&& esSoporte
					&& (canceladoPrincipal.getProblema().getCdArea()
							.equals(usuario.getCdArea()))
					&& usuario.getCdArea().equals(idAreaDestino)) {
				siguienteEstado = Estado.INSTRUCCIONES_ENVIADAS;
			}
			break;
		case Estado.INSTRUCCIONES_ENVIADAS:
			if (tipoCanceladoDestino.equals(IBitacoraService.EN_VALIDACION)
					&& esOperador
					&& areaProblemaActual.getCdArea().equals(idAreaDestino)) {
				siguienteEstado = Estado.INSTRUCCIONES_ATENDIDAS;
			}
			if ((tipoCanceladoDestino.equals(IBitacoraService.PARA_CCR) || tipoCanceladoDestino
					.equals(IBitacoraService.PARA_FU)) && esOperador) {
				siguienteEstado = Estado.INSTRUCCIONES_SOLICITADAS;
			}
			if (tipoCanceladoDestino.equals(IBitacoraService.GSI)
					&& esOperador
					&& areaProblemaActual.getCdArea().equals(idAreaDestino)
					&& !documentacionUltima
							.getTxRecuperacion()
							.matches(
									IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO)
					&& !documentacionUltima
							.getTxImpacto()
							.matches(
									IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO)
					&& !documentacionUltima
							.getTxSintoma()
							.matches(
									IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO)) {
				siguienteEstado = Estado.ENVIADO_UNICENTER;
			}
			break;
		case Estado.INSTRUCCIONES_ATENDIDAS:
			if ((tipoCanceladoDestino.equals(IBitacoraService.PARA_CCR) || tipoCanceladoDestino
					.equals(IBitacoraService.PARA_FU)) && esOperador) {
				siguienteEstado = Estado.INSTRUCCIONES_SOLICITADAS;
			}
			if ((tipoCanceladoDestino.equals(IBitacoraService.DESDE_CCR) || tipoCanceladoDestino
					.equals(IBitacoraService.DESDE_FU)) && esSoporte) {
				siguienteEstado = Estado.INSTRUCCIONES_ENVIADAS;
			}
			if (tipoCanceladoDestino.equals(IBitacoraService.PRODUCCION)
					&& esOperador) {
				siguienteEstado = Estado.INSTRUCCIONES_ENVIADAS;
			}
			if (tipoCanceladoDestino.equals(IBitacoraService.GSI)
					&& esOperador
					&& areaProblemaActual.getCdArea().equals(idAreaDestino)
					&& !documentacionUltima
							.getTxRecuperacion()
							.matches(
									IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO)
					&& !documentacionUltima
							.getTxImpacto()
							.matches(
									IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO)
					&& !documentacionUltima
							.getTxSintoma()
							.matches(
									IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO)) {
				siguienteEstado = Estado.ENVIADO_UNICENTER;
			}
			break;
		default:
			logger.warn("Movimiento no permitido");
			break;
		}
		return siguienteEstado;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	@Override
	public Cancelado moverCancelado(Cancelado canceladoPrincipal,
			Documentacion documentacion, Usuario usuario,
			Integer idEstadoDestino, Integer idAreaDestino,
			Integer tipoCanceladoDestino) {
		logger.trace("En mover cancelado");
		Cancelado cancelado = null;
		Documentacion documentacionNueva = null;
		Area areaProblemaActual = null;

		cancelado = traerPorIdGenerico(Cancelado.class,
				canceladoPrincipal.getId());

		documentacionNueva = new Documentacion(documentacion);
		documentacionNueva.setTmDocumentacion(new Date());
		documentacionNueva.setCdEstado(idEstadoDestino);
		documentacionNueva.setCdUsuario(usuario.getCdUsuario());

		if (idAreaDestino == null) {
			switch (tipoCanceladoDestino) {
			case IBitacoraService.PRODUCCION:
				idAreaDestino = Area.PRODUCCION;
				break;
			case IBitacoraService.PARA_FU:
				idAreaDestino = Area.FRENTE_UNICO;
				break;
			case IBitacoraService.DESDE_FU:
				idAreaDestino = Area.FRENTE_UNICO;
				break;
			case IBitacoraService.DESDE_CCR:
				idAreaDestino = traerPorIdGenerico(Problema.class,
						canceladoPrincipal.getCdProblema()).getCdArea();
				break;
			default:
				logger.error("No se pudo determinar el área destino ");
				return null;
			}
		}

		areaProblemaActual = traerPorIdGenerico(Problema.class,
				canceladoPrincipal.getCdProblema()).getArea();
		if (!areaProblemaActual.getCdArea().equals(idAreaDestino)) {
			documentacionNueva.setCdProblema(traerPorIdGenerico(Area.class,
					idAreaDestino).getProblemas().get(0).getCdProblema());
		}

		logger.trace("Antes de persistir la documentacion "
				+ documentacionNueva);
		documentacionNueva = guardar(documentacionNueva);

		guardarGenerico(new CanceladoDocumentacion(cancelado.getId(),
				documentacionNueva.getId()));
		for (Cancelado canceladoActual : cancelado.getCanceladosSecundarios()) {
			guardarGenerico(new CanceladoDocumentacion(canceladoActual.getId(),
					documentacionNueva.getId()));
			canceladoActual.setCdEstadoCan(documentacionNueva.getCdEstado());

			canceladoActual.setCdProblema(documentacionNueva.getCdProblema());
			actualizarGenerico(canceladoActual);
		}

		cancelado.setCdEstadoCan(documentacionNueva.getCdEstado());
		cancelado.setCdProblema(documentacionNueva.getCdProblema());
		cancelado = actualizarGenerico(cancelado);

		return cancelado;
	}

	@Transactional
	@Override
	public Boolean validarTextoDocumentacion(Documentacion documentacion) {
		return documentacion.getTxImpacto() != null
				&& !documentacion
						.getTxImpacto()
						.matches(
								"[\\s\\S]*"
										+ IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO
										+ "[\\s\\S]*")
				&& documentacion.getTxRecuperacion() != null
				&& !documentacion
						.getTxRecuperacion()
						.matches(
								"[\\s\\S]*"
										+ IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO
										+ "[\\s\\S]*")
				&& documentacion.getTxSintoma() != null
				&& !documentacion
						.getTxSintoma()
						.matches(
								"[\\s\\S]*"
										+ IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO
										+ "[\\s\\S]*");
	}

	@Transactional
	@Override
	public Boolean validarTextoRecuperacion(Documentacion documentacion) {
		return documentacion.getTxRecuperacion() != null
				&& !documentacion
						.getTxRecuperacion()
						.matches(
								"[\\s\\S]*"
										+ IDocumentacionService.MARCADOR_CANCELADO_NO_DOCUMENTADO
										+ "[\\s\\S]*");
	}

	@Transactional
	@Override
	public void guardarDocumentacionIndividual(Documentacion documentacion,
			List<CanceladoId> canceladoIds) {
		logger.trace("En guardarDocumentacionIndividual");
		Documentacion documentacionPersistida = null;
		List<Cancelado> cancelados = null;
		CanceladoDocumentacion canceladoDocumentacion = null;

		cancelados = traerPorIds(Cancelado.class, canceladoIds);

		if (documentacion.getCdEstado()
				.equals(Estado.INSTRUCCIONES_SOLICITADAS)
				&& traerPorIdGenerico(Problema.class,
						documentacion.getCdProblema()).getCdArea().equals(
						Area.PRODUCCION)) {
			documentacion.setCdEstado(Estado.INSTRUCCIONES_ENVIADAS);
		}

		for (Cancelado cancelado : cancelados) {
			logger.trace("Procesando cancelado " + cancelado);

			documentacionPersistida = new Documentacion(documentacion);
			documentacionPersistida.setTmDocumentacion(new Date());
			logger.trace("Creando documentación " + documentacion);
			guardar(documentacionPersistida);
			logger.trace("Documentacion persistida" + documentacionPersistida);
			// Guardando las relaciones cancelado-documentacion
			canceladoDocumentacion = new CanceladoDocumentacion(
					cancelado.getId(), documentacionPersistida.getId());
			logger.trace("Creando enlace cancelado-documentacion "
					+ canceladoDocumentacion);
			guardarGenerico(canceladoDocumentacion);

			cancelado.setCdEstadoCan(documentacionPersistida.getCdEstado());
			cancelado.setCdProblema(documentacionPersistida.getCdProblema());
			actualizarGenerico(cancelado);
		}

	}

	@Transactional
	@Override
	public void guardarDocumentacionInicial(Documentacion documentacion,
			CanceladoId canceladoIdPrincipal, List<CanceladoId> canceladoIds) {
		guardarDocumentacion(documentacion, canceladoIdPrincipal, canceladoIds);
	}

	@Transactional
	@Override
	public void sincronizarDocumentacionAdelantada() {
		((DocumentacionDao) entidadDao).sincronizarDocumentacionAdelantada();
	}
}

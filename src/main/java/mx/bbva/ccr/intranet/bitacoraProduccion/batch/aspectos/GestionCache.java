package mx.bbva.ccr.intranet.bitacoraProduccion.batch.aspectos;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionHoldeadoService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoDocumentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.IModelo;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoEspecial;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.ProcesoRetrasado;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.proxy.HibernateProxyHelper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Named
@Aspect
public class GestionCache {
	private Logger logger = Logger.getLogger(GestionCache.class);
	private SessionFactory sessionFactory;
	private IDocumentacionService documentacionService;
	private IDocumentacionHoldeadoService documentacionHoldeadoService;

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a IModelo
	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate.*.* (..))")
	public void refrescarCache(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {
		String nombreFuncion = joinPoint.getSignature().getName();
		Object argumento = null;
		List<Field> atributos;
		IModelo<Serializable> argumentoPersistente;
		Class<IModelo<Serializable>> clazzeOriginal = null;
		logger.trace("Interceptando " + joinPoint.getSignature());
		logger.trace("Nombre de funcion " + nombreFuncion);

		if (joinPoint.getArgs().length > 0) {
			argumento = joinPoint.getArgs()[0];
			if (argumento instanceof IModelo<?>) {
				argumentoPersistente = (IModelo<Serializable>) argumento;
				clazzeOriginal = HibernateProxyHelper
						.getClassWithoutInitializingProxy(argumentoPersistente);
			} else {
				return;
			}
		} else {
			return;
		}

		if (nombreFuncion.matches("delete.*")
				|| nombreFuncion.matches("update.*")) {
			if (!clazzeOriginal.equals(Cancelado.class)
					&& !clazzeOriginal.equals(Documentacion.class)
					&& !clazzeOriginal.equals(Holdeado.class)
					&& !clazzeOriginal.equals(DocumentacionHoldeado.class)
					&& !clazzeOriginal.equals(ProcesoEspecial.class)
					&& !clazzeOriginal.equals(ProcesoRetrasado.class)) {
				logger.trace("Se ejecutó una función de modificación, lo que se modificó fue "
						+ argumento);
				atributos = Arrays.asList(clazzeOriginal.getDeclaredFields());
				for (Field field : atributos) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Cache.class)
							&& field.get(argumentoPersistente) instanceof Collection) {
						logger.trace("La collección "
								+ clazzeOriginal.getName() + "."
								+ field.getName()
								+ " es cacheable y se refrescará");
						if (sessionFactory.getCache().containsCollection(
								clazzeOriginal.getName() + "."
										+ field.getName(),
								argumentoPersistente.getId())) {
							logger.trace("La coleccion esta en cache y sera refrescada");
							sessionFactory.getCache().evictCollection(
									clazzeOriginal.getName() + "."
											+ field.getName(),
									argumentoPersistente.getId());
						}
					}

				}
				logger.trace("Refrescando entidad " + argumentoPersistente
						+ " de clase " + clazzeOriginal);
				if (sessionFactory.getCache().containsEntity(clazzeOriginal,
						argumentoPersistente.getId())) {
					logger.trace("La entidad  esta en cache y sera refrescada");
					sessionFactory.getCache().evictEntity(clazzeOriginal,
							argumentoPersistente.getId());
				}
			}
		}

		if (clazzeOriginal.equals(ProcesoEspecial.class)
				&& nombreFuncion.matches("save.*")) {
			ProcesoEspecial procesoEspecial = null;
			procesoEspecial = (ProcesoEspecial) argumento;

			logger.trace("Interceptando con refrescarCacheEspeciales "
					+ joinPoint.getSignature());
			logger.trace("Nombre de funcion " + nombreFuncion);

			logger.trace("El sesion factoria es " + sessionFactory);

			logger.trace("Refrescando el cache de " + procesoEspecial);
			sessionFactory.getCache().evictEntity(ProcesoEspecial.class,
					procesoEspecial.getId());
			sessionFactory.getCache().evictCollection(
					Empresa.class.getName() + ".procesosEspeciales",
					procesoEspecial.getCdEmpresa());
		}

	}

	// Por el cast de los argumentos que son lista a lista de CanceladoId
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionService.* (..))")
	public void refrescarCacheCanceladoDocumentacion(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {
		String nombreFuncion = joinPoint.getSignature().getName();
		List<Object> argumentos;
		Class<?> clazzeOriginal;
		CanceladoId canceladoId;
		Documentacion documentacion;
		CanceladoDocumentacion canceladoDocumentacionEjemplo;
		List<CanceladoDocumentacion> canceladoDocumentacions;

		logger.trace("Interceptando con refrescarCacheCanceladoDocumentacion "
				+ joinPoint.getSignature());
		logger.trace("Nombre de funcion " + nombreFuncion);

		logger.trace("El sesion factoria es " + sessionFactory);
		sessionFactory.getCurrentSession().flush();

		argumentos = Arrays.asList(joinPoint.getArgs());

		if (nombreFuncion.matches("guardar.*")
				|| nombreFuncion.matches("mover.*")
				|| nombreFuncion.matches("anadir.*")) {
			for (Object argumento : argumentos) {
				logger.trace("El argumento es " + argumento);
				if (argumento != null) {
					clazzeOriginal = HibernateProxyHelper
							.getClassWithoutInitializingProxy(argumento);
					if (clazzeOriginal.equals(Documentacion.class)) {

						documentacion = (Documentacion) argumento;
						canceladoDocumentacionEjemplo = new CanceladoDocumentacion();
						canceladoDocumentacionEjemplo
								.setCdDocumentacion(documentacion
										.getCdDocumentacion());
						canceladoDocumentacions = documentacionService
								.encontrarPorInstanciaEjemploGenerico(canceladoDocumentacionEjemplo);
						logger.trace("La documentacion " + documentacion
								+ " tiene cancelados "
								+ documentacion.getCancelados());

						for (CanceladoDocumentacion canceladoDocumentacion : canceladoDocumentacions) {
							logger.trace("Iterando en cancelado-documentacion "
									+ canceladoDocumentacion);
							canceladoId = new CanceladoId(
									canceladoDocumentacion.getId()
											.getCdEmpresa(),
									canceladoDocumentacion.getId().getNbGrupo(),
									canceladoDocumentacion.getId()
											.getNbProceso(),
									canceladoDocumentacion.getId().getCdPaso(),
									canceladoDocumentacion.getId().getFhCiclo(),
									canceladoDocumentacion.getId()
											.getCdOrigen());
							logger.trace("El id de cancelado cuyo cache se invalidara "
									+ canceladoId);
							sessionFactory.getCache().evictEntity(
									Cancelado.class, canceladoId);
							sessionFactory.getCache().evictCollection(
									Cancelado.class.getName()
											+ ".documentacions", canceladoId);
						}
					}

					if (nombreFuncion.matches("guardarDocumentacionInicial")
							&& clazzeOriginal.equals(CanceladoId.class)) {
						sessionFactory.getCache().evictCollection(
								Cancelado.class.getName()
										+ ".canceladosSecundarios",
								(Serializable) argumento);
					}
				}
			}
		}
	}

	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionHoldeadoService.documentarHoldeado (mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Holdeado,mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionHoldeado))")
	public void refrescarCacheHoldeado(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {
		Holdeado holdeado = null;
		holdeado = (Holdeado) joinPoint.getArgs()[0];
		logger.trace("Refrescando el cachete de " + holdeado);
		sessionFactory.getCache().evictEntity(Holdeado.class, holdeado.getId());
		sessionFactory.getCache().evictCollection(
				Holdeado.class.getName() + ".documentacionHoldeados",
				holdeado.getId());
		sessionFactory.getCache().evictQueryRegion(
				"query.holdeadosVigentesBitacora");
	}

	@SuppressWarnings("unchecked")
	// Por el cast de objeto a lista de enteros
	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionProcesoRetrasadoService.documentarProcesoRetrasado (..))")
	public void refrescarCacheProcesoRetrasado(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {
		List<Integer> idProcesosRetrasados;
		idProcesosRetrasados = (List<Integer>) joinPoint.getArgs()[0];
		logger.trace("Refrescando el cachete de " + idProcesosRetrasados);
		for (Integer idProcesoRetrasado : idProcesosRetrasados) {

			sessionFactory.getCache().evictEntity(ProcesoRetrasado.class,
					idProcesoRetrasado);
			sessionFactory.getCache().evictCollection(
					ProcesoRetrasado.class.getName()
							+ ".documentacionProcesoRetrasados",
					idProcesoRetrasado);
		}
	}

	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionSeguimientoProcesoEspecialService.documentarSeguimientoProcesoEspecial (mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial,mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial))")
	public void refrescarCacheSeguimientoProcesoEspecial(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {
		SeguimientoProcesoEspecial seguimientoProcesoEspecial = null;
		seguimientoProcesoEspecial = (SeguimientoProcesoEspecial) joinPoint
				.getArgs()[0];
		logger.trace("Refrescando el cachete de " + seguimientoProcesoEspecial);
		sessionFactory.getCache().evictEntity(SeguimientoProcesoEspecial.class,
				seguimientoProcesoEspecial.getId());
		sessionFactory.getCache().evictCollection(
				SeguimientoProcesoEspecial.class.getName()
						+ ".documentacionSeguimientoProcesoEspecials",
				seguimientoProcesoEspecial.getId());
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public IDocumentacionService getDocumentacionService() {
		return documentacionService;
	}

	public void setDocumentacionService(
			IDocumentacionService documentacionService) {
		this.documentacionService = documentacionService;
	}

	public IDocumentacionHoldeadoService getDocumentacionHoldeadoService() {
		return documentacionHoldeadoService;
	}

	public void setDocumentacionHoldeadoService(
			IDocumentacionHoldeadoService documentacionHoldeadoService) {
		this.documentacionHoldeadoService = documentacionHoldeadoService;
	}

}

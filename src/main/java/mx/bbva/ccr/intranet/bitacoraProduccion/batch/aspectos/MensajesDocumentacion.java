package mx.bbva.ccr.intranet.bitacoraProduccion.batch.aspectos;

import java.util.List;

import javax.inject.Named;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IBitacoraService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDocumentacionService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.annotations.RemoteProxy;

@RemoteProxy
@Named
@Aspect
public class MensajesDocumentacion {

	private Logger logger = Logger.getLogger(MensajesDocumentacion.class);
	private IDocumentacionService documentacionService;
	private IBitacoraService bitacoraService;
	private String contexto;
	private Cancelado cancelado;
	private CanceladoId canceladoId;
	private List<CanceladoId> canceladoIdsSel;
	Empresa empresa;

	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionService.moverCancelado (..))")
	public void mensajeMoverCancelado(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {

		logger.trace("El interceptor de mover cancelado esta AQUI");

		cancelado = (Cancelado) joinPoint.getArgs()[0];

		contexto = ServerContextFactory.get().getContextPath();

		Browser.withPage(contexto + "/bitacora.xhtml", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en mover cancelado");
				ScriptSessions.addFunctionCall("agregarMensajeMoverCancelado",
						cancelado);
			}
		});

	}

	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionService.guardarDocumentacionInicial (..))")
	public void mensajeGuardarDocumentacionInicial(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {

		logger.trace("La intercepcion de asignacion documentacion inicial"
				+ joinPoint.getArgs()[1]);

		canceladoId = (CanceladoId) joinPoint.getArgs()[1];

		cancelado = documentacionService.traerPorIdGenerico(Cancelado.class,
				canceladoId);

		contexto = ServerContextFactory.get().getContextPath();

		Browser.withPage(contexto + "/bitacora.xhtml", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en asignar inicial cancelado");
				ScriptSessions.addFunctionCall(
						"agregarMensajeDocumentarCancelado", cancelado);
			}
		});

	}

	/* Por el cast a lista de CanceladoId */
	@SuppressWarnings("unchecked")
	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionService.guardarDocumentacionIndividual (..))")
	public void mensajeGuardarDocumentacionIndividual(JoinPoint joinPoint)
			throws IllegalArgumentException, IllegalAccessException {

		logger.trace("La intercepcion de asignacion documentacion individual "
				+ joinPoint.getArgs()[1]);

		canceladoIdsSel = (List<CanceladoId>) joinPoint.getArgs()[1];
		for (CanceladoId cId : canceladoIdsSel) {
			cancelado = documentacionService.traerPorIdGenerico(
					Cancelado.class, cId);

			contexto = ServerContextFactory.get().getContextPath();

			Browser.withPage(contexto + "/bitacora.xhtml", new Runnable() {
				@Override
				public void run() {
					logger.trace("Haciendo ajax inverso en asignar inicial cancelado");
					ScriptSessions.addFunctionCall(
							"agregarMensajeDocumentarCancelado", cancelado);
				}
			});
		}

	}

	@AfterReturning("execution(* mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl.DocumentacionService.guardarDocumentacion (mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion,mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId,java.util.List))")
	public void mensajeGuardarDocumentacionCanceladoPrincipal(
			JoinPoint joinPoint) throws IllegalArgumentException,
			IllegalAccessException {

		logger.trace("La intercepcion de documentar canceladoId "
				+ joinPoint.getArgs()[1]);

		canceladoId = (CanceladoId) joinPoint.getArgs()[1];

		cancelado = documentacionService.traerPorIdGenerico(Cancelado.class,
				canceladoId);

		contexto = ServerContextFactory.get().getContextPath();

		Browser.withPage(contexto + "/bitacora.xhtml", new Runnable() {
			@Override
			public void run() {
				logger.trace("Haciendo ajax inverso en asignar inicial cancelado");
				ScriptSessions.addFunctionCall(
						"agregarMensajeDocumentarCancelado", cancelado);
			}
		});

	}

	public IDocumentacionService getDocumentacionService() {
		return documentacionService;
	}

	public void setDocumentacionService(
			IDocumentacionService documentacionService) {
		this.documentacionService = documentacionService;
	}

	public IBitacoraService getBitacoraService() {
		return bitacoraService;
	}

	public void setBitacoraService(IBitacoraService bitacoraService) {
		this.bitacoraService = bitacoraService;
	}

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	public Cancelado getCancelado() {
		return cancelado;
	}

	public void setCancelado(Cancelado cancelado) {
		this.cancelado = cancelado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.util;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.BitacoraController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.CatalogoProcesoEspecialController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.CatalogoUsuarioController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionAdelantadaController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionHoldeadoController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.DocumentacionProcesoRetrasadoController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.controller.GestionarMensajeController;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DocumentacionSeguimientoProcesoEspecial;

//TODO Poner todas los nombres de objetos de sesion aqui
public class NombreObjetoSesion {
	public static final String CANCELADOS_CLASIFICADOS = BitacoraController.class
			.getName() + ":canceladosClasificados";
	public static final String CANCELADO_SELECCIONADO_PRINCIPAL = DocumentacionController.class
			.getName() + ":canceladoPrincipalSel";
	public static final String CANCELADOS_SELECCIONADOS = DocumentacionController.class
			.getName() + ":canceladosSel";
	public static final String USUARIO = BitacoraController.class.getName()
			+ ":usuario";
	public static final String BITACORA = BitacoraController.class.getName()
			+ ":bitacora";
	public static final String AREAS_DISPONIBLES = DocumentacionController.class
			.getName() + ":areas";
	public static final String PAGINA_DOCUMENTACION = DocumentacionController.class
			.getName() + ":paginaDocumentacion";
	public static final String CANCELADOS_CLASIFICADOS_CCR = BitacoraController.class
			.getName() + ":canceladosClasificadosCCR";
	public static final String PERMISOS_DISPONIBLES = CatalogoUsuarioController.class
			.getName() + ":permisosDisponibles";
	public static final String PERFILES_DISPONIBLES = CatalogoUsuarioController.class
			.getName() + ":perfilesDisponibles";
	public static final String EMPRESAS_DISPONIBLES = CatalogoUsuarioController.class
			.getName() + ":empresasDisponibles";
	public static final String EMPRESA = BitacoraController.class.getName()
			+ ":empresa";
	public static final String ULTIMA_DOCUMENTACION = DocumentacionController.class
			.getName() + ":ultimaDocumentacion";
	public static final String ORIGENES_DISPONIBLES = DocumentacionAdelantadaController.class
			.getName() + ":origenesDisponibles";
	public static final String CANCELADOS_DOCUMENTACION_SEL = DocumentacionAdelantadaController.class
			.getName() + ":canceladosDocumentacionSel";
	public static final String HOLDEADO = DocumentacionHoldeadoController.class
			.getName() + ":holdeado";
	public static final String TIPO_MENSAJE = GestionarMensajeController.class
			.getName() + ":tipoMensaje";

	public static final String TIPOS_PROCESO_ESPECIAL_DISPONIBLES = CatalogoProcesoEspecialController.class
			.getName() + ":tiposProcesoEspecialDisponibles";

	public static final String PROCESO_RETRASADO = DocumentacionProcesoRetrasadoController.class
			.getName() + ":documentacionProcesoRetrasado";

	public static final String EMP_SEL = CatalogoProcesoEspecialController.class
			.getName() + "empresaSeleccionada";

	public static final String SEGUIMIENTO_PROCESO_ESPECIAL = DocumentacionSeguimientoProcesoEspecial.class
			.getName() + ":documentacionSeguimientoProcesoEspecial";
	public static final String IDS_PROCESOS_RETRASADOS = DocumentacionProcesoRetrasadoController.class
			.getName() + ":idsProcesosRetrasadosSel";
	public static final String PROCESOS_RETRASADOS = DocumentacionProcesoRetrasadoController.class
			.getName() + ":procesosRetrasadosSel";

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Area;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Cancelado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoDocumentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Documentacion;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Estado;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

public interface IDocumentacionService extends
		IGenericoService<Documentacion, Integer> {
	public static final String MARCADOR_CANCELADO_NO_DOCUMENTADO = "_Documentacion pendiente_";

	/**
	 * Guarda la documentación <code>documentacion</code>, cambia de estado los
	 * cancelados identificados por los ids contenidos en
	 * <code>canceladoIds</code>, dentro de los cuales esta el principal,
	 * identificado por el id <code>canceladoIdPrincipal</code>. Se crean las
	 * relaciones entre el registro de documentación y los cancelados, poniendo
	 * como cancelado principal el identificado por
	 * <code>canceladoIdPrincipal</code> en los que no son principales. Se hace
	 * un caso especial para cuando el estado que viene en
	 * <code>documentacion</code> es {@link Estado#INSTRUCCIONES_SOLICITADAS} y
	 * el área del problema es {@link Area#PRODUCCION}, se cambia el estado a
	 * {@link Estado#INSTRUCCIONES_ENVIADAS}, para efectos de cumplir con la
	 * clasificación de cancelados {@link IBitacoraService#PRODUCCION}.
	 * 
	 * Se da por hecho que <code>documentacion</code> tiene el valor del estado
	 * del cancelado al que se actualizaran los cancelados.
	 * 
	 * Efectos secundarios: <code>documentacion</code> apunta a un objeto ya
	 * persistido, es decir, con su id ya generada.
	 * 
	 * @param documentacion
	 * @param canceladoIdPrincipal
	 * @param canceladoIds
	 */
	public void guardarDocumentacion(Documentacion documentacion,
			CanceladoId canceladoIdPrincipal, List<CanceladoId> canceladoIds);

	/**
	 * 
	 * @param usuario
	 * @param cancelado
	 * @return La lista de estados a los que puede cambiarse el cancelado
	 *         <code>cancelado</code> (y los cancelados que tengan a este como
	 *         principal) en base a los permisos con los que cuente
	 *         <code>usuario</code>
	 */
	public List<Estado> getEstados(Usuario usuario, Cancelado cancelado);

	/**
	 * Guarda las relaciones entre el registro de documentación y los cancelados
	 * (que aún no han sido registrados en el sistema).
	 * 
	 * @param documentacion
	 *            Registro de documentación a persistir
	 * @param canceladoDocumentacions
	 *            Relaciones cancelado-documentación a persistir
	 */
	public void guardarDocumentacion(Documentacion documentacion,
			List<CanceladoDocumentacion> canceladoDocumentacions);

	/**
	 * Se valida que los cancelados que tengan un id de los que estan contenidos
	 * en <code>canceladoIds</code>:
	 * <ul>
	 * <li>Tengan el mismo padre.
	 * <li>Tengan el mismo estado.
	 * <li>Tengan la misma documentacion asociada, es decir, que no exista
	 * alguno que se haya documentado en otro grupo anteriormente.
	 * </ul>
	 * 
	 * @return Los cancelados que no cumplan las condiciones. Regresa una lista
	 *         vacia en caso de que todos sean documentables.
	 */
	public List<Cancelado> validarCanceladoDocumentable(
			List<CanceladoId> canceladoIds);

	/**
	 * Se valida que no haya una documentación asociada a la cancelación
	 * identificada con <code>canceladoIdPrincipal</code> más actual que
	 * <code>documentacion</code>. Si la hay regresa la documentación más
	 * actual. Si no, regresa nulo. La intención es guardar la integridad del
	 * flujo de estados de cancelación, lo que puede suceder si la pantalla del
	 * usuario no se refresca.
	 * 
	 * @param documentacion
	 * @param canceladoIdPrincipal
	 * @return
	 */
	public Documentacion validarCanceladoDocumentable(
			Documentacion documentacion, CanceladoId canceladoIdPrincipal);

	/**
	 * Pone el estado y el problema de <code>documentacion</code> y del grupo de
	 * cancelados identificados por <code>canceladoIds</code> a el estado y
	 * problema de la última documentación registrada para este grupo de
	 * cancelados. Posteriormente delega el guardado a
	 * <code>guardarDocumentacion(Documentacion, CanceladoId, List)</code>
	 * 
	 * @see IDocumentacionService#guardarDocumentacion(Documentacion,
	 *      CanceladoId, List)
	 * 
	 * @param documentacion
	 * @param canceladoIdPrincipal
	 * @param canceladoIds
	 */
	public void anadirDocumentacion(Documentacion documentacion,
			CanceladoId canceladoIdPrincipal, List<CanceladoId> canceladoIds);

	/**
	 * Se valida si los cancelados identificados por los valores en
	 * <code>canceladoIdSel</code> estan en estado registrado.
	 * 
	 * @param canceladoIdsSel
	 * @return Los cancelados que no esten en estado registrado o una lista
	 *         vacia si todos estan en ese estado.
	 */
	public List<Cancelado> validarCanceladoSoloRegistrado(
			List<CanceladoId> canceladoIdsSel);

	/**
	 * Valida si es factible cambiar el estado y el aŕea asociada (a través del
	 * problema asociado) del cancelado <code>canceladoPrincipal</code> y sus
	 * cancelados secundarios de acuerdo al grupo destino
	 * <code>tipoCanceladoDestino</code>, a los permisos del usuario
	 * <code>usuario</code>, al id del área <code>idAreaDestino</code> al que se
	 * asignara este cancelado y a la última documentación registrada
	 * <code>documentacionUltima</code>.
	 * 
	 * Si <code>idAreaDestino</code> es null, se calcula de la siguiente manera:
	 * <ul>
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#PRODUCCION} idAreaDestino es
	 * {@link Area#PRODUCCION}.
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#PARA_FU} idAreaDestino es
	 * {@link Area#FRENTE_UNICO}.
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#DESDE_CCR} idAreaDestino es la misma área a la
	 * que pertenece el problema asociado al cancelado.
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#DESDE_FU} idAreaDestino es
	 * {@link Area#FRENTE_UNICO}.
	 * <li>De cualquier otro modo, el movimiento es invalido.
	 * </ul>
	 * 
	 * 
	 * @param canceladoPrincipal
	 * @param documentacionUltima
	 * @param usuario
	 * @param tipoCanceladoDestino
	 *            Puede ser cualquiera de las siguientes constantes:
	 *            <ul>
	 *            <li> {@link IBitacoraService#PARA_FU}
	 *            <li> {@link IBitacoraService#PARA_CCR}
	 *            <li> {@link IBitacoraService#DESDE_FU}
	 *            <li> {@link IBitacoraService#DESDE_CCR}
	 *            <li> {@link IBitacoraService#EN_VALIDACION}
	 *            <li> {@link IBitacoraService#GSI}
	 *            </ul>
	 * @param idAreaDestino
	 * @return El id del estado al que se moveran los cancelados o null en caso
	 *         de que no sea valido el movimiento
	 */
	public Integer validarMovimientoCancelado(Cancelado canceladoPrincipal,
			Documentacion documentacionUltima, Usuario usuario,
			Integer tipoCanceladoDestino, Integer idAreaDestino);

	/**
	 * Se cambia el cancelado al estado <code>idEstadoDestino</code> y se asigna
	 * al área identificada por <code>idAreaSeleccionada</code>. Para esto
	 * último se le asigna a una documentación nueva el primer problema de esa
	 * área. Esta documentación tomará todos los datos de
	 * <code>documentacionUltima</code> salvo el estado, el problema, el tiempo
	 * de registro y el usuario.
	 * 
	 * Si <code>idAreaDestino</code> es null, se calcula de la siguiente manera:
	 * <ul>
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#PRODUCCION} idAreaDestino es
	 * {@link Area#PRODUCCION}.
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#PARA_FU} idAreaDestino es
	 * {@link Area#FRENTE_UNICO}.
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#DESDE_FU} idAreaDestino es la misma área a la que
	 * pertenece el problema asociado al cancelado.
	 * <li>Si <code>tipoCanceladoDestino</code> es
	 * {@link IBitacoraService#DESDE_FU} idAreaDestino es
	 * {@link Area#FRENTE_UNICO}.
	 * </ul>
	 * 
	 * Cuando el área del cancelado es diferente al área destino, el problema
	 * cambia al primer problema del área destino.
	 * 
	 * @param canceladoPrincipal
	 * @param documentacionUltima
	 * @param usuario
	 * @param idEstadoDestino
	 * @param idAreaDestino
	 * @param tipoCanceladoDestino
	 *            Puede ser cualquiera de las siguientes constantes:
	 *            <ul>
	 *            <li> {@link IBitacoraService#PARA_FU}
	 *            <li> {@link IBitacoraService#PARA_CCR}
	 *            <li> {@link IBitacoraService#DESDE_FU}
	 *            <li> {@link IBitacoraService#DESDE_CCR}
	 *            <li> {@link IBitacoraService#EN_VALIDACION}
	 *            <li> {@link IBitacoraService#GSI}
	 *            </ul>
	 * @return El cancelado principal actualizado de acuerdo al cambio de estado
	 *         y área asociada, ademas del nuevo último registro de
	 *         documentación
	 */
	public Cancelado moverCancelado(Cancelado canceladoPrincipal,
			Documentacion documentacionUltima, Usuario usuario,
			Integer idEstadoDestino, Integer idAreaDestino,
			Integer tipoCanceladoDestino);

	/**
	 * 
	 * @param documentacion
	 * @return Si todos los textos de documentación no contienen la constante
	 *         {@link IDocumentacionService#MARCADOR_CANCELADO_NO_DOCUMENTADO},
	 *         lo que implica que ha sido documentado el cancelado.
	 */
	public Boolean validarTextoDocumentacion(Documentacion documentacion);

	/**
	 * 
	 * @param documentacion
	 * @return Si el texto de recuperación de la documentación no contiene a la
	 *         constante
	 *         {@link IDocumentacionService#MARCADOR_CANCELADO_NO_DOCUMENTADO},
	 *         lo que implica que sido documentado el cancelado en su
	 *         recuperación.
	 */
	public Boolean validarTextoRecuperacion(Documentacion documentacion);

	/**
	 * Hace lo mismo que
	 * {@link IDocumentacionService#guardarDocumentacion(Documentacion, CanceladoId, List)}
	 * , salvo que guarda cada cancelado de manera independiente, es decir,
	 * guarda varias registros de <code>documentacion</code>, asociando uno para
	 * cada uno de los elementos de <code>canceladoIds</code>.
	 * 
	 * @param documentacion
	 * @param canceladoIds
	 */
	public void guardarDocumentacionIndividual(Documentacion documentacion,
			List<CanceladoId> canceladoIds);

	/**
	 * Lo mismo que
	 * {@link #guardarDocumentacion(Documentacion, CanceladoId, List)} , salvo
	 * que este método solo se utiliza para iniciar el proceso de documentación.
	 * 
	 * @param documentacion
	 * @param canceladoIdPrincipal
	 * @param canceladoIds
	 */
	public void guardarDocumentacionInicial(Documentacion documentacion,
			CanceladoId canceladoIdPrincipal, List<CanceladoId> canceladoIds);

	/**
	 * Asocia los cancelados que no han sido documentados con su documentación
	 * adelantada, si esta existe.
	 */
	public void sincronizarDocumentacionAdelantada();

}

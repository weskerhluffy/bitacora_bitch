package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "ultimasBitacoras", query = "from Bitacora b where b.cdEmpresa=:cdEmpresa order by b.fhCicloBitacora desc, b.cdTurno desc", hints = {}),
		@NamedQuery(name = "canceladosPendientesEmpresaAsignados", query = "select c from Cancelado c where c.cdEmpresa=:cdEmpresa and c.cdEstadoCan not in ("
				+ Estado.ENVIADO_UNICENTER + "," + Estado.REGISTRADO + ")", hints = {}),
		@NamedQuery(name = "canceladosPendientesEmpresaNoAsignados", query = "select c from Cancelado c where c.cdEmpresa=:cdEmpresa and c.cdEstadoCan="
				+ Estado.REGISTRADO, hints = {}),
		@NamedQuery(name = "canceladosPendientesBitacora", query = "select c from Cancelado as c inner join fetch c.documentacions as d where d.cdBitacora=:cdBitacora and d.cdEstado="
				+ Estado.ENVIADO_UNICENTER, hints = {}) })
@Entity
@Table(name = "TBP014_BITA_TAB")
public class Bitacora implements IModelo<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2453419634822923440L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "CD_BITACORA")
	private Integer cdBitacora;

	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;

	@Column(name = "CD_TURNO")
	private Integer cdTurno;

	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO_BITACORA")
	private Date fhCicloBitacora;

	@Column(name = "CD_USU_RECIBE")
	private Integer cdUsuRecibe;

	@Column(name = "CD_USU_DELEGA")
	private Integer cdUsuDelega;

	@Column(name = "CD_USU_AUTORIZA")
	private Integer cdUsuAutoriza;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_BITACORA")
	private Date tmBitacora;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_DELEGACION")
	private Date tmDelegacion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_EMPRESA", insertable = false, updatable = false, referencedColumnName = "CD_EMPRESA")
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_TURNO", insertable = false, updatable = false)
	private Turno turno;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_USU_DELEGA", insertable = false, updatable = false)
	private Usuario usuarioDelega;

	/**
	 * Lista de todas las documentaciones pertenecientes a esta bitácora
	 */
	@OrderBy("tmDocumentacion ASC")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bitacora")
	private List<Documentacion> documentacions = null;

	/**
	 * Lista de documentaciones pertenecientes a esta bitácora y que ya fueron
	 * enviadas a unicenter. Debido a que despues de que el cancelado pasa al
	 * estado de enviado a unicenter, se asume que cada cancelado tiene solo una
	 * documentación marcada con ese estado.
	 */
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@LazyCollection(LazyCollectionOption.TRUE)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("tmDocumentacion ASC")
	@Where(clause = "cd_estado=" + Estado.ENVIADO_UNICENTER)
	@OneToMany(mappedBy = "bitacora")
	private List<Documentacion> documentacionesEnviadas = null;

	/**
	 * Lista de documentaciones pertenecientes a esta bitácora y que no han sido
	 * enviadas a unicenter
	 */
	@OrderBy("tmDocumentacion ASC")
	@Where(clause = "cd_estado!=" + Estado.ENVIADO_UNICENTER)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bitacora")
	private List<Documentacion> documentacionesNoEnviadas = null;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "bitacora")
	private List<Mensaje> mensajes;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "bitacora")
	private List<DocumentacionHoldeado> documentacionHoldeados;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "bitacora")
	private List<DocumentacionSeguimientoProcesoEspecial> documentacionEspeciales;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "bitacora")
	private List<DocumentacionProcesoRetrasado> documentacionProcesoRetrasados;

	@Transient
	@Override
	public Integer getId() {
		return cdBitacora;
	}

	@Override
	public void setId(Integer pId) {
		cdBitacora = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public Empresa getEmpresa() {
		return this.empresa;
	}

	@JSON(serialize = false, deserialize = false)
	public Usuario getUsuarioDelega() {
		return usuarioDelega;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder
				.setExcludeFieldNames(new String[] { "empresa",
						"documentacions", "documentacionesEnviadas",
						"documentacionesNoEnviadas", "turno", "usuarioDelega",
						"mensajes", "documentacionHoldeados",
						"documentacionEspeciales" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdBitacora() {
		return cdBitacora;
	}

	public void setCdBitacora(Integer cdBitacora) {
		this.cdBitacora = cdBitacora;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public Integer getCdTurno() {
		return cdTurno;
	}

	public void setCdTurno(Integer cdTurno) {
		this.cdTurno = cdTurno;
	}

	public Date getFhCicloBitacora() {
		return fhCicloBitacora;
	}

	public void setFhCicloBitacora(Date fhCicloBitacora) {
		this.fhCicloBitacora = fhCicloBitacora;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Date getTmBitacora() {
		return tmBitacora;
	}

	public void setTmBitacora(Date tmBitacora) {
		this.tmBitacora = tmBitacora;
	}

	public Date getTmDelegacion() {
		return tmDelegacion;
	}

	public void setTmDelegacion(Date tmDelegacion) {
		this.tmDelegacion = tmDelegacion;
	}

	public List<Documentacion> getDocumentacions() {
		return documentacions;
	}

	public void setDocumentacions(List<Documentacion> documentacions) {
		this.documentacions = documentacions;
	}

	public List<Documentacion> getDocumentacionesEnviadas() {
		return documentacionesEnviadas;
	}

	public void setDocumentacionesEnviadas(
			List<Documentacion> documentacionesEnviadas) {
		this.documentacionesEnviadas = documentacionesEnviadas;
	}

	public List<Documentacion> getDocumentacionesNoEnviadas() {
		return documentacionesNoEnviadas;
	}

	public void setDocumentacionesNoEnviadas(
			List<Documentacion> documentacionesNoEnviadas) {
		this.documentacionesNoEnviadas = documentacionesNoEnviadas;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setUsuarioDelega(Usuario usuarioDelega) {
		this.usuarioDelega = usuarioDelega;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public List<DocumentacionHoldeado> getDocumentacionHoldeados() {
		return documentacionHoldeados;
	}

	public void setDocumentacionHoldeados(
			List<DocumentacionHoldeado> documentacionHoldeados) {
		this.documentacionHoldeados = documentacionHoldeados;
	}

	public Integer getCdUsuRecibe() {
		return cdUsuRecibe;
	}

	public void setCdUsuRecibe(Integer cdUsuRecibe) {
		this.cdUsuRecibe = cdUsuRecibe;
	}

	public Integer getCdUsuDelega() {
		return cdUsuDelega;
	}

	public void setCdUsuDelega(Integer cdUsuDelega) {
		this.cdUsuDelega = cdUsuDelega;
	}

	public Integer getCdUsuAutoriza() {
		return cdUsuAutoriza;
	}

	public void setCdUsuAutoriza(Integer cdUsuAutoriza) {
		this.cdUsuAutoriza = cdUsuAutoriza;
	}

	public List<DocumentacionSeguimientoProcesoEspecial> getDocumentacionEspeciales() {
		return documentacionEspeciales;
	}

	public void setDocumentacionEspeciales(
			List<DocumentacionSeguimientoProcesoEspecial> documentacionEspeciales) {
		this.documentacionEspeciales = documentacionEspeciales;
	}

	public List<DocumentacionProcesoRetrasado> getDocumentacionProcesoRetrasados() {
		return documentacionProcesoRetrasados;
	}

	public void setDocumentacionProcesoRetrasados(
			List<DocumentacionProcesoRetrasado> documentacionProcesoRetrasados) {
		this.documentacionProcesoRetrasados = documentacionProcesoRetrasados;
	}

}

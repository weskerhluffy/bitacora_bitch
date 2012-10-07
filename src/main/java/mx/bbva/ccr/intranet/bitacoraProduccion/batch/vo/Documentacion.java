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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

@NamedQueries({ @NamedQuery(name = "canceladosDocumentacionAdelantada", query = "select c from Cancelado as c "
		+ "inner join fetch c.documentacions as d "
		+ "where c.cdEstadoCan="
		+ Estado.REGISTRADO) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DataTransferObject(javascript = "Documentacion")
@Entity
@Table(name = "TBP012_DOC_TAB")
public class Documentacion implements Serializable, IModelo<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1703074320302107545L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@RemoteProperty
	@Id
	@Column(name = "CD_DOCUMENTACION")
	Integer cdDocumentacion;

	@RemoteProperty
	@Column(name = "CD_USUARIO")
	Integer cdUsuario;
	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_DOCUMENTACION")
	Date tmDocumentacion;

	@RemoteProperty
	@Column(name = "CD_PROBLEMA")
	Integer cdProblema;
	@RemoteProperty
	@Column(name = "CD_ESTADO")
	Integer cdEstado;
	@RemoteProperty
	@Column(name = "TX_RECUPERACION")
	String txRecuperacion;
	@RemoteProperty
	@Column(name = "TX_IMPACTO")
	String txImpacto;
	@RemoteProperty
	@Column(name = "TX_SINTOMA")
	String txSintoma;
	@RemoteProperty
	@Column(name = "CD_BITACORA")
	Integer cdBitacora;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CD_BITACORA", referencedColumnName = "CD_BITACORA", insertable = false, updatable = false) })
	Bitacora bitacora;

	@RemoteProperty
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_PROBLEMA", updatable = false, insertable = false)
	Problema problema;

	@RemoteProperty
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CD_USUARIO", referencedColumnName = "CD_USUARIO", insertable = false, updatable = false) })
	Usuario usuario;

	/**
	 * Lista de cancelados, que idealmente debe ser solo 1 aun si esta repetido,
	 * que contiene al cancelado principal de todos en los que esta
	 * documentación aplica
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@Fetch(FetchMode.SUBSELECT)
	@Where(clause = "cd_empresa_pri is null")
	@LazyCollection(LazyCollectionOption.TRUE)
	@ManyToMany
	@JoinTable(name = "TBP011_CAN_DOC_TAB", joinColumns = { @JoinColumn(name = "CD_DOCUMENTACION", referencedColumnName = "CD_DOCUMENTACION", insertable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "NB_PROCESO", referencedColumnName = "NB_PROCESO", insertable = false, updatable = false),
			@JoinColumn(name = "FH_CICLO", referencedColumnName = "FH_CICLO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_PASO", referencedColumnName = "CD_PASO", insertable = false, updatable = false),
			@JoinColumn(name = "NB_GRUPO", referencedColumnName = "NB_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	private List<Cancelado> canceladoPrincipal = null;

	/**
	 * Seta de cancelados en los que esta documentación aplica
	 */
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("tmEvento")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TBP011_CAN_DOC_TAB", joinColumns = { @JoinColumn(name = "CD_DOCUMENTACION", referencedColumnName = "CD_DOCUMENTACION", insertable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "NB_PROCESO", referencedColumnName = "NB_PROCESO", insertable = false, updatable = false),
			@JoinColumn(name = "FH_CICLO", referencedColumnName = "FH_CICLO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_PASO", referencedColumnName = "CD_PASO", insertable = false, updatable = false),
			@JoinColumn(name = "NB_GRUPO", referencedColumnName = "NB_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	private List<Cancelado> cancelados = null;

	@OrderBy("tmEnvioEvid")
	@OneToMany(mappedBy = "documentacion")
	private List<Evidencia> evidencias = null;

	public Documentacion() {
	}

	public Documentacion(Documentacion documentacion) {
		this.cdUsuario = documentacion.cdUsuario;
		this.tmDocumentacion = documentacion.tmDocumentacion;
		this.cdProblema = documentacion.cdProblema;
		this.cdEstado = documentacion.cdEstado;
		this.txRecuperacion = documentacion.txRecuperacion;
		this.txImpacto = documentacion.txImpacto;
		this.txSintoma = documentacion.txSintoma;
		this.cdBitacora = documentacion.cdBitacora;
	}

	@Transient
	@Override
	public Integer getId() {
		return cdDocumentacion;
	}

	@Override
	public void setId(Integer pId) {
		this.cdDocumentacion = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public Bitacora getBitacora() {
		return bitacora;
	}

	@JSON(serialize = false, deserialize = false)
	public Problema getProblema() {
		return problema;
	}

	@JSON(serialize = false, deserialize = false)
	public Usuario getUsuario() {
		return usuario;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Cancelado> getCancelados() {
		return cancelados;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Cancelado> getCanceladoPrincipal() {
		return canceladoPrincipal;
	}

	@JSON(serialize = false, deserialize = false)
	@Transient
	public Evidencia getUltimaEvidencia() {
		return (getEvidencias() != null && getEvidencias().size() > 0) ? getEvidencias()
				.get(getEvidencias().size() - 1) : null;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"bitacora", "problema", "cancelados", "canceladoPrincipal",
				"usuario", "evidencias" });
		return reflectionToStringBuilder.toString();
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getCdDocumentacion() {
		return cdDocumentacion;
	}

	public void setCdDocumentacion(Integer cdDocumentacion) {
		this.cdDocumentacion = cdDocumentacion;
	}

	public Date getTmDocumentacion() {
		return tmDocumentacion;
	}

	public void setTmDocumentacion(Date tmDocumentacion) {
		this.tmDocumentacion = tmDocumentacion;
	}

	public Integer getCdProblema() {
		return cdProblema;
	}

	public void setCdProblema(Integer cdProblema) {
		this.cdProblema = cdProblema;
	}

	public Integer getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(Integer cdEstado) {
		this.cdEstado = cdEstado;
	}

	public String getTxRecuperacion() {
		return txRecuperacion;
	}

	public void setTxRecuperacion(String txRecuperacion) {
		this.txRecuperacion = txRecuperacion;
	}

	public String getTxImpacto() {
		return txImpacto;
	}

	public void setTxImpacto(String txImpacto) {
		this.txImpacto = txImpacto;
	}

	public String getTxSintoma() {
		return txSintoma;
	}

	public void setTxSintoma(String txSintoma) {
		this.txSintoma = txSintoma;
	}

	public Integer getCdBitacora() {
		return cdBitacora;
	}

	public void setCdBitacora(Integer cdBitacora) {
		this.cdBitacora = cdBitacora;
	}

	public void setBitacora(Bitacora bitacora) {
		this.bitacora = bitacora;
	}

	public void setCancelados(List<Cancelado> cancelados) {
		this.cancelados = cancelados;
	}

	public void setCanceladoPrincipal(List<Cancelado> canceladoPrincipal) {
		this.canceladoPrincipal = canceladoPrincipal;
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public List<Evidencia> getEvidencias() {
		return evidencias;
	}

	public void setEvidencias(List<Evidencia> evidencias) {
		this.evidencias = evidencias;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.opensymphony.xwork2.conversion.annotations.ConversionType;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DataTransferObject(javascript = "Cancelado")
@Entity
@Table(name = "TBP006_CANCEL_TAB")
public class Cancelado implements IModelo<CanceladoId>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4094724466668925957L;

	@RemoteProperty
	@EmbeddedId
	CanceladoId id;

	@RemoteProperty
	@Column(name = "CD_EMPRESA", updatable = false, insertable = false)
	String cdEmpresa;
	@RemoteProperty
	@Column(name = "NB_GRUPO", updatable = false, insertable = false)
	String nbGrupo;
	@RemoteProperty
	@Column(name = "NB_PROCESO", updatable = false, insertable = false)
	String nbProceso;
	@RemoteProperty
	@Column(name = "CD_PASO", updatable = false, insertable = false)
	String cdPaso;
	@RemoteProperty
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO", updatable = false, insertable = false)
	Date fhCiclo;
	@RemoteProperty
	@Column(name = "CD_ORIGEN", updatable = false, insertable = false)
	Integer cdOrigen;

	@RemoteProperty
	@Column(name = "CD_ESTADO")
	String cdEstado;

	@RemoteProperty
	@Column(name = "CD_PROCESO")
	String cdProceso;

	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_EVENTO")
	Date tmEvento;

	@RemoteProperty
	@Column(name = "CD_CICLO")
	String cdCiclo;

	@RemoteProperty
	@Column(name = "CD_ERROR")
	String cdError;

	@RemoteProperty
	@Column(name = "CD_PROBLEMA")
	Integer cdProblema;

	@RemoteProperty
	@Column(name = "CD_ESTADO_CAN")
	Integer cdEstadoCan;

	@RemoteProperty
	@Column(name = "CD_CANCELADO", insertable = false, updatable = false)
	String cdCancelado;

	// Inicia referencia al cancelado padre
	@RemoteProperty
	@Column(name = "CD_EMPRESA_PRI")
	String cdEmpresaPri;
	@RemoteProperty
	@Column(name = "NB_GRUPO_PRI")
	String nbGrupoPri;
	@RemoteProperty
	@Column(name = "NB_PROCESO_PRI")
	String nbProcesoPri;
	@RemoteProperty
	@Column(name = "CD_PASO_PRI")
	String cdPasoPri;
	@RemoteProperty
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO_PRI")
	Date fhCicloPri;
	@RemoteProperty
	@Column(name = "CD_ORIGEN_PRI")
	Integer cdOrigenPri;
	// Termina referencia al cancelado padre

	@LazyToOne(LazyToOneOption.FALSE)
	@Fetch(FetchMode.JOIN)
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	private Origen origen;

	@LazyToOne(LazyToOneOption.FALSE)
	@Fetch(FetchMode.JOIN)
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "CD_ESTADO_CAN", referencedColumnName = "CD_ESTADO", insertable = false, updatable = false) })
	private Estado estado;

	@LazyToOne(LazyToOneOption.FALSE)
	@Fetch(FetchMode.JOIN)
	@RemoteProperty
	@ManyToOne
	@JoinColumn(name = "CD_PROBLEMA", updatable = false, insertable = false)
	private Problema problema;

	/**
	 * Referencia al cancelado principal o nulo si no ha sido documentado o se
	 * documento solo
	 */
	@LazyToOne(LazyToOneOption.FALSE)
	@Fetch(FetchMode.SELECT)
	@RemoteProperty
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "CD_EMPRESA_PRI", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "NB_PROCESO_PRI", referencedColumnName = "NB_PROCESO", insertable = false, updatable = false),
			@JoinColumn(name = "FH_CICLO_PRI", referencedColumnName = "FH_CICLO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_PASO_PRI", referencedColumnName = "CD_PASO", insertable = false, updatable = false),
			@JoinColumn(name = "NB_GRUPO_PRI", referencedColumnName = "NB_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN_PRI", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	private Cancelado canceladoPrincipal;

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	@RemoteProperty
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "canceladoPrincipal")
	private List<Cancelado> canceladosSecundarios;

	/**
	 * Lista de documentaciones donde la documentaciones
	 */
	@OrderBy("tmDocumentacion")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@ManyToMany
	@JoinTable(name = "TBP011_CAN_DOC_TAB",

	inverseJoinColumns = { @JoinColumn(name = "CD_DOCUMENTACION", referencedColumnName = "CD_DOCUMENTACION", insertable = false, updatable = false) },

	joinColumns = {
			@JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "NB_PROCESO", referencedColumnName = "NB_PROCESO", insertable = false, updatable = false),
			@JoinColumn(name = "FH_CICLO", referencedColumnName = "FH_CICLO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_PASO", referencedColumnName = "CD_PASO", insertable = false, updatable = false),
			@JoinColumn(name = "NB_GRUPO", referencedColumnName = "NB_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	private List<Documentacion> documentacions;

	public Cancelado() {
	}

	public CanceladoId getId() {
		return id;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.CanceladoIdConverter", type = ConversionType.APPLICATION)
	public void setId(CanceladoId pId) {
		id = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Documentacion> getDocumentacions() {
		return documentacions;
	}

	@JSON(serialize = false, deserialize = false)
	public Estado getEstado() {
		return estado;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Cancelado> getCanceladosSecundarios() {
		return canceladosSecundarios;
	}

	@JSON(serialize = false, deserialize = false)
	public Problema getProblema() {
		return problema;
	}

	/**
	 * 
	 * @return La última documentación
	 */
	@RemoteProperty
	@Transient
	public Documentacion getUltimaDocumentacion() {
		if (getDocumentacions() != null && getDocumentacions().size() > 0) {
			return documentacions.get(documentacions.size() - 1);
		} else {
			return new Documentacion();
		}
	}

	/**
	 * 
	 * @return El c�digo del cancelado padre o nulo si no tiene padre. En caso
	 *         de que este cancelado sea padre, su mismo codigo.
	 */
	@RemoteProperty
	@Transient
	public String getCdCanceladoPrincipal() {
		String cdCanceladoPrincipal = null;
		if (canceladoPrincipal == null) {
			if (cdEstadoCan != Estado.REGISTRADO) {
				cdCanceladoPrincipal = cdCancelado;
			}
		} else {
			cdCanceladoPrincipal = getCanceladoPrincipal().getCdCancelado();
		}
		return cdCanceladoPrincipal;
	}

	/**
	 * 
	 * @return El id del cancelado padre o nulo si no tiene padre. En caso de
	 *         que este cancelado sea padre, su mismo id.
	 */
	@RemoteProperty
	@Transient
	public CanceladoId getCanceladoIdPrincipal() {
		CanceladoId canceladoId = null;
		if (canceladoPrincipal == null) {
			if (cdEstadoCan != Estado.REGISTRADO) {
				canceladoId = id;
			}
		} else {
			canceladoId = getCanceladoPrincipal().getId();
		}
		return canceladoId;
	}

	@Override
	public boolean equals(Object objeto) {
		return EqualsBuilder.reflectionEquals(this, objeto);
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"documentacions", "empresa", "estado", "origen",
				"canceladoPrincipal", "canceladosSecundarios", "problema",
				"canceladoDocumentacions" });
		return reflectionToStringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public Cancelado getCanceladoPrincipal() {
		return canceladoPrincipal;
	}

	public void setCanceladoPrincipal(Cancelado canceladoPrincipal) {
		this.canceladoPrincipal = canceladoPrincipal;
	}

	public String getCdCancelado() {
		return cdCancelado;
	}

	public void setCdCancelado(String cdCancelado) {
		this.cdCancelado = cdCancelado;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getNbGrupo() {
		return nbGrupo;
	}

	public void setNbGrupo(String nbGrupo) {
		this.nbGrupo = nbGrupo;
	}

	public String getNbProceso() {
		return nbProceso;
	}

	public void setNbProceso(String nbProceso) {
		this.nbProceso = nbProceso;
	}

	public String getCdPaso() {
		return cdPaso;
	}

	public void setCdPaso(String cdPaso) {
		this.cdPaso = cdPaso;
	}

	public Date getFhCiclo() {
		return fhCiclo;
	}

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
	}

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public String getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(String cdEstado) {
		this.cdEstado = cdEstado;
	}

	public String getCdProceso() {
		return cdProceso;
	}

	public void setCdProceso(String cdProceso) {
		this.cdProceso = cdProceso;
	}

	public Date getTmEvento() {
		return tmEvento;
	}

	public void setTmEvento(Date tmEvento) {
		this.tmEvento = tmEvento;
	}

	public String getCdCiclo() {
		return cdCiclo;
	}

	public void setCdCiclo(String cdCiclo) {
		this.cdCiclo = cdCiclo;
	}

	public String getCdError() {
		return cdError;
	}

	public void setCdError(String cdError) {
		this.cdError = cdError;
	}

	public Integer getCdProblema() {
		return cdProblema;
	}

	public void setCdProblema(Integer cdProblema) {
		this.cdProblema = cdProblema;
	}

	public Integer getCdEstadoCan() {
		return cdEstadoCan;
	}

	public void setCdEstadoCan(Integer cdEstadoCan) {
		this.cdEstadoCan = cdEstadoCan;
	}

	public String getCdEmpresaPri() {
		return cdEmpresaPri;
	}

	public void setCdEmpresaPri(String cdEmpresaPri) {
		this.cdEmpresaPri = cdEmpresaPri;
	}

	public String getNbGrupoPri() {
		return nbGrupoPri;
	}

	public void setNbGrupoPri(String nbGrupoPri) {
		this.nbGrupoPri = nbGrupoPri;
	}

	public String getNbProcesoPri() {
		return nbProcesoPri;
	}

	public void setNbProcesoPri(String nbProcesoPri) {
		this.nbProcesoPri = nbProcesoPri;
	}

	public String getCdPasoPri() {
		return cdPasoPri;
	}

	public void setCdPasoPri(String cdPasoPri) {
		this.cdPasoPri = cdPasoPri;
	}

	public Date getFhCicloPri() {
		return fhCicloPri;
	}

	public void setFhCicloPri(Date fhCicloPri) {
		this.fhCicloPri = fhCicloPri;
	}

	public Integer getCdOrigenPri() {
		return cdOrigenPri;
	}

	public void setCdOrigenPri(Integer cdOrigenPri) {
		this.cdOrigenPri = cdOrigenPri;
	}

	public void setDocumentacions(List<Documentacion> documentacions) {
		this.documentacions = documentacions;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setOrigen(Origen origen) {
		this.origen = origen;
	}

	public void setCanceladosSecundarios(List<Cancelado> canceladosSecundarios) {
		this.canceladosSecundarios = canceladosSecundarios;
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public Origen getOrigen() {
		return origen;
	}

}

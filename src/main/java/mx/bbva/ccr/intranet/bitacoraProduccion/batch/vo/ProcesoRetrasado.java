package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@NamedQueries({
		@NamedQuery(name = "retrasadosDocumentados", query = "select distinct p "
				+ "from ProcesoRetrasado p "
				+ "inner join fetch p.documentacionProcesoRetrasados as d "
				+ "where p.cdEmpresa=:cdEmpresa "
				+ "and (p.tmCerrado is null or d.cdBitacora=:cdBitacora) ", hints = {}),
		@NamedQuery(name = "retrasadosNoDocumentados", query = "select p "
				+ "from ProcesoRetrasado p "
				+ "where p.documentacionProcesoRetrasados.size=0 "
				+ "and p.cdEmpresa=:cdEmpresa", hints = {}) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP017_PRO_RET_TAB")
public class ProcesoRetrasado implements IModelo<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5709689456821731290L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_PRO_RET")
	private Integer cdProRet;

	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	private Date fhCiclo;

	@Column(name = "NB_PROCESO")
	private String nbProceso;

	@Column(name = "NB_GRUPO")
	private String nbGrupo;

	@Column(name = "CD_CICLO")
	private String cdCiclo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_INICIO")
	private Date tmInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_CERRADO")
	private Date tmCerrado;

	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;

	@Column(name = "CD_ORIGEN")
	private Integer cdOrigen;

	@LazyToOne(LazyToOneOption.FALSE)
	@ManyToOne
	@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false)
	private Origen origen;

	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("tmDocumentacion asc")
	@ManyToMany(mappedBy = "procesoRetrasados")
	private List<DocumentacionProcesoRetrasado> documentacionProcesoRetrasados;

	@Override
	public Integer getId() {
		return cdProRet;
	}

	@Override
	public void setId(Integer pId) {
		cdProRet = pId;
	}

	@Transient
	public DocumentacionProcesoRetrasado getUltimaDocumentacionProcesoRetrasado() {
		return (getDocumentacionProcesoRetrasados() != null && getDocumentacionProcesoRetrasados()
				.size() > 0) ? getDocumentacionProcesoRetrasados().get(
				getDocumentacionProcesoRetrasados().size() - 1) : null;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] { "origen",
				"documentacionProcesoRetrasados" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdProRet() {
		return cdProRet;
	}

	public void setCdProRet(Integer cdProRet) {
		this.cdProRet = cdProRet;
	}

	public Date getFhCiclo() {
		return fhCiclo;
	}

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
	}

	public String getNbProceso() {
		return nbProceso;
	}

	public void setNbProceso(String nbProceso) {
		this.nbProceso = nbProceso;
	}

	public String getNbGrupo() {
		return nbGrupo;
	}

	public void setNbGrupo(String nbGrupo) {
		this.nbGrupo = nbGrupo;
	}

	public String getCdCiclo() {
		return cdCiclo;
	}

	public void setCdCiclo(String cdCiclo) {
		this.cdCiclo = cdCiclo;
	}

	public Date getTmInicio() {
		return tmInicio;
	}

	public void setTmInicio(Date tmInicio) {
		this.tmInicio = tmInicio;
	}

	public Date getTmCerrado() {
		return tmCerrado;
	}

	public void setTmCerrado(Date tmCerrado) {
		this.tmCerrado = tmCerrado;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public Origen getOrigen() {
		return origen;
	}

	public void setOrigen(Origen origen) {
		this.origen = origen;
	}

	public List<DocumentacionProcesoRetrasado> getDocumentacionProcesoRetrasados() {
		return documentacionProcesoRetrasados;
	}

	public void setDocumentacionProcesoRetrasados(
			List<DocumentacionProcesoRetrasado> documentacionProcesoRetrasados) {
		this.documentacionProcesoRetrasados = documentacionProcesoRetrasados;
	}

}

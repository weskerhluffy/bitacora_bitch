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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
import org.hibernate.annotations.Where;

import com.opensymphony.xwork2.conversion.annotations.ConversionType;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@NamedQueries({ @NamedQuery(name = "actualizarEmpresa", query = "update Empresa e set e.cdEmpresa=:cdEmpresaNuevo where e.cdEmpresa=:cdEmpresa") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP001_EMPRESA_CAT", schema = "GBP001")
public class Empresa implements Serializable, IModelo<String> {
	public static final String MEXICO = "M";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8446804959638955238L;

	@Id
	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NU_EMPRESA", insertable = false, updatable = false)
	private Integer nuEmpresa;

	@Column(name = "NB_EMPRESA")
	private String nbEmpresa;
	@Column(name = "CD_GSI")
	private String cdGsi;
	@Column(name = "NB_GSI")
	private String nbGsi;
	@Column(name = "CD_EVIDENCIA")
	private String cdEvidencia;
	@Temporal(TemporalType.TIME)
	@Column(name = "HM_DIF_HORARIA")
	private Date hmDifHoraria;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_EMPRESA_ELIM")
	private Date tmEmpresaElim;
	@Column(name = "ST_DIF_HORARIA_POS")
	private Boolean stDifHorariaPos;

	@Transient
	private String cdEmpresaMutable;
	/**
	 * Los cancelados de esta empresa que no hayan sido enviados a unicenter
	 * aun.
	 */
	@Where(clause = "cd_estado_can!=" + Estado.ENVIADO_UNICENTER)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false) })
	private List<Cancelado> canceladosPendientes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	private List<Bitacora> bitacoras;

	/**
	 * Los procesos especiales de esta empresa
	 */
	@LazyCollection(LazyCollectionOption.TRUE)
	@Fetch(FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(mappedBy = "empresa")
	private List<ProcesoEspecial> procesosEspeciales = null;

	/**
	 * Los dias inhabiles de esta empresa
	 */
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	@JoinColumns({ @JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false) })
	private List<DiaInhabil> diasInhabiles = null;

	@Transient
	@Override
	public String getId() {
		return cdEmpresa;
	}

	@Override
	public void setId(String pId) {
		cdEmpresa = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Bitacora> getBitacoras() {
		return bitacoras;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Cancelado> getCanceladosPendientes() {
		return canceladosPendientes;
	}

	@JSON(serialize = false, deserialize = false)
	public List<ProcesoEspecial> getProcesosEspeciales() {
		return procesosEspeciales;
	}

	@JSON(serialize = false, deserialize = false)
	public List<DiaInhabil> getDiasInhabiles() {
		return diasInhabiles;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.TiempoConverter", type = ConversionType.CLASS)
	public void setHmDifHoraria(Date hmDifHoraria) {
		this.hmDifHoraria = hmDifHoraria;
	}

	public String getCdEmpresaMutable() {
		if (cdEmpresaMutable == null) {
			cdEmpresaMutable = cdEmpresa;
		}
		return cdEmpresaMutable;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"canceladosPendientes", "bitacoras", "diasInhabiles ",
				"procesosEspeciales" });
		return reflectionToStringBuilder.toString();
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getNbEmpresa() {
		return nbEmpresa;
	}

	public void setNbEmpresa(String nbEmpresa) {
		this.nbEmpresa = nbEmpresa;
	}

	public String getCdGsi() {
		return cdGsi;
	}

	public void setCdGsi(String cdGsi) {
		this.cdGsi = cdGsi;
	}

	public String getNbGsi() {
		return nbGsi;
	}

	public void setNbGsi(String nbGsi) {
		this.nbGsi = nbGsi;
	}

	public String getCdEvidencia() {
		return cdEvidencia;
	}

	public void setCdEvidencia(String cdEvidencia) {
		this.cdEvidencia = cdEvidencia;
	}

	public void setCanceladosPendientes(List<Cancelado> canceladosPendientes) {
		this.canceladosPendientes = canceladosPendientes;
	}

	public void setBitacoras(List<Bitacora> bitacoras) {
		this.bitacoras = bitacoras;
	}

	public Integer getNuEmpresa() {
		return nuEmpresa;
	}

	public void setNuEmpresa(Integer nuEmpresa) {
		this.nuEmpresa = nuEmpresa;
	}

	public Date getTmEmpresaElim() {
		return tmEmpresaElim;
	}

	public void setTmEmpresaElim(Date tmEmpresaElim) {
		this.tmEmpresaElim = tmEmpresaElim;
	}

	public Date getHmDifHoraria() {
		return hmDifHoraria;
	}

	public Boolean getStDifHorariaPos() {
		return stDifHorariaPos;
	}

	public void setStDifHorariaPos(Boolean stDifHorariaPos) {
		this.stDifHorariaPos = stDifHorariaPos;
	}

	public void setCdEmpresaMutable(String cdEmpresaMutable) {
		this.cdEmpresaMutable = cdEmpresaMutable;
	}

	public void setProcesosEspeciales(List<ProcesoEspecial> procesosEspeciales) {
		this.procesosEspeciales = procesosEspeciales;
	}

	public void setDiasInhabiles(List<DiaInhabil> diasInhabiles) {
		this.diasInhabiles = diasInhabiles;
	}

}

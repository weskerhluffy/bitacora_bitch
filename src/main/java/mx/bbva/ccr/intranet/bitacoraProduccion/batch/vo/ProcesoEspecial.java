package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import com.opensymphony.xwork2.conversion.annotations.ConversionType;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

/**
 * @author Hiram Ernesto Alvarado Gaspar
 * @author Juan Carlos Ruiz Guadarrama
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP008_PRO_ESP_CAT")
public class ProcesoEspecial implements IModelo<Integer>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2528528518227541505L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@RemoteProperty
	@Id
	@Column(name = "CD_PRO_ESP")
	Integer cdProEsp;

	@RemoteProperty
	@Column(name = "NB_PROCESO")
	String nbProceso;

	@RemoteProperty
	@Column(name = "CD_EMPRESA")
	String cdEmpresa;

	@RemoteProperty
	@Column(name = "NB_GRUPO")
	String nbGrupo;

	@RemoteProperty
	@Column(name = "CD_ORIGEN")
	String cdOrigen;

	@RemoteProperty
	@Column(name = "NB_PROC_ESP")
	String nbProcEsp;

	@RemoteProperty
	@Temporal(TemporalType.TIME)
	@Column(name = "HM_TECNICA")
	Date hmTecnica;

	@RemoteProperty
	@Temporal(TemporalType.TIME)
	@Column(name = "HM_SERVICIO")
	Date hmServicio;

	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FH_PROCESO_ELIM")
	Date fhProcesoEliminado;

	@RemoteProperty
	@Temporal(TemporalType.TIME)
	@Column(name = "HM_TECNICA_FM")
	Date hmTecnicaFinDeMes;

	@RemoteProperty
	@Temporal(TemporalType.TIME)
	@Column(name = "HM_SERVICIO_FM")
	Date hmServicioFinDeMes;

	@RemoteProperty
	@Column(name = "CD_TIP_PRO_ESP")
	Integer cdTipProEsp;

	@RemoteProperty
	@Column(name = "ST_FH_INI_DIA_SIG")
	Boolean stFhIniDiaSig;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false) })
	private Empresa empresa;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	Origen origen;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CD_TIP_PRO_ESP", referencedColumnName = "CD_TIP_PRO_ESP", insertable = false, updatable = false) })
	TipoProcesoEspecial tipoProcesoEspecial;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procesoEspecial")
	List<SeguimientoProcesoEspecial> seguimientoProcesoEspecials;

	public ProcesoEspecial(Integer cdProEsp) {
		this.cdProEsp = cdProEsp;
	}

	public ProcesoEspecial() {

	}

	@Transient
	@Override
	public Integer getId() {
		return cdProEsp;
	}

	@Override
	public void setId(Integer pId) {
		this.cdProEsp = pId;

	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.TiempoConverter", type = ConversionType.CLASS)
	public void setHmTecnica(Date hmTecnica) {
		this.hmTecnica = hmTecnica;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.TiempoConverter", type = ConversionType.CLASS)
	public void setHmServicio(Date hmServicio) {
		this.hmServicio = hmServicio;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.TiempoConverter", type = ConversionType.CLASS)
	public void setHmTecnicaFinDeMes(Date hmTecnicaFinDeMes) {
		this.hmTecnicaFinDeMes = hmTecnicaFinDeMes;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.TiempoConverter", type = ConversionType.CLASS)
	public void setHmServicioFinDeMes(Date hmServicioFinDeMes) {
		this.hmServicioFinDeMes = hmServicioFinDeMes;
	}

	public String getNbProcEsp() {
		return nbProcEsp;
	}

	public void setNbProcEsp(String nbProcEsp) {
		this.nbProcEsp = nbProcEsp;
	}

	public Date getHmTecnica() {
		return hmTecnica;
	}

	public Date getHmServicio() {
		return hmServicio;
	}

	public Date getFhProcesoEliminado() {
		return fhProcesoEliminado;
	}

	public void setFhProcesoEliminado(Date fhProcesoEliminado) {
		this.fhProcesoEliminado = fhProcesoEliminado;
	}

	public Date getHmTecnicaFinDeMes() {
		return hmTecnicaFinDeMes;
	}

	public Date getHmServicioFinDeMes() {
		return hmServicioFinDeMes;
	}

	public Integer getCdTipProEsp() {
		return cdTipProEsp;
	}

	public void setCdTipProEsp(Integer cdTipProEsp) {
		this.cdTipProEsp = cdTipProEsp;
	}

	public List<SeguimientoProcesoEspecial> getSeguimientoProcesoEspecials() {
		return seguimientoProcesoEspecials;
	}

	public void setSeguimientoProcesoEspecials(
			List<SeguimientoProcesoEspecial> seguimientoProcesoEspecials) {
		this.seguimientoProcesoEspecials = seguimientoProcesoEspecials;
	}

	public Integer getCdProEsp() {
		return cdProEsp;
	}

	public void setCdProEsp(Integer cdProEsp) {
		this.cdProEsp = cdProEsp;
	}

	public String getNbProceso() {
		return nbProceso;
	}

	public void setNbProceso(String nbProceso) {
		this.nbProceso = nbProceso;
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

	public String getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(String cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public Origen getOrigen() {
		return origen;
	}

	public void setOrigen(Origen origen) {
		this.origen = origen;
	}

	public TipoProcesoEspecial getTipoProcesoEspecial() {
		return tipoProcesoEspecial;
	}

	public void setTipoProcesoEspecial(TipoProcesoEspecial tipoProcesoEspecial) {
		this.tipoProcesoEspecial = tipoProcesoEspecial;
	}

	public Boolean getStFhIniDiaSig() {
		return stFhIniDiaSig;
	}

	public void setStFhIniDiaSig(Boolean stFhIniDiaSig) {
		this.stFhIniDiaSig = stFhIniDiaSig;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}

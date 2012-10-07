package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP025_DOC_SPE_TAB")
public class DocumentacionSeguimientoProcesoEspecial implements
		IModelo<Integer>, Serializable {

	/**
	 * @author Juan Carlos Ruiz Guadarrama
	 */
	private static final long serialVersionUID = -6074251925518352613L;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@RemoteProperty
	@Id
	@Column(name="CD_DOC_SPE")
	Integer cdDocSpe;
	
	@RemoteProperty
	@Column(name = "CD_EMPRESA")
	String cdEmpresa;
	
	@RemoteProperty
	@Column(name = "NB_GRUPO")
	String nbGrupo;
	
	@RemoteProperty
	@Column(name = "NB_PROCESO")
	String nbProceso;
	
	@RemoteProperty
	@Column(name="CD_ORIGEN")
	Integer cdOrigen;
	
	@RemoteProperty
	@Column(name="CD_BITACORA")
	Integer cdBitacora;
	
	@RemoteProperty
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	Date fhCiclo;
	
	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_INICIO")
	Date tmInicio;
	
	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_DOCUMENTACION")
	Date tmDocumentacion;
	
	@RemoteProperty
	@Column(name = "TX_DOCUMENTACION")
	String txDocumentacion;
	
	@Column(name = "CD_USUARIO")
	private Integer cdUsuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "NB_PROCESO", referencedColumnName = "NB_PROCESO", insertable = false, updatable = false),
			@JoinColumn(name = "NB_GRUPO", referencedColumnName = "NB_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false),
			@JoinColumn(name = "FH_CICLO", referencedColumnName = "FH_CICLO", insertable = false, updatable = false),
			@JoinColumn(name = "TM_INICIO", referencedColumnName = "TM_INICIO", insertable = false, updatable = false)})
	SeguimientoProcesoEspecial seguimientoProcesoEspecial;
	
	@LazyToOne(LazyToOneOption.PROXY)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CD_BITACORA", referencedColumnName = "CD_BITACORA", insertable = false, updatable = false) })
	Bitacora bitacora;
	
	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"seguimientoProcesoEspecial", "bitacora" });
		return reflectionToStringBuilder.toString();
	}
	
	public Integer getCdDocSpe() {
		return cdDocSpe;
	}

	public void setCdDocSpe(Integer cdDocSpe) {
		this.cdDocSpe = cdDocSpe;
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

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public Integer getCdBitacora() {
		return cdBitacora;
	}

	public void setCdBitacora(Integer cdBitacora) {
		this.cdBitacora = cdBitacora;
	}

	public Date getFhCiclo() {
		return fhCiclo;
	}

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
	}

	public Date getTmInicio() {
		return tmInicio;
	}

	public void setTmInicio(Date tmInicio) {
		this.tmInicio = tmInicio;
	}

	public Date getTmDocumentacion() {
		return tmDocumentacion;
	}

	public void setTmDocumentacion(Date tmDocumentacion) {
		this.tmDocumentacion = tmDocumentacion;
	}

	public String getTxDocumentacion() {
		return txDocumentacion;
	}

	public void setTxDocumentacion(String txDocumentacion) {
		this.txDocumentacion = txDocumentacion;
	}

	@Override
	public Integer getId() {
		return cdDocSpe;
	}

	@Override
	public void setId(Integer pId) {
		this.cdDocSpe=pId;
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

}

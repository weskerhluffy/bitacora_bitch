package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.opensymphony.xwork2.conversion.annotations.ConversionType;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@NamedQueries({
		@NamedQuery(name = "holdeadosVigentesBitacora", query = "select distinct h "
				+ "from Holdeado h "
				+ "inner join h.documentacionHoldeados d "
				+ "where d.cdBitacora=:cdBitacora"),
		@NamedQuery(name = "holdeadosVigentesSinDocumentar", query = "select h "
				+ "from Holdeado h "
				+ "where h.documentacionHoldeados.size=0 "
				+ "and h.id.cdEmpresa=:cdEmpresa") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "TBP007_HOLD_TAB")
public class Holdeado implements IModelo<HoldeadoId>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6697566996282218164L;

	@EmbeddedId
	private HoldeadoId id;

	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	private Date fhCiclo;

	@Column(name = "NB_PROCESO")
	private String nbProceso;

	@Column(name = "NB_GRUPO")
	private String nbGrupo;

	@Column(name = "CD_ESTADO")
	private String cdEstado;

	@Column(name = "CD_PROCESO")
	private String cdProceso;

	@Column(name = "CD_USUARIO_DETIENE")
	private String cdUsuarioDetiene;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_FINALIZACION")
	private Date tmFinalizacion;

	@Column(name = "CD_USU_FINALIZA")
	private String cdUsuarioFinaliza;

	@Column(name = "CD_ESTADO_FINAL")
	private String cdEstadoFinal;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false)
	private Origen origen;

	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "CD_USUARIO_DETIENE", referencedColumnName = "CD_LDAP", insertable = false, updatable = false)
	private Usuarioldap usuarioDetiene = null;

	@ManyToOne(fetch=FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "CD_USU_FINALIZA", referencedColumnName = "CD_LDAP", insertable = false, updatable = false)
	private Usuarioldap usuarioFinaliza = null;

	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("tmDocumentacion asc")
	@OneToMany(mappedBy = "holdeado")
	private List<DocumentacionHoldeado> documentacionHoldeados;

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.HoldeadoIdConverter", type = ConversionType.APPLICATION)
	@Override
	public HoldeadoId getId() {
		return id;
	}

	@Override
	public void setId(HoldeadoId pId) {
		id = pId;
	}

	@Transient
	public DocumentacionHoldeado getUltimaDocumentacionHoldeado() {
		return (getDocumentacionHoldeados() != null && getDocumentacionHoldeados()
				.size() > 0) ? getDocumentacionHoldeados().get(
				getDocumentacionHoldeados().size() - 1) : null;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"documentacionHoldeados", "origen", "usuarioDetiene",
				"usuarioFinaliza" });
		return reflectionToStringBuilder.toString();
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

	public String getCdUsuarioDetiene() {
		return cdUsuarioDetiene;
	}

	public void setCdUsuarioDetiene(String cdUsuarioDetiene) {
		this.cdUsuarioDetiene = cdUsuarioDetiene;
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

	public Date getTmFinalizacion() {
		return tmFinalizacion;
	}

	public void setTmFinalizacion(Date tmFinalizacion) {
		this.tmFinalizacion = tmFinalizacion;
	}

	public String getCdUsuarioFinaliza() {
		return cdUsuarioFinaliza;
	}

	public void setCdUsuarioFinaliza(String cdUsuarioFinaliza) {
		this.cdUsuarioFinaliza = cdUsuarioFinaliza;
	}

	public String getCdEstadoFinal() {
		return cdEstadoFinal;
	}

	public void setCdEstadoFinal(String cdEstadoFinal) {
		this.cdEstadoFinal = cdEstadoFinal;
	}

	public List<DocumentacionHoldeado> getDocumentacionHoldeados() {
		return documentacionHoldeados;
	}

	public void setDocumentacionHoldeados(
			List<DocumentacionHoldeado> documentacionHoldeados) {
		this.documentacionHoldeados = documentacionHoldeados;
	}

	public Origen getOrigen() {
		return origen;
	}

	public void setOrigen(Origen origen) {
		this.origen = origen;
	}

	public Usuarioldap getUsuarioDetiene() {
		return usuarioDetiene;
	}

	public void setUsuarioDetiene(Usuarioldap usuarioDetiene) {
		this.usuarioDetiene = usuarioDetiene;
	}

	public Usuarioldap getUsuarioFinaliza() {
		return usuarioFinaliza;
	}

	public void setUsuarioFinaliza(Usuarioldap usuarioFinaliza) {
		this.usuarioFinaliza = usuarioFinaliza;
	}
}

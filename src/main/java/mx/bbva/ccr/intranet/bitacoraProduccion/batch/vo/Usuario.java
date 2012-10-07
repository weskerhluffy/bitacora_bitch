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
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DataTransferObject(javascript = "Usuario")
@Entity
@Table(name = "TBP019_USUARIO_CAT")
public class Usuario implements IModelo<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4104716603416103441L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@RemoteProperty
	@Id
	@Column(name = "CD_USUARIO")
	Integer cdUsuario = null;

	@RemoteProperty
	@Column(name = "CD_LDAP")
	String cdLdap = null;

	@RemoteProperty
	@Column(name = "TX_NOMBRE")
	String txNombre = null;

	@RemoteProperty
	@Column(name = "TX_AP")
	String txAp = null;

	@RemoteProperty
	@Column(name = "TX_AM")
	String txAm = null;

	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_USUARIO_ELIM")
	Date tmUsuarioElim = null;

	@RemoteProperty
	@Column(name = "CD_PERFIL")
	Integer cdPerfil;

	@RemoteProperty
	@Column(name = "CD_AREA")
	Integer cdArea;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_PERFIL", insertable = false, updatable = false)
	Perfil perfil = null;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_AREA", insertable = false, updatable = false)
	Area area = null;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "usuario")
	List<UsuarioPermiso> usuarioPermisos = null;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "usuario")
	private List<EmpresaUsuario> empresaUsuarios = null;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "usuario")
	List<Documentacion> documentacions = null;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	@JoinTable(name = "TBP020_USU_PRM_TAB", joinColumns = { @JoinColumn(name = "CD_USUARIO", updatable = false, insertable = false) }, inverseJoinColumns = { @JoinColumn(name = "CD_PERMISO", updatable = false, insertable = false) })
	List<Permiso> permisos = null;

	@OrderBy(value="nuEmpresa")
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	@JoinTable(name = "TBP018_EMP_USU_TAB", joinColumns = { @JoinColumn(name = "CD_USUARIO", updatable = false, insertable = false) }, inverseJoinColumns = { @JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", updatable = false, insertable = false) })
	private List<Empresa> empresas = null;

	@Transient
	@Override
	public Integer getId() {
		return cdUsuario;
	}

	@Override
	public void setId(Integer pId) {
		cdUsuario = pId;
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
				"permisos", "perfil", "area", "usuarioPermisos",
				"documentacions","empresaUsuarios","empresas" });
		return reflectionToStringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getCdLdap() {
		return cdLdap;
	}

	public void setCdLdap(String cdLdap) {
		this.cdLdap = cdLdap;
	}

	public String getTxNombre() {
		return txNombre;
	}

	public void setTxNombre(String txNombre) {
		this.txNombre = txNombre;
	}

	public String getTxAp() {
		return txAp;
	}

	public void setTxAp(String txAp) {
		this.txAp = txAp;
	}

	public String getTxAm() {
		return txAm;
	}

	public void setTxAm(String txAm) {
		this.txAm = txAm;
	}

	public Date getTmUsuarioElim() {
		return tmUsuarioElim;
	}

	public void setTmUsuarioElim(Date tmUsuarioElim) {
		this.tmUsuarioElim = tmUsuarioElim;
	}

	public Integer getCdPerfil() {
		return cdPerfil;
	}

	public void setCdPerfil(Integer cdPerfil) {
		this.cdPerfil = cdPerfil;
	}

	public Integer getCdArea() {
		return cdArea;
	}

	public void setCdArea(Integer cdArea) {
		this.cdArea = cdArea;
	}

	public List<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<UsuarioPermiso> getUsuarioPermisos() {
		return usuarioPermisos;
	}

	public void setUsuarioPermisos(List<UsuarioPermiso> usuarioPermisos) {
		this.usuarioPermisos = usuarioPermisos;
	}

	public List<Documentacion> getDocumentacions() {
		return documentacions;
	}

	public void setDocumentacions(List<Documentacion> documentacions) {
		this.documentacions = documentacions;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<EmpresaUsuario> getEmpresaUsuarios() {
		return empresaUsuarios;
	}

	public void setEmpresaUsuarios(List<EmpresaUsuario> empresaUsuarios) {
		this.empresaUsuarios = empresaUsuarios;
	}

}

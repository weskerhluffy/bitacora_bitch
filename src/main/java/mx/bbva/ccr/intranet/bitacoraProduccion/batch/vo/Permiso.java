package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@DataTransferObject
@Entity
@Table(name = "TBP021_PERMISO_CAT")
public class Permiso implements IModelo<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7220784746620512147L;
	public static final int DOCUMENTAR_OPERACION = 3;
	public static final int BITACORA_OPERATIVA_CONSULTA = 4;
	public static final int DOCUMENTAR_SOPORTE = 5;
	public static final int PANEL_DE_CONTROL= 6;
	public static final int ADMINISTRACION = 7;
	public static final int MENSAJES= 9;
	@RemoteProperty
	@Id
	@Column(name = "CD_PERMISO")
	Integer cdPermiso;

	@RemoteProperty
	@Column(name = "NB_PERMISO")
	String nbPermiso;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permisos")
	List<Usuario> usuarios = null;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TBP024_PRM_PER_TAB", joinColumns = { @JoinColumn(name = "CD_PERMISO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "CD_PERFIL", nullable = false, updatable = false) })
	List<Perfil> perfils = null;

	public Permiso() {
	}

	@RemoteProperty
	@Transient
	@Override
	public Integer getId() {
		return cdPermiso;
	}

	@Override
	public void setId(Integer pId) {
		cdPermiso = pId;
	}

	public Integer getCdPermiso() {
		return cdPermiso;
	}

	public void setCdPermiso(Integer cdPermiso) {
		this.cdPermiso = cdPermiso;
	}

	public String getNbPermiso() {
		return nbPermiso;
	}

	public void setNbPermiso(String nbPermiso) {
		this.nbPermiso = nbPermiso;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Perfil> getPerfils() {
		return perfils;
	}

	public void setPerfils(List<Perfil> perfils) {
		this.perfils = perfils;
	}

}

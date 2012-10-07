package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP023_PERFIL_CAT")
public class Perfil implements IModelo<Integer>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 950400754227976064L;

	@Id
	@Column(name = "CD_PERFIL")
	Integer cdPerfil;

	@Column(name = "NB_PERFIL")
	String nbPerfil;

	@Temporal(TemporalType.DATE)
	@Column(name = "TM_PERFIL_ELIM")
	Date tmPefilElim;

	@OneToMany(mappedBy = "perfil")
	List<Usuario> usuarios = null;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "perfils")
	List<Permiso> permisos = null;

	@Transient
	@Override
	public Integer getId() {
		return cdPerfil;
	}

	@Override
	public void setId(Integer pId) {
		cdPerfil = pId;
	}

	public Integer getCdPerfil() {
		return cdPerfil;
	}

	public void setCdPerfil(Integer cdPerfil) {
		this.cdPerfil = cdPerfil;
	}

	public String getNbPerfil() {
		return nbPerfil;
	}

	public void setNbPerfil(String nbPerfil) {
		this.nbPerfil = nbPerfil;
	}

	public Date getTmPefilElim() {
		return tmPefilElim;
	}

	public void setTmPefilElim(Date tmPefilElim) {
		this.tmPefilElim = tmPefilElim;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

}

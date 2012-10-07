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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP013_AREA_CAT")
public class Area implements IModelo<Integer>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3253288573861621240L;
	public static final int FRENTE_UNICO = 1;
	public static final int PRODUCCION = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CD_AREA")
	private Integer cdArea;
	@Column(name = "NB_AREA")
	private String nbArea;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_AREA_ELIM")
	private Date tmAreaElim;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("cdProblema")
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "area")
	List<Problema> problemas = null;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "area")
	List<Usuario> usuarios = null;

	@Transient
	@Override
	public Integer getId() {
		return cdArea;
	}

	@Override
	public void setId(Integer pId) {
		cdArea = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public Integer getCdArea() {
		return cdArea;
	}

	public void setCdArea(Integer cdArea) {
		this.cdArea = cdArea;
	}

	public String getNbArea() {
		return nbArea;
	}

	public void setNbArea(String nbArea) {
		this.nbArea = nbArea;
	}

	public List<Problema> getProblemas() {
		return problemas;
	}

	public void setProblemas(List<Problema> problemas) {
		this.problemas = problemas;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Date getTmAreaElim() {
		return tmAreaElim;
	}

	public void setTmAreaElim(Date tmAreaElim) {
		this.tmAreaElim = tmAreaElim;
	}

}

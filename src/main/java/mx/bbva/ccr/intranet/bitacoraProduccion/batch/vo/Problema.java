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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.struts2.json.annotations.JSON;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.SQLDelete;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "cuentaCancelacionesAsociadasProblema", query = "select count(*) from Cancelado c where c.cdProblema=:cdProblema"),
		@NamedQuery(name = "traerProblemasHabilitados", query = "from Problema p where p.tmProblemaElim is null") })
@SQLDelete(sql = "update "
		+ CcrUtil.NOMBRE_SCHEMA
		+ ".TBP010_PROBLEM_CAT set tm_problema_elim=current timestamp WHERE CD_PROBLEMA= ?")
@DataTransferObject(javascript = "Problema")
@Entity
@Table(name = "TBP010_PROBLEM_CAT")
public class Problema implements IModelo<Integer>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1210018579742055929L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@RemoteProperty
	@Id
	@Column(name = "CD_PROBLEMA")
	private Integer cdProblema;

	@RemoteProperty
	@Column(name = "CD_AREA")
	private Integer cdArea;

	@RemoteProperty
	@Column(name = "NB_PROBLEMA")
	private String nbProblema;

	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_PROBLEMA_ELIM")
	private Date tmProblemaElim;

	@RemoteProperty
	@Column(name = "TX_RESPONSABILIDAD")
	private String txResponsabilidad;
	
	
	
	@RemoteProperty
	@Column(name = "TX_CORRIGE")
	private String txCorrige;
	
	



	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_AREA", updatable = false, insertable = false)
	private Area area;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "problema")
	private List<Documentacion> documentacions = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "problema")
	private List<Cancelado> cancelados = null;

	@Override
	public Integer getId() {
		return cdProblema;
	}

	@Override
	public void setId(Integer pId) {
		cdProblema = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public Area getArea() {
		return area;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Documentacion> getDocumentacions() {
		return documentacions;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Cancelado> getCancelados() {
		return cancelados;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"documentacions", "cancelados", "area" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdProblema() {
		return cdProblema;
	}

	public void setCdProblema(Integer cdProblema) {
		this.cdProblema = cdProblema;
	}

	public Integer getCdArea() {
		return cdArea;
	}

	public void setCdArea(Integer cdArea) {
		this.cdArea = cdArea;
	}

	public String getNbProblema() {
		return nbProblema;
	}

	public void setNbProblema(String nbProblema) {
		this.nbProblema = nbProblema;
	}

	public Date getTmProblemaElim() {
		return tmProblemaElim;
	}

	public void setTmProblemaElim(Date tmProblemaElim) {
		this.tmProblemaElim = tmProblemaElim;
	}

	public String getTxResponsabilidad() {
		return txResponsabilidad;
	}

	public void setTxResponsabilidad(String txResponsabilidad) {
		this.txResponsabilidad = txResponsabilidad;
	}

	public String getTxCorrige() {
		return txCorrige;
	}

	public void setTxCorrige(String txCorrige) {
		this.txCorrige = txCorrige;
	}

	public void setDocumentacions(List<Documentacion> documentacions) {
		this.documentacions = documentacions;
	}

	public void setCancelados(List<Cancelado> cancelados) {
		this.cancelados = cancelados;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	

}

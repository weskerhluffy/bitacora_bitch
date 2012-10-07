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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP029_DOC_PR_TAB")
public class DocumentacionProcesoRetrasado implements Serializable,
		IModelo<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8410451104104896057L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "CD_DOC_PR")
	private Integer cdDocPr;

	@Column(name = "CD_BITACORA")
	private Integer cdBitacora;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_DOCUMENTACION")
	private Date tmDocumentacion;

	@Column(name = "TX_DOCUMENTACION")
	private String txDocumentacion;
	
	@Column(name = "CD_USUARIO")
	private Integer cdUsuario;

	@LazyToOne(LazyToOneOption.PROXY)
	@ManyToOne
	@JoinColumn(name = "cd_bitacora", referencedColumnName = "cd_bitacora", insertable = false, updatable = false)
	private Bitacora bitacora;

	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("cdProRet asc")
	@ManyToMany
	@JoinTable(name = "TBP036_RET_DOC_TAB", joinColumns = { @JoinColumn(name = "cd_doc_pr", referencedColumnName = "cd_doc_pr", insertable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "cd_pro_ret", referencedColumnName = "cd_pro_ret", insertable = false, updatable = false) })
	private List<ProcesoRetrasado> procesoRetrasados;

	@Override
	public Integer getId() {
		return cdDocPr;
	}

	@Override
	public void setId(Integer pId) {
		cdDocPr = pId;
	}

	/**
	 * 
	 * @return El primer proceso retrasado asociado a esta documentación, de
	 *         acuerdo a su cdProRet.
	 */
	public ProcesoRetrasado getPrimerProcesoRetrasado() {
		if (getProcesoRetrasados() != null && getProcesoRetrasados().size() > 0) {
			return getProcesoRetrasados().get(0);
		} else {
			return null;
		}
	}

	@JSON(deserialize = false, serialize = false)
	public List<ProcesoRetrasado> getProcesoRetrasados() {
		return procesoRetrasados;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"procesoRetrasados", "bitacora" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdDocPr() {
		return cdDocPr;
	}

	public void setCdDocPr(Integer cdDocPr) {
		this.cdDocPr = cdDocPr;
	}

	public Integer getCdBitacora() {
		return cdBitacora;
	}

	public void setCdBitacora(Integer cdBitacora) {
		this.cdBitacora = cdBitacora;
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

	public Bitacora getBitacora() {
		return bitacora;
	}

	public void setBitacora(Bitacora bitacora) {
		this.bitacora = bitacora;
	}

	public void setProcesoRetrasados(List<ProcesoRetrasado> procesoRetrasados) {
		this.procesoRetrasados = procesoRetrasados;
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

}

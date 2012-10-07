package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @author Ernesto Alvarado Gaspar
 */
@Entity
@Table(name = "TBP032_EVID_DOC_TAB")
public class Evidencia implements Serializable, IModelo<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8320009636758361672L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "CD_EVIDENCIA")
	private Integer cdEvidencia;
	@Column(name = "CD_DOCUMENTACION")
	private Integer cdDocumentacion;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_ENVIO_EVID")
	private Date tmEnvioEvid;
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BL_EVIDENCIA")
	private byte[] blEvidencia;
	@Column(name = "NB_EVIDENCIA")
	private String nbEvidencia;

	@ManyToOne
	@JoinColumn(name = "CD_DOCUMENTACION", referencedColumnName = "CD_DOCUMENTACION", insertable = false, updatable = false)
	private Documentacion documentacion;

	@Transient
	public Integer getId() {
		return cdEvidencia;
	}

	public void setId(Integer pId) {
		cdEvidencia = pId;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"documentacion", "blEvidencia" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdEvidencia() {
		return cdEvidencia;
	}

	public void setCdEvidencia(Integer cdEvidencia) {
		this.cdEvidencia = cdEvidencia;
	}

	public Date getTmEnvioEvid() {
		return tmEnvioEvid;
	}

	public void setTmEnvioEvid(Date tmEnvioEvid) {
		this.tmEnvioEvid = tmEnvioEvid;
	}

	public byte[] getBlEvidencia() {
		return blEvidencia;
	}

	public void setBlEvidencia(byte[] blEvidencia) {
		this.blEvidencia = blEvidencia;
	}

	public Integer getCdDocumentacion() {
		return cdDocumentacion;
	}

	public void setCdDocumentacion(Integer cdDocumentacion) {
		this.cdDocumentacion = cdDocumentacion;
	}

	public Documentacion getDocumentacion() {
		return documentacion;
	}

	public void setDocumentacion(Documentacion documentacion) {
		this.documentacion = documentacion;
	}

	public String getNbEvidencia() {
		return nbEvidencia;
	}

	public void setNbEvidencia(String nbEvidencia) {
		this.nbEvidencia = nbEvidencia;
	}

}

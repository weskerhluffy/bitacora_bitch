package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class RetrasadoDocumentacionId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1960457219575000233L;
	@Column(name = "CD_DOC_PR")
	private Integer cdDocPr;
	@Column(name = "CD_PRO_RET")
	private Integer cdProRet;

	public RetrasadoDocumentacionId() {
	}

	public RetrasadoDocumentacionId(Integer cdDocPr, Integer cdProRet) {
		this.cdDocPr = cdDocPr;
		this.cdProRet = cdProRet;
	}

	public Integer getCdDocPr() {
		return cdDocPr;
	}

	public void setCdDocPr(Integer cdDocPr) {
		this.cdDocPr = cdDocPr;
	}

	public Integer getCdProRet() {
		return cdProRet;
	}

	public void setCdProRet(Integer cdProRet) {
		this.cdProRet = cdProRet;
	}

	@Override
	public boolean equals(Object objeto) {
		return EqualsBuilder.reflectionEquals(this, objeto);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}

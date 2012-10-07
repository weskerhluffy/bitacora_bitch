package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "TBP036_RET_DOC_TAB")
public class RetrasadoDocumentacion implements Serializable,
		IModelo<RetrasadoDocumentacionId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3463793888687478495L;
	@EmbeddedId
	private RetrasadoDocumentacionId id;

	public RetrasadoDocumentacion() {
	}

	public RetrasadoDocumentacion(Integer cdDocPr, Integer cdProRet) {
		this.id = new RetrasadoDocumentacionId();
		this.id.setCdDocPr(cdDocPr);
		this.id.setCdProRet(cdProRet);
	}

	public RetrasadoDocumentacionId getId() {
		return id;
	}

	public void setId(RetrasadoDocumentacionId pId) {
		id = pId;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"documentacion", "cancelado" });
		return reflectionToStringBuilder.toString();
	}

}

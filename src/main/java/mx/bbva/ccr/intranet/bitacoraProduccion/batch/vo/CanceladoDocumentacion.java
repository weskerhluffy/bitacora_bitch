package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Entity
@Table(name = "TBP011_CAN_DOC_TAB")
public class CanceladoDocumentacion implements Serializable,
		IModelo<CanceladoDocumentacionId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2463490433899638922L;

	@EmbeddedId
	private CanceladoDocumentacionId id;

	@Column(name = "CD_EMPRESA", updatable = false, insertable = false)
	private String cdEmpresa;
	@Column(name = "NB_PROCESO", updatable = false, insertable = false)
	private String nbProceso;
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO", updatable = false, insertable = false)
	private Date fhCiclo;
	@Column(name = "CD_PASO", updatable = false, insertable = false)
	private String cdPaso;
	@Column(name = "NB_GRUPO", updatable = false, insertable = false)
	private String nbGrupo;
	@Column(name = "CD_ORIGEN", updatable = false, insertable = false)
	private Integer cdOrigen;
	@Column(name = "CD_DOCUMENTACION", updatable = false, insertable = false)
	private Integer cdDocumentacion;

	public CanceladoDocumentacion() {
	}

	public CanceladoDocumentacion(CanceladoId canceladoId,
			Integer documentacionId) {
		id = new CanceladoDocumentacionId();
		id.setCdEmpresa(canceladoId.getCdEmpresa());
		id.setNbProceso(canceladoId.getNbProceso());
		id.setFhCiclo(canceladoId.getFhCiclo());
		id.setCdPaso(canceladoId.getCdPaso());
		id.setNbGrupo(canceladoId.getNbGrupo());
		id.setCdOrigen(canceladoId.getCdOrigen());
		id.setCdDocumentacion(documentacionId);
	}

	public CanceladoDocumentacionId getId() {
		return id;
	}

	public void setId(CanceladoDocumentacionId pId) {
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

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getNbProceso() {
		return nbProceso;
	}

	public void setNbProceso(String nbProceso) {
		this.nbProceso = nbProceso;
	}

	public Date getFhCiclo() {
		return fhCiclo;
	}

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
	}

	public String getCdPaso() {
		return cdPaso;
	}

	public void setCdPaso(String cdPaso) {
		this.cdPaso = cdPaso;
	}

	public String getNbGrupo() {
		return nbGrupo;
	}

	public void setNbGrupo(String nbGrupo) {
		this.nbGrupo = nbGrupo;
	}

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public Integer getCdDocumentacion() {
		return cdDocumentacion;
	}

	public void setCdDocumentacion(Integer cdDocumentacion) {
		this.cdDocumentacion = cdDocumentacion;
	}

}

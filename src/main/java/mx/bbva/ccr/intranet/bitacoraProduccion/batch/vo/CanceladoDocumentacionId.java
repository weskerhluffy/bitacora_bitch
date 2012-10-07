package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;
import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class CanceladoDocumentacionId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2770153776828239888L;
	@Column(name = "CD_EMPRESA")
	String cdEmpresa;
	@Column(name = "NB_PROCESO")
	String nbProceso;
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	Date fhCiclo;
	@Column(name = "CD_PASO")
	String cdPaso;
	@Column(name = "NB_GRUPO")
	String nbGrupo;
	@Column(name = "CD_ORIGEN")
	Integer cdOrigen;
	@Column(name = "CD_DOCUMENTACION")
	Integer cdDocumentacion;

	@JSON(format = "yyyy-MM-dd")
	public Date getFhCiclo() {
		return fhCiclo;
	}

	public CanceladoDocumentacionId() {
	}

	public CanceladoDocumentacionId(String cdEmpresa, String nbProceso,
			Date fhCiclo, String cdPaso, String nbGrupo, Integer cdOrigen,
			Integer cdDocumentacion) {
		this.cdEmpresa = cdEmpresa;
		this.nbProceso = nbProceso;
		this.fhCiclo = fhCiclo;
		this.cdPaso = cdPaso;
		this.nbGrupo = nbGrupo;
		this.cdOrigen = cdOrigen;
		this.cdDocumentacion = cdDocumentacion;
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

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
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

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.struts2.json.annotations.JSON;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject(javascript = "CanceladoId")
@Embeddable
public
class CanceladoId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5803200800024345655L;
	@RemoteProperty
	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;
	@RemoteProperty
	@Column(name = "NB_GRUPO")
	private String nbGrupo;
	@RemoteProperty
	@Column(name = "NB_PROCESO")
	private String nbProceso;
	@RemoteProperty
	@Column(name = "CD_PASO")
	private String cdPaso;
	@RemoteProperty
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	private Date fhCiclo;
	@RemoteProperty
	@Column(name = "CD_ORIGEN")
	private Integer cdOrigen;

	public CanceladoId() {
	}

	public CanceladoId(String cdEmpresa, String nbGrupo, String nbProceso,
			String cdPaso, Date fhCiclo, Integer cdOrigen) {
		this.cdEmpresa = cdEmpresa;
		this.nbGrupo = nbGrupo;
		this.nbProceso = nbProceso;
		this.cdPaso = cdPaso;
		this.fhCiclo = fhCiclo;
		this.cdOrigen = cdOrigen;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getFhCiclo() {
		return fhCiclo;
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

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getNbGrupo() {
		return nbGrupo;
	}

	public void setNbGrupo(String nbGrupo) {
		this.nbGrupo = nbGrupo;
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

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class HoldeadoId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -568402303927706276L;
	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;
	@Column(name = "CD_ORIGEN")
	private Integer cdOrigen;
	@Column(name = "CD_CICLO")
	private String cdCiclo;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_EVENTO")
	private Date tmEvento;

	public HoldeadoId() {
	}

	public HoldeadoId(String cdEmpresa, Integer cdOrigen, String cdCiclo,
			Date tmEvento) {
		super();
		this.cdEmpresa = cdEmpresa;
		this.cdOrigen = cdOrigen;
		this.cdCiclo = cdCiclo;
		this.tmEvento = tmEvento;
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

	public Date getTmEvento() {
		return tmEvento;
	}

	public void setTmEvento(Date tmEvento) {
		this.tmEvento = tmEvento;
	}

	public String getCdCiclo() {
		return cdCiclo;
	}

	public void setCdCiclo(String cdCiclo) {
		this.cdCiclo = cdCiclo;
	}

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class CargaDatosId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7218922258575233246L;

	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_INSERCION")
	private Date tmInsercion;


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

	public Date getTmInsercion() {
		return tmInsercion;
	}

	public void setTmInsercion(Date tmInsercion) {
		this.tmInsercion = tmInsercion;
	}
}

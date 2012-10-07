package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
class PermisoPerfilId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9182378997131114242L;
	@Column(name = "CD_PERMISO")
	Integer cdPermiso;
	@Column(name = "CD_PERFIL")
	Integer cdPerfil;

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

	public Integer getCdPermiso() {
		return cdPermiso;
	}

	public void setCdPermiso(Integer cdPermiso) {
		this.cdPermiso = cdPermiso;
	}

	public Integer getCdPerfil() {
		return cdPerfil;
	}

	public void setCdPerfil(Integer cdPerfil) {
		this.cdPerfil = cdPerfil;
	}

}

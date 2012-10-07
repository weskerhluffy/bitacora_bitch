package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public
class UsuarioPermisoId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8326683455847434135L;
	@Column(name = "CD_USUARIO")
	Integer cdUsuario;
	@Column(name = "CD_PERMISO")
	Integer cdPermiso;

	public UsuarioPermisoId() {
	}

	public UsuarioPermisoId(Integer cdUsuario, Integer cdPermiso) {
		this.cdUsuario = cdUsuario;
		this.cdPermiso = cdPermiso;
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

	public Integer getCdPermiso() {
		return cdPermiso;
	}

	public void setCdPermiso(Integer cdPermiso) {
		this.cdPermiso = cdPermiso;
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

}

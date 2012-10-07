package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "TBP022_USU_PER_TAB")
class UsuarioPerfil implements IModelo<UsuarioPerfilId>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2731637098772663098L;
	@EmbeddedId
	UsuarioPerfilId id;
	

	@Override
	public UsuarioPerfilId getId() {
		return id;
	}

	@Override
	public void setId(UsuarioPerfilId pId) {
		this.id=pId;
	}
}

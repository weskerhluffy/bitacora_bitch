package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "TBP024_PRM_PER_TAB")
public class PermisoPerfil implements IModelo<PermisoPerfilId>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2274043643058011035L;
	@EmbeddedId
	PermisoPerfilId id;

	@Override
	public PermisoPerfilId getId() {
		return id;
	}

	@Override
	public void setId(PermisoPerfilId pId) {
		this.id=pId;
	}
	
}
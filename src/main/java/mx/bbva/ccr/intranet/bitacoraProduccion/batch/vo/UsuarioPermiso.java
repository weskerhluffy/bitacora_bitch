package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "TBP020_USU_PRM_TAB")
public class UsuarioPermiso implements IModelo<UsuarioPermisoId>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4394093407792688471L;

	@EmbeddedId
	UsuarioPermisoId id;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO", insertable = false, updatable = false)
	Usuario usuario;

	public UsuarioPermiso() {
	}

	public UsuarioPermiso(Integer cdUsuario, Integer cdPermiso) {

		this.id = new UsuarioPermisoId(cdUsuario, cdPermiso);
	}

	@Override
	public UsuarioPermisoId getId() {
		return id;
	}

	@Override
	public void setId(UsuarioPermisoId pId) {
		this.id = pId;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}

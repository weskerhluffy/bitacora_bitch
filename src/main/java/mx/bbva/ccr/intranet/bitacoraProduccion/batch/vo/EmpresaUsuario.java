package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "TBP018_EMP_USU_TAB")
public class EmpresaUsuario implements IModelo<EmpresaUsuarioId>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3918775303977219357L;
	@EmbeddedId
	private EmpresaUsuarioId id;

	@ManyToOne
	@JoinColumn(name = "CD_USUARIO", insertable = false, updatable = false)
	private Usuario usuario;

	public EmpresaUsuario() {
	}

	public EmpresaUsuario(String cdEmpresa, Integer cdUsuario) {
		this.id = new EmpresaUsuarioId();
		this.getId().setCdEmpresa(cdEmpresa);
		this.getId().setCdUsuario(cdUsuario);
	}

	@Override
	public EmpresaUsuarioId getId() {
		return id;
	}

	@Override
	public void setId(EmpresaUsuarioId pId) {
		this.id = pId;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}

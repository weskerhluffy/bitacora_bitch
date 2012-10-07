package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * Las constantes deben coincidir con los registros de la tabla de estatus de
 * cancelado
 * 
 * @author Ernesto Alvarado Gaspar
 */
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "TBP009_ESTADO_CAT")
public
class Estado implements Serializable, IModelo<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1353434162143666164L;
	public static final int REGISTRADO = 1;
	public static final int INSTRUCCIONES_SOLICITADAS = 2;
	public static final int INSTRUCCIONES_ENVIADAS = 3;
	public static final int INSTRUCCIONES_ATENDIDAS = 4;
	public static final int ENVIADO_UNICENTER = 5;

	@Id
	@Column(name = "CD_ESTADO")
	Integer cdEstado;
	@Column(name = "NB_ESTADO")
	String nbEstado;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "estado")
	List<Cancelado> cancelados;

	@Transient
	public Integer getId() {
		return cdEstado;
	}

	public void setId(Integer pId) {
		cdEstado = pId;
	}

	@JSON(serialize = false, deserialize = false)
	public List<Cancelado> getCancelados() {
		return cancelados;
	}

	public Integer getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(Integer cdEstado) {
		this.cdEstado = cdEstado;
	}

	public String getNbEstado() {
		return nbEstado;
	}

	public void setNbEstado(String nbEstado) {
		this.nbEstado = nbEstado;
	}

	public void setCancelados(List<Cancelado> cancelados) {
		this.cancelados = cancelados;
	}

}

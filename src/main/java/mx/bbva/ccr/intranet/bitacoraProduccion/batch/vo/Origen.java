package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * Las constantes deben coincidir con los registros de la tabla de origen de
 * cancelado
 * 
 * @author Ernesto Alvarado Gaspar
 */
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "TBP022_ORIGEN_CAT")
public class Origen implements Serializable, IModelo<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8314094824248498575L;
	public static final int HOST = 1;
	public static final int DISTRIBUIDO = 2;

	@Id
	@Column(name = "CD_ORIGEN")
	private Integer cdOrigen;
	@Column(name = "NB_ORIGEN")
	private String nbOrigen;

	@Transient
	public Integer getId() {
		return cdOrigen;
	}

	public void setId(Integer pId) {
		cdOrigen = pId;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public String getNbOrigen() {
		return nbOrigen;
	}

	public void setNbOrigen(String nbOrigen) {
		this.nbOrigen = nbOrigen;
	}

	public Integer getCdOrigen() {
		return cdOrigen;
	}

}

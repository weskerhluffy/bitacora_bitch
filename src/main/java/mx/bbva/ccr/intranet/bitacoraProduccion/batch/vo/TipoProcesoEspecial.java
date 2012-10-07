package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Las constantes deben coincidir con los registros de la tabla de tipo de
 * proceso especial
 * 
 * @author Ernesto Alvarado Gaspar
 */
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "TBP031_TIP_PRO_ESP_CAT")
public class TipoProcesoEspecial implements Serializable, IModelo<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5661679668164974505L;
	public static final int HITO = 1;
	public static final int CHECKLIST = 2;
	public static final int BR = 3;

	@Id
	@Column(name = "CD_TIP_PRO_ESP")
	Integer cdTipProEsp;
	@Column(name = "NB_TIP_PRO_ESP")
	String nbTipProEsp;
	
	@OneToMany(mappedBy = "tipoProcesoEspecial")
	List<ProcesoEspecial> procesosEspeciales = null;

	@Transient
	public Integer getId() {
		return cdTipProEsp;
	}

	public void setId(Integer pId) {
		cdTipProEsp = pId;
	}

	public Integer getCdTipProEsp() {
		return cdTipProEsp;
	}

	public void setCdTipProEsp(Integer cdTipProEsp) {
		this.cdTipProEsp = cdTipProEsp;
	}

	public String getNbTipProEsp() {
		return nbTipProEsp;
	}

	public void setNbTipProEsp(String nbTipProEsp) {
		this.nbTipProEsp = nbTipProEsp;
	}

}

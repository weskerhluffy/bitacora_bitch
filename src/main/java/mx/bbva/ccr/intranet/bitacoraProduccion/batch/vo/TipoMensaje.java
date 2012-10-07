package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Ernesto Alvarado Gaspar
 */
//FIXME Poner como autogenerada el id
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "TBP028_TIPO_MSG_CAT")
public class TipoMensaje implements Serializable, IModelo<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5803763318367280638L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "CD_TIPO_MSG")
	private Integer cdTipoMsg;
	@Column(name = "NB_TIPO_MSG")
	private String nbTipoMsg;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_TIPO_MSG_ELIM")
	private Date tmTipoMsgElim;

	@Transient
	public Integer getId() {
		return cdTipoMsg;
	}

	public void setId(Integer pId) {
		cdTipoMsg = pId;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {});
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdTipoMsg() {
		return cdTipoMsg;
	}

	public void setCdTipoMsg(Integer cdTipoMsg) {
		this.cdTipoMsg = cdTipoMsg;
	}

	public String getNbTipoMsg() {
		return nbTipoMsg;
	}

	public void setNbTipoMsg(String nbTipoMsg) {
		this.nbTipoMsg = nbTipoMsg;
	}

	public Date getTmTipoMsgElim() {
		return tmTipoMsgElim;
	}

	public void setTmTipoMsgElim(Date tmTipoMsgElim) {
		this.tmTipoMsgElim = tmTipoMsgElim;
	}

}

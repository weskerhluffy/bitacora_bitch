package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;

@NamedQueries({ @NamedQuery(name = "ultimasCargas", query = "from CargaDatos c where c.id.cdEmpresa=:cdEmpresa order by c.id.tmInsercion desc", hints = {}) })
@Entity
@Table(name = "TBP003_CAR_DAT_TAB")
public class CargaDatos implements IModelo<CargaDatosId>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 588113011808084283L;

	@EmbeddedId
	private CargaDatosId id;

	@Column(name = "ST_CARGA")
	private Boolean stCarga;

	@Override
	public CargaDatosId getId() {
		return id;
	}

	@Override
	public void setId(CargaDatosId pId) {
		this.id = pId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Boolean getStCarga() {
		return stCarga;
	}

	public void setStCarga(Boolean stCarga) {
		this.stCarga = stCarga;
	}
}

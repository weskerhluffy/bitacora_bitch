package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TBP004_SEG_PRO_TAB")
class SeguimientoProceso implements IModelo<SeguimientoProcesoId>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8221435544574082925L;

	@EmbeddedId
	SeguimientoProcesoId id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_INSERCION")
	Date tmInsercion;
	
	@Column(name = "CD_RESPONSABLE")
	String cdResponsable;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_EJEC_PROGRAMADO")
	Date fhEjecProgramado;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "HM_EJEC_PROGRAMADO")
	Date hmEjecProgramado;

	@Override
	public SeguimientoProcesoId getId() {
		return id;
	}

	@Override
	public void setId(SeguimientoProcesoId pId) {
		this.id=pId;
	}

	public Date getTmInsercion() {
		return tmInsercion;
	}

	public void setTmInsercion(Date tmInsercion) {
		this.tmInsercion = tmInsercion;
	}

	public String getCdResponsable() {
		return cdResponsable;
	}

	public void setCdResponsable(String cdResponsable) {
		this.cdResponsable = cdResponsable;
	}

	public Date getFhEjecProgramado() {
		return fhEjecProgramado;
	}

	public void setFhEjecProgramado(Date fhEjecProgramado) {
		this.fhEjecProgramado = fhEjecProgramado;
	}

	public Date getHmEjecProgramado() {
		return hmEjecProgramado;
	}

	public void setHmEjecProgramado(Date hmEjecProgramado) {
		this.hmEjecProgramado = hmEjecProgramado;
	}
	
	
}

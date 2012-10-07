package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Embeddable
class SeguimientoProcesoId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3267969086225776404L;

	@Column(name = "CD_EMPRESA")
	String cdEmpresa;

	@Column(name = "CD_ESTADO")
	String cdEstado;

	@Column(name = "NB_PROCESO")
	String nbProceso;

	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	Date fhCiclo;

	@Column(name = "CD_PROCESO")
	String cdProceso;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_EVENTO", nullable = false, length = 26)
	Date tmEvento;

	@Column(name = "CD_PASO")
	String cdPaso;

	@Column(name = "CD_CICLO")
	String cdCiclo;

	@Column(name = "COD_ERROR")
	String cdError;

	@Column(name = "NB_GRUPO")
	String nbGrupo;

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(String cdEstado) {
		this.cdEstado = cdEstado;
	}

	public String getNbProceso() {
		return nbProceso;
	}

	public void setNbProceso(String nbProceso) {
		this.nbProceso = nbProceso;
	}

	public Date getFhCiclo() {
		return fhCiclo;
	}

	public void setFhCiclo(Date fhCiclo) {
		this.fhCiclo = fhCiclo;
	}

	public String getCdProceso() {
		return cdProceso;
	}

	public void setCdProceso(String cdProceso) {
		this.cdProceso = cdProceso;
	}

	public Date getTmEvento() {
		return tmEvento;
	}

	public void setTmEvento(Date tmEvento) {
		this.tmEvento = tmEvento;
	}

	public String getCdPaso() {
		return cdPaso;
	}

	public void setCdPaso(String cdPaso) {
		this.cdPaso = cdPaso;
	}

	public String getCdCiclo() {
		return cdCiclo;
	}

	public void setCdCiclo(String cdCiclo) {
		this.cdCiclo = cdCiclo;
	}

	public String getCdError() {
		return cdError;
	}

	public void setCdError(String cdError) {
		this.cdError = cdError;
	}

	public String getNbGrupo() {
		return nbGrupo;
	}

	public void setNbGrupo(String nbGrupo) {
		this.nbGrupo = nbGrupo;
	}

}

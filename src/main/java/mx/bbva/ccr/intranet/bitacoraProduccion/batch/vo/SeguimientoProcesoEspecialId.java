package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.directwebremoting.annotations.RemoteProperty;

@Embeddable
public class SeguimientoProcesoEspecialId implements Serializable {
	/**
	 * @author Juan Carlos Ruiz Guadarrama
	 */
	private static final long serialVersionUID = -3830651229093482370L;
	
	@RemoteProperty
	@Column(name = "CD_EMPRESA")
	String cdEmpresa;
	
	@RemoteProperty
	@Column(name = "NB_GRUPO")
	String nbGrupo;
	
	@RemoteProperty
	@Column(name = "NB_PROCESO")
	String nbProceso;
	
	@RemoteProperty
	@Column (name="CD_ORIGEN")
	Integer cdOrigen;
	
	@RemoteProperty
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO")
	Date fhCiclo;
	
	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_INICIO")
	Date tmInicio;
	
	public SeguimientoProcesoEspecialId(){
		
	}
	
	public SeguimientoProcesoEspecialId(String cdEmpresa,String nbGrupo,String nbProceso,Integer cdOrigen,Date fhCiclo,Date tmInicio){
		this.cdEmpresa=cdEmpresa;
		this.nbGrupo=nbGrupo;
		this.nbProceso=nbProceso;
		this.cdOrigen=cdOrigen;
		this.fhCiclo=fhCiclo;
		this.tmInicio=tmInicio;
	}
	
	@Override
	public boolean equals(Object objeto) {
		return EqualsBuilder.reflectionEquals(this, objeto);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
		
	public String getCdEmpresa() {
		return cdEmpresa;
	}
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	public String getNbGrupo() {
		return nbGrupo;
	}
	public void setNbGrupo(String nbGrupo) {
		this.nbGrupo = nbGrupo;
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
	public Date getTmInicio() {
		return tmInicio;
	}
	public void setTmInicio(Date tmInicio) {
		this.tmInicio = tmInicio;
	}
	public Integer getCdOrigen() {
		return cdOrigen;
	}
	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

}

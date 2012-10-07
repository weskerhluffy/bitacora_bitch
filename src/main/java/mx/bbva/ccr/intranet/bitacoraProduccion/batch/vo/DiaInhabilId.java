package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.directwebremoting.annotations.RemoteProperty;

@Embeddable
public class DiaInhabilId implements Serializable {
	
	private static final long serialVersionUID = 8863031284232000647L;
	
	@RemoteProperty
	@Column(name = "CD_EMPRESA", nullable = false, length = 20)
	private String cdEmpresa;
	
	@RemoteProperty
	@Column(name = "FH_INHABIL", nullable = false, length = 10)
	private Date fhInhabil;
	
	public DiaInhabilId(){}
	
	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				 });
		return reflectionToStringBuilder.toString();
	}
	
	public DiaInhabilId(String cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	
	public DiaInhabilId(Date fhInhabil){
		this.fhInhabil=fhInhabil;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public Date getFhInhabil() {
		return fhInhabil;
	}

	public void setFhInhabil(Date fhInhabil) {
		this.fhInhabil = fhInhabil;
	}

}

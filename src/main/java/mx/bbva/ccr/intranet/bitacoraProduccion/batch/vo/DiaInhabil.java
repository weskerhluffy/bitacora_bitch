package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP002_DIA_INH_CAT")
public class DiaInhabil implements IModelo<DiaInhabilId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2345088566468551586L;

	@RemoteProperty
	@EmbeddedId
	private DiaInhabilId id;

	@Column(name = "CD_EMPRESA", insertable = false, updatable = false)
	private String cdEmpresa;

	@Column(name = "FH_INHABIL", insertable = false, updatable = false)
	private Date fhInhabil;

	public DiaInhabil(DiaInhabilId diaInhabilId) {

		this.id = diaInhabilId;

	}

	public DiaInhabil() {
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

	@Override
	public DiaInhabilId getId() {
		return this.id;
	}

	@Override
	public void setId(DiaInhabilId pId) {
		id = pId;
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

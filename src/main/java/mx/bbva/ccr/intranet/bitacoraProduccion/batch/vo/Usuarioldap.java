package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP019_USUARIO_CAT")
public class Usuarioldap implements IModelo<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4104716603416103441L;

	@Id
	@Column(name = "CD_LDAP")
	String cdLdap = null;

	@Column(name = "CD_USUARIO")
	Integer cdUsuario = null;

	@Column(name = "TX_NOMBRE")
	String txNombre = null;

	@Column(name = "TX_AP")
	String txAp = null;

	@Column(name = "TX_AM")
	String txAm = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_USUARIO_ELIM")
	Date tmUsuarioElim = null;

	@Column(name = "CD_PERFIL")
	Integer cdPerfil;

	@Column(name = "CD_AREA")
	Integer cdArea;

	@Transient
	@Override
	public String getId() {
		return cdLdap;
	}

	@Override
	public void setId(String pId) {
		cdLdap = pId;
	}

	@Override
	public boolean equals(Object objeto) {
		return EqualsBuilder.reflectionEquals(this, objeto);
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"permisos", "perfil", "area", "usuarioPermisos",
				"documentacions" });
		return reflectionToStringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getCdLdap() {
		return cdLdap;
	}

	public void setCdLdap(String cdLdap) {
		this.cdLdap = cdLdap;
	}

	public String getTxNombre() {
		return txNombre;
	}

	public void setTxNombre(String txNombre) {
		this.txNombre = txNombre;
	}

	public String getTxAp() {
		return txAp;
	}

	public void setTxAp(String txAp) {
		this.txAp = txAp;
	}

	public String getTxAm() {
		return txAm;
	}

	public void setTxAm(String txAm) {
		this.txAm = txAm;
	}

	public Date getTmUsuarioElim() {
		return tmUsuarioElim;
	}

	public void setTmUsuarioElim(Date tmUsuarioElim) {
		this.tmUsuarioElim = tmUsuarioElim;
	}

	public Integer getCdPerfil() {
		return cdPerfil;
	}

	public void setCdPerfil(Integer cdPerfil) {
		this.cdPerfil = cdPerfil;
	}

	public Integer getCdArea() {
		return cdArea;
	}

	public void setCdArea(Integer cdArea) {
		this.cdArea = cdArea;
	}

}

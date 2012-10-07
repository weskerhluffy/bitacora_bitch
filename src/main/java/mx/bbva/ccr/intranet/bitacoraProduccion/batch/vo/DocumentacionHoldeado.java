package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP026_DOC_HOLD_TAB")
public class DocumentacionHoldeado implements Serializable, IModelo<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -187906070403063524L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "CD_DOC_HOLD")
	private Integer cdDocHold;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_EVENTO")
	private Date tmEvento;

	@Column(name = "CD_EMPRESA")
	private String cdEmpresa;

	@Column(name = "CD_USUARIO")
	private Integer cdUsuario;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_DOCUMENTACION")
	private Date tmDocumentacion;

	@Column(name = "CD_ORIGEN")
	private Integer cdOrigen;

	@Column(name = "CD_CICLO")
	private String cdCiclo;

	@Column(name = "TX_DOCUMENTACION")
	private String txDocumentacion;

	@Column(name = "CD_BITACORA")
	private Integer cdBitacora;

	@ManyToOne
	@JoinColumn(name = "cd_usuario", referencedColumnName = "cd_usuario", insertable = false, updatable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false),
			@JoinColumn(name = "CD_CICLO", referencedColumnName = "CD_CICLO", insertable = false, updatable = false),
			@JoinColumn(name = "TM_EVENTO", referencedColumnName = "TM_EVENTO", insertable = false, updatable = false) })
	private Holdeado holdeado;

	@LazyToOne(LazyToOneOption.PROXY)
	@ManyToOne
	@JoinColumn(name = "cd_bitacora", referencedColumnName = "cd_bitacora", insertable = false, updatable = false)
	private Bitacora bitacora;

	@Override
	public Integer getId() {
		return cdDocHold;
	}

	@Override
	public void setId(Integer pId) {
		cdDocHold = pId;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"holdeado", "bitacora" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdDocHold() {
		return cdDocHold;
	}

	public void setCdDocHold(Integer cdDocHold) {
		this.cdDocHold = cdDocHold;
	}

	public Date getTmEvento() {
		return tmEvento;
	}

	public void setTmEvento(Date tmEvento) {
		this.tmEvento = tmEvento;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public Date getTmDocumentacion() {
		return tmDocumentacion;
	}

	public void setTmDocumentacion(Date tmDocumentacion) {
		this.tmDocumentacion = tmDocumentacion;
	}

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
	}

	public String getCdCiclo() {
		return cdCiclo;
	}

	public void setCdCiclo(String cdCiclo) {
		this.cdCiclo = cdCiclo;
	}

	public String getTxDocumentacion() {
		return txDocumentacion;
	}

	public void setTxDocumentacion(String txDocumentacion) {
		this.txDocumentacion = txDocumentacion;
	}

	public Integer getCdBitacora() {
		return cdBitacora;
	}

	public void setCdBitacora(Integer cdBitacora) {
		this.cdBitacora = cdBitacora;
	}

	public Holdeado getHoldeado() {
		return holdeado;
	}

	public void setHoldeado(Holdeado holdeado) {
		this.holdeado = holdeado;
	}

	public Bitacora getBitacora() {
		return bitacora;
	}

	public void setBitacora(Bitacora bitacora) {
		this.bitacora = bitacora;
	}

	public Integer getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(Integer cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}

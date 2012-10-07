package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * 
 * @author Ernesto Alvarado Gaspar
 */
@NamedQueries({ @NamedQuery(name = "mensajesVigentes", query = "select m "
		+ "from Mensaje m " + "inner join fetch m.bitacora as b "
		+ "where b.cdEmpresa=:cdEmpresa "
		+ "and (m.cdBitacora=:cdBitacora or m.tmMensajeCerrado is null) "
		+ "and m.tmMsgBorrado is null and m.cdMensajePri is null", hints = {}) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP015_MENSAJE_TAB")
public class Mensaje implements Serializable, IModelo<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1228092576715906649L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "CD_MENSAJE")
	private Integer cdMensaje;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_MENSAJE")
	private Date tmMensaje;

	@Column(name = "TX_MENSAJE")
	private String txMensaje;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_MENSAJE_CERRADO")
	private Date tmMensajeCerrado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_MSG_BORRADO")
	private Date tmMsgBorrado;

	@Column(name = "CD_BITACORA")
	private Integer cdBitacora;

	@Column(name = "CD_TIPO_MSG")
	private Integer cdTipoMsg;

	@Column(name = "CD_MENSAJE_PRI")
	private Integer cdMensajePri;
	
	@Column(name = "CD_USUARIO")
	private Integer cdUsuario;

	@LazyToOne(LazyToOneOption.FALSE)
	@ManyToOne
	@JoinColumn(name = "cd_tipo_msg", referencedColumnName = "cd_tipo_msg", insertable = false, updatable = false)
	private TipoMensaje tipoMensaje;

	@LazyToOne(LazyToOneOption.FALSE)
	@ManyToOne
	@JoinColumn(name = "cd_mensaje_pri", referencedColumnName = "cd_mensaje", insertable = false, updatable = false)
	private Mensaje mensajePrincipal;

	@LazyToOne(LazyToOneOption.FALSE)
	@ManyToOne
	@JoinColumn(name = "cd_bitacora", referencedColumnName = "cd_bitacora", insertable = false, updatable = false)
	private Bitacora bitacora;

	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("tmMensaje")
	@OneToMany(mappedBy = "mensajePrincipal")
	private List<Mensaje> mensajesSecundarios;

	public Mensaje() {
	}

	@Transient
	public Integer getId() {
		return cdMensaje;
	}

	public void setId(Integer pId) {
		cdMensaje = pId;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"mensajePrincipal", "mensajesSecundarios" });
		return reflectionToStringBuilder.toString();
	}

	@Transient
	public Mensaje getUltimoMensajeSecundario() {
		Mensaje mensaje = null;
		if (getMensajesSecundarios() != null
				&& getMensajesSecundarios().size() > 0) {
			mensaje = getMensajesSecundarios().get(
					getMensajesSecundarios().size() - 1);
		}
		return mensaje;
	}

	@JSON(deserialize = false, serialize = false)
	public List<Mensaje> getMensajesSecundarios() {
		return mensajesSecundarios;
	}

	public Integer getCdTipoMsg() {
		return cdTipoMsg;
	}

	public void setCdTipoMsg(Integer cdTipoMsg) {
		this.cdTipoMsg = cdTipoMsg;
	}

	public Integer getCdMensaje() {
		return cdMensaje;
	}

	public void setCdMensaje(Integer cdMensaje) {
		this.cdMensaje = cdMensaje;
	}

	public Date getTmMensaje() {
		return tmMensaje;
	}

	public void setTmMensaje(Date tmMensaje) {
		this.tmMensaje = tmMensaje;
	}

	public String getTxMensaje() {
		return txMensaje;
	}

	public void setTxMensaje(String txMensaje) {
		this.txMensaje = txMensaje;
	}

	public Date getTmMensajeCerrado() {
		return tmMensajeCerrado;
	}

	public void setTmMensajeCerrado(Date tmMensajeCerrado) {
		this.tmMensajeCerrado = tmMensajeCerrado;
	}

	public Date getTmMsgBorrado() {
		return tmMsgBorrado;
	}

	public void setTmMsgBorrado(Date tmMsgBorrado) {
		this.tmMsgBorrado = tmMsgBorrado;
	}

	public Integer getCdBitacora() {
		return cdBitacora;
	}

	public void setCdBitacora(Integer cdBitacora) {
		this.cdBitacora = cdBitacora;
	}

	public TipoMensaje getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(TipoMensaje tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public Mensaje getMensajePrincipal() {
		return mensajePrincipal;
	}

	public void setMensajePrincipal(Mensaje mensajePrincipal) {
		this.mensajePrincipal = mensajePrincipal;
	}

	public void setMensajesSecundarios(List<Mensaje> mensajesSecundarios) {
		this.mensajesSecundarios = mensajesSecundarios;
	}

	public Integer getCdMensajePri() {
		return cdMensajePri;
	}

	public void setCdMensajePri(Integer cdMensajePri) {
		this.cdMensajePri = cdMensajePri;
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

}

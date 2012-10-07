package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "TBP016_TURNO_CAT")
public class Turno implements Serializable, IModelo<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7687177104673333198L;

	public static final int MATUTINO = 1;
	public static final int VESPERTINO = 2;
	public static final int NOCTURNO = 3;

	@Id
	@Column(name = "CD_TURNO")
	private Integer cdTurno;
	@Column(name = "NB_TURNO")
	private String nbTurno;
	@Column(name = "CD_TURNO_ANTERIOR")
	private Integer cdTurnoAnterior;

	@ManyToOne
	@JoinColumn(name = "CD_TURNO", referencedColumnName = "CD_TURNO_ANTERIOR", updatable = false, insertable = false)
	private Turno turnoSiguiente;
	@ManyToOne
	@JoinColumn(name = "CD_TURNO_ANTERIOR", referencedColumnName = "CD_TURNO", updatable = false, insertable = false)
	private Turno turnoAnterior;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "turno")
	private List<Bitacora> bitacoras = null;

	@Override
	public Integer getId() {
		return cdTurno;
	}

	@Override
	public void setId(Integer pId) {
		cdTurno = pId;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder
				.setExcludeFieldNames(new String[] { "bitacoras" });
		return reflectionToStringBuilder.toString();
	}

	public Integer getCdTurno() {
		return cdTurno;
	}

	public void setCdTurno(Integer cdTurno) {
		this.cdTurno = cdTurno;
	}

	public String getNbTurno() {
		return nbTurno;
	}

	public void setNbTurno(String nbTurno) {
		this.nbTurno = nbTurno;
	}

	public Integer getCdTurnoAnterior() {
		return cdTurnoAnterior;
	}

	public void setCdTurnoAnterior(Integer cdTurnoAnterior) {
		this.cdTurnoAnterior = cdTurnoAnterior;
	}

	public List<Bitacora> getBitacoras() {
		return bitacoras;
	}

	public void setBitacoras(List<Bitacora> bitacoras) {
		this.bitacoras = bitacoras;
	}

	public Turno getTurnoSiguiente() {
		return turnoSiguiente;
	}

	public void setTurnoSiguiente(Turno turnoSiguiente) {
		this.turnoSiguiente = turnoSiguiente;
	}

	public Turno getTurnoAnterior() {
		return turnoAnterior;
	}

	public void setTurnoAnterior(Turno turnoAnterior) {
		this.turnoAnterior = turnoAnterior;
	}

}

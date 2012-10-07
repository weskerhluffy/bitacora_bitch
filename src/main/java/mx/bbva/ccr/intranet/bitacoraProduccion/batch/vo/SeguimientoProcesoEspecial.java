package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.opensymphony.xwork2.conversion.annotations.ConversionType;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@NamedQueries({
		@NamedQuery(name = "procesosEspecialesPorODate", query = "select spe "
				+ "from SeguimientoProcesoEspecial spe "
				+ "inner join fetch spe.procesoEspecial pe "
				+ "where spe.fhCiclo=:fecha " + "and spe.cdEmpresa=:cdEmpresa "
				+ "and pe.cdTipProEsp=:tipoProcesoEspecial"),
		@NamedQuery(name = "procesosEspecialesPorTiempoEvento", query = "select spe "
				+ "from SeguimientoProcesoEspecial spe "
				+ "inner join fetch spe.procesoEspecial pe "
				+ "where spe.tmInicio between :fecha and :fechaManana "
				+ "and spe.cdEmpresa=:cdEmpresa "
				+ "and pe.cdTipProEsp=:tipoProcesoEspecial") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "TBP005_SPE_TAB")
public class SeguimientoProcesoEspecial implements
IModelo<SeguimientoProcesoEspecialId>, Serializable {

	/**
	 * @author Juan Carlos Ruiz Guadarrama
	 */
	private static final long serialVersionUID = 5062215447445878217L;

	public static final Integer VERDE=0;
	public static final Integer AMARILLO=1;
	public static final Integer ROJO=2;

	@RemoteProperty
	@EmbeddedId
	private SeguimientoProcesoEspecialId id;

	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_FIN")
	private Date tmFin;

	@RemoteProperty
	@Column(name = "CD_ESTADO_FIN")
	private String cdEstadoFin;

	@RemoteProperty
	@Column(name = "CD_USUARIO_FIN")
	private String cdUsuarioFin;

	/* Propiedades de la llave primaria */
	@RemoteProperty
	@Column(name = "CD_EMPRESA",insertable=false,updatable=false)
	private String cdEmpresa;

	@RemoteProperty
	@Column(name = "NB_GRUPO", insertable = false, updatable = false)
	private String nbGrupo;

	@RemoteProperty
	@Column(name = "NB_PROCESO", insertable = false, updatable = false)
	private String nbProceso;

	@RemoteProperty
	@Column(name = "CD_ORIGEN", insertable = false, updatable = false)
	private Integer cdOrigen;

	@RemoteProperty
	@Temporal(TemporalType.DATE)
	@Column(name = "FH_CICLO", insertable = false, updatable = false)
	private Date fhCiclo;

	@RemoteProperty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TM_INICIO", insertable = false, updatable = false)
	private Date tmInicio;
	/* Propiedades de la llave foranea */

	@LazyToOne(LazyToOneOption.FALSE)
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "CD_EMPRESA", referencedColumnName = "CD_EMPRESA", insertable = false, updatable = false),
			@JoinColumn(name = "NB_PROCESO", referencedColumnName = "NB_PROCESO", insertable = false, updatable = false),
			@JoinColumn(name = "NB_GRUPO", referencedColumnName = "NB_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "CD_ORIGEN", referencedColumnName = "CD_ORIGEN", insertable = false, updatable = false) })
	private ProcesoEspecial procesoEspecial;

	@Fetch(FetchMode.SUBSELECT)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("tmDocumentacion asc")
	@OneToMany(mappedBy = "seguimientoProcesoEspecial")
	private List<DocumentacionSeguimientoProcesoEspecial> documentacionSeguimientoProcesoEspecials;

	public SeguimientoProcesoEspecial(){

	}

	public SeguimientoProcesoEspecial(SeguimientoProcesoEspecialId seguimientoProcesoEspecialId){
		this.id=seguimientoProcesoEspecialId;
	}


	/**
	 * Regresa el estado de SeguimientoProcesoEspecial de acuerdo a
	 * <code>hmTecnica</code> y <code>hmServicio</code> de <code>procesoEspecial</code>,
	 * calcula el ultimo dia hábil de cada mes y utilizar <code>hmTecnicaFinDeMes</code>,<code>hmServicioFinDeMes</code>
	 * y <code>diasInhabiles</code> de Empresa para establecer el estado 
	 * 
	 * @return estado de seguimiento de proceso especial
	 */
	public Integer getEstado(){
		Integer estado=VERDE;

		
		SimpleDateFormat formatoFecha=new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar=GregorianCalendar.getInstance();

		/*Constantes de GregorianCalendar*/
		final Integer DIA=GregorianCalendar.DATE;
		final Integer MES=GregorianCalendar.MONTH;
		final Integer ANIO=GregorianCalendar.YEAR;
		final Integer DIA_SEMANA=GregorianCalendar.DAY_OF_WEEK;
		final Integer SABADO=GregorianCalendar.SATURDAY;
		final Integer DOMINGO=GregorianCalendar.SUNDAY;
		final Integer LUNES=GregorianCalendar.MONDAY;

		Date tmIn;
		Date horaTn;
		Date horaSe;
		Date horaTnFm;
		Date horaSeFm;
		Date fecTiempoActual;
		Date fecUltimoDiaHabilMes;

		String ultimoDiaHabilMes;
		String fecTiempoInicio;
		String diaInhabil;

		Integer anio;
		Integer mes;
		Integer dia;
		Integer ultimoDiaMes;
		Integer diaSemana;

		/*Establecer diasInhabiles por Empresa*/
		Empresa empresaCache=new Empresa();
		
		List<DiaInhabil> diasInhabiles;

		empresaCache.setCdEmpresa(id.getCdEmpresa());
		diasInhabiles=empresaCache.getDiasInhabiles();

		fecTiempoActual=new Date();
		
		tmIn=id.getTmInicio();
		
		calendar.setTime(tmIn);
		
		if(procesoEspecial.getStFhIniDiaSig()!=null&&procesoEspecial.getStFhIniDiaSig()==true){
			dia=calendar.get(DIA);
			calendar.set(DIA, (dia+1));
			tmIn=calendar.getTime();
		}
		
		fecTiempoInicio=formatoFecha.format(tmIn);

		/*Ultimo día del mes*/
		ultimoDiaMes=calendar.getActualMaximum(DIA);

		calendar.set(DIA, ultimoDiaMes);
		dia=calendar.get(DIA);
		diaSemana=calendar.get(DIA_SEMANA);

		/*Si es sabado o domingo se recorre a viernes*/
		if(diaSemana==SABADO){
			calendar.set(DIA, (dia-1));
			dia=calendar.get(DIA);
			diaSemana=calendar.get(DIA_SEMANA);
		}else if(diaSemana==DOMINGO){
			calendar.set(DIA, (dia-2));
			dia=calendar.get(DIA);
			diaSemana=calendar.get(DIA_SEMANA);
		}

		/*Se asigna el ultimo dia habil del mes*/
		fecUltimoDiaHabilMes=calendar.getTime();
		ultimoDiaHabilMes=formatoFecha.format(fecUltimoDiaHabilMes);

		/*Se compara con los dias inhabiles de la Empresa y se recorre un dia antes*/
		if(diasInhabiles!=null&&!diasInhabiles.isEmpty()){
			for(DiaInhabil dInhabil:diasInhabiles){
				diaInhabil=formatoFecha.format(dInhabil.getId().getFhInhabil());
				if(ultimoDiaHabilMes.equals(diaInhabil)){
					if(diaSemana==LUNES){
						calendar.set(DIA, (dia-3));
						dia=calendar.get(DIA);
						diaSemana=calendar.get(DIA_SEMANA);

						fecUltimoDiaHabilMes=calendar.getTime();
						ultimoDiaHabilMes=formatoFecha.format(fecUltimoDiaHabilMes);
					}else{
						calendar.set(DIA, (dia-1));
						dia=calendar.get(DIA);
						diaSemana=calendar.get(DIA_SEMANA);

						fecUltimoDiaHabilMes=calendar.getTime();
						ultimoDiaHabilMes=formatoFecha.format(fecUltimoDiaHabilMes);
					}
				}
			}
		}

		/*Se asgina el dia de tmInicio a la Hora Tecnica y Hora de Servicio para poder establecer el estado */
		calendar.setTime(tmIn);

		anio=calendar.get(ANIO);
		mes=calendar.get(MES);
		dia=calendar.get(DIA);

		horaTn=procesoEspecial.getHmTecnica();
		calendar.setTime(horaTn);
		calendar.set(anio, mes, dia);		
		horaTn=calendar.getTime();

		/*Se verifica si la Hora de Servicio es del mismo día*/
		horaSe=procesoEspecial.getHmServicio();
		if(horaSe.getTime()<horaTn.getTime()){
			calendar.setTime(horaSe);
			calendar.set(anio, mes, (dia+1));		
			horaSe=calendar.getTime();
		}else{
			calendar.setTime(horaSe);
			calendar.set(anio, mes, dia);		
			horaSe=calendar.getTime();
		}
		calendar.setTime(horaSe);
		calendar.set(anio, mes, dia);		
		horaSe=calendar.getTime();

		/*Se establece el estado si tiene tmFin o es nulo*/
		if(tmFin!=null){
			/*Se verifica que sea ultimo dia de mes*/
			if(ultimoDiaHabilMes.equals(fecTiempoInicio)&&procesoEspecial.getHmServicioFinDeMes()!=null&&procesoEspecial.getHmTecnicaFinDeMes()!=null){

				horaTnFm=procesoEspecial.getHmTecnicaFinDeMes();
				calendar.setTime(horaTnFm);
				calendar.set(anio, mes, dia);		
				horaTnFm=calendar.getTime();

				horaSeFm=procesoEspecial.getHmServicioFinDeMes();
				if(horaSeFm.getTime()<horaTnFm.getTime()){
					calendar.setTime(horaSeFm);
					calendar.set(anio, mes, (dia+1));		
					horaSeFm=calendar.getTime();
				}else{
					calendar.setTime(horaSeFm);
					calendar.set(anio, mes, dia);		
					horaSeFm=calendar.getTime();
				}

				if(horaTnFm.getTime()<tmFin.getTime()){
					estado =AMARILLO;
				}
				if(horaSeFm.getTime()<tmFin.getTime()){
					estado= ROJO;
				}
			}else{
				if(horaTn.getTime()<tmFin.getTime()){
					estado =AMARILLO;
				}
				if(horaSe.getTime()<tmFin.getTime()){
					estado= ROJO;
				}
			}
		}
		else{
			if(ultimoDiaHabilMes.equals(fecTiempoInicio)&&procesoEspecial.getHmServicioFinDeMes()!=null&&procesoEspecial.getHmTecnicaFinDeMes()!=null){

				horaTnFm=procesoEspecial.getHmTecnicaFinDeMes();
				calendar.setTime(horaTnFm);
				calendar.set(anio, mes, dia);		
				horaTnFm=calendar.getTime();

				horaSeFm=procesoEspecial.getHmServicioFinDeMes();
				if(horaSeFm.getTime()<horaTnFm.getTime()){
					calendar.setTime(horaSeFm);
					calendar.set(anio, mes, (dia+1));		
					horaSeFm=calendar.getTime();
				}else{
					calendar.setTime(horaSeFm);
					calendar.set(anio, mes, dia);		
					horaSeFm=calendar.getTime();
				}

				if(horaTnFm.getTime()<fecTiempoActual.getTime()){
					estado= AMARILLO;
				}
				if(horaSeFm.getTime()<fecTiempoActual.getTime()){
					estado= ROJO;
				}
			}else{
				if(horaTn.getTime()<fecTiempoActual.getTime()){
					estado= AMARILLO;
				}
				if(horaSe.getTime()<fecTiempoActual.getTime()){
					estado= ROJO;
				}
			}
		}


		return estado;
	}


	public SeguimientoProcesoEspecialId getId() {
		return id;
	}

	@TypeConversion(converter = "mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter.SeguimientoProcesoEspecialIdConverter", type = ConversionType.APPLICATION)
	public void setId(SeguimientoProcesoEspecialId pId) {
		id = pId;
	}

	@Transient
	public DocumentacionSeguimientoProcesoEspecial getUltimaDocumentacionSeguimientoProcesoEspecial() {
		return (getDocumentacionSeguimientoProcesoEspecials() != null && getDocumentacionSeguimientoProcesoEspecials()
				.size() > 0) ? getDocumentacionSeguimientoProcesoEspecials().get(
						getDocumentacionSeguimientoProcesoEspecials().size() - 1) : null;
	}

	@Override
	public String toString() {
		final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(
				this);
		reflectionToStringBuilder.setAppendStatics(true);
		reflectionToStringBuilder.setAppendTransients(true);
		reflectionToStringBuilder.setExcludeFieldNames(new String[] {
				"documentacionSeguimientoProcesoEspecials", "procesoEspecial" });
		return reflectionToStringBuilder.toString();
	}

	public ProcesoEspecial getProcesoEspecial() {
		return procesoEspecial;
	}

	public void setProcesoEspecial(ProcesoEspecial procesoEspecial) {
		this.procesoEspecial = procesoEspecial;
	}

	public Date getTmFin() {
		return tmFin;
	}

	public void setTmFin(Date tmFin) {
		this.tmFin = tmFin;
	}

	public List<DocumentacionSeguimientoProcesoEspecial> getDocumentacionSeguimientoProcesoEspecials() {
		return documentacionSeguimientoProcesoEspecials;
	}

	public void setDocumentacionSeguimientoProcesoEspecials(
			List<DocumentacionSeguimientoProcesoEspecial> documentacionSeguimientoProcesoEspecials) {
		this.documentacionSeguimientoProcesoEspecials = documentacionSeguimientoProcesoEspecials;
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getCdEstadoFin() {
		return cdEstadoFin;
	}

	public void setCdEstadoFin(String cdEstadoFin) {
		this.cdEstadoFin = cdEstadoFin;
	}

	public String getCdUsuarioFin() {
		return cdUsuarioFin;
	}

	public void setCdUsuarioFin(String cdUsuarioFin) {
		this.cdUsuarioFin = cdUsuarioFin;
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

	public Integer getCdOrigen() {
		return cdOrigen;
	}

	public void setCdOrigen(Integer cdOrigen) {
		this.cdOrigen = cdOrigen;
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

}

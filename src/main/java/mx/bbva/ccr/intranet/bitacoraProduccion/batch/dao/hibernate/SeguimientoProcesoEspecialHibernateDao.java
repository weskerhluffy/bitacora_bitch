package mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.SeguimientoProcesoEspecialDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Bitacora;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecial;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecialId;

import org.apache.log4j.Logger;

@Singleton
@Named("seguimientoProcesoEspecialDao")
public class SeguimientoProcesoEspecialHibernateDao
		extends
		GenericHibernateDAO<SeguimientoProcesoEspecial, SeguimientoProcesoEspecialId>
		implements SeguimientoProcesoEspecialDao {
	private static final Logger LOGGER = Logger
			.getLogger(SeguimientoProcesoEspecialDao.class);
	private static final Calendar CALENDARIO = GregorianCalendar.getInstance();

	@SuppressWarnings("unchecked")
	// Por el cast de lista sin tipo a lista de seguimientos
	@Override
	public List<SeguimientoProcesoEspecial> getSeguimientosProcesoEspecial(
			Bitacora bitacora, Date fecha, Boolean usarODate,
			Integer tipoProcesoEspecial) {
		List<SeguimientoProcesoEspecial> seguimientoProcesoEspecials = null;
		Date fechaManana;
		LOGGER.trace("Buscando seguimientos con usarODate " + usarODate);
		if (usarODate) {
			seguimientoProcesoEspecials = getHibernateTemplate()
					.findByNamedQueryAndNamedParam(
							"procesosEspecialesPorODate",
							new String[] { "fecha", "cdEmpresa",
									"tipoProcesoEspecial" },
							new Object[] { fecha, bitacora.getCdEmpresa(),
									tipoProcesoEspecial });
		} else {
			CALENDARIO.setTime(fecha);
			CALENDARIO.set(GregorianCalendar.HOUR_OF_DAY, 0);
			CALENDARIO.set(GregorianCalendar.MINUTE, 0);
			CALENDARIO.set(GregorianCalendar.SECOND, 0);
			fecha = CALENDARIO.getTime();
			CALENDARIO.roll(GregorianCalendar.DATE, 1);
			fechaManana = CALENDARIO.getTime();
			seguimientoProcesoEspecials = getHibernateTemplate()
					.findByNamedQueryAndNamedParam(
							"procesosEspecialesPorTiempoEvento",
							new String[] { "fecha", "fechaManana", "cdEmpresa",
									"tipoProcesoEspecial" },
							new Object[] { fecha, fechaManana,
									bitacora.getCdEmpresa(),
									tipoProcesoEspecial });
		}
		return seguimientoProcesoEspecials;
	}

}

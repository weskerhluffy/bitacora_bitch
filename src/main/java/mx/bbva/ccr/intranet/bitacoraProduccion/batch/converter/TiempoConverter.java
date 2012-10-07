package mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * @author Ernesto Alvarado Gaspar
 * 
 */
public class TiempoConverter extends StrutsTypeConverter {

	private Logger logger = Logger.getLogger(TiempoConverter.class);

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		logger.trace("Se convertira la cadena " + values[0]);
		Date tiempo = null;
		try {
			tiempo = CcrUtil.getHoraDeString(values[0]);
		} catch (ParseException pe) {
			logger.warn("Error al intentar parsear una hora " + values[0]);
			logger.warn(ExceptionUtils.getStackTrace(pe));
		}
		if (tiempo == null) {
			try {
				tiempo = CcrUtil.getFechaDeString(values[0]);
			} catch (ParseException pe) {
				logger.error("Error al parsear fecha u hora " + values[0]);
				logger.error(ExceptionUtils.getStackTrace(pe));
				throw new TypeConversionException(
						"No se pudo convertir la cadena "
								+ values[0]
								+ " a un objeto date para representar una fecha u hora");
			}
		}
		return tiempo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		String cadena = "";
		if (o != null) {
			if (CcrUtil.getFechaFormateadaCorta((Date) o).matches("1970-01-01")) {
				cadena = CcrUtil.getHoraFormateada((Date) o);
			} else {
				cadena = CcrUtil.getFechaFormateadaCorta((Date) o);
			}
		}
		return cadena;
	}

}

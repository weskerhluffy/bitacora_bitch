package mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter;

import java.util.Date;

import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.CanceladoId;

import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * Asegurar que el formato de conversión sea el mismo que en el archivo
 * bitacora.js
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 */
public class CanceladoIdConverter extends StrutsTypeConverter {

	private Logger logger = Logger.getLogger(CanceladoIdConverter.class);
	public static final String SEPARADOR=":::";

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		logger.trace("Se convertira la cadena " + values[0]);
		CanceladoId canceladoId = null;
		Date fecCiclo = null;
		Long fecCicloTime = null;
		Integer cdOrigen = null;
		String[] componentes = null;
		if (values == null || values.length < 1 || values[0] == null
				|| values[0].matches("")) {
			logger.error("La cadena a convertir en canceladoId es nula o vacia");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId a traves de los valores pasados, estos eran nulos o vacios");
		}
		// logger.trace("La cadena original es " + values[0]);
		componentes = values[0].split(SEPARADOR);
		if (componentes.length != 6) {
			logger.error("Al separar la cadena para obtener un canceladoId hubo un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId a traves de los valores pasados, no esta en el formato esperado");
		}
		if (componentes[0].matches("")) {
			logger.error("El paso es una cadena vacia, es un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que la empresa aparecio como cadena vacia");
		}
		if (componentes[1].matches("")) {
			logger.error("El idEmpresa es una cadena vacia, es un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que el grupo aparecio como cadena vacia");
		}
		if (componentes[2].matches("")) {
			logger.error("El nombre del grupo es una cadena vacia, es un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que el proceso aparecio como cadena vacia");
		}
		if (componentes[3].matches("")) {
			logger.error("El nombre del grupo es una cadena vacia, es un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que el paso aparecio como cadena vacia");
		}
		if (componentes[5].matches("")) {
			logger.error("El codigo de origen esta vacio, es un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que el código de origen aparecio como cadena vacia");
		}
		try {
			fecCicloTime = Long.parseLong(componentes[4]);
			fecCiclo = new Date(fecCicloTime);
		} catch (NumberFormatException nfe) {
			logger.error("La fecha de canceladoId no fue proporcionada");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que la fecha no fue proporcionada en la cadena");
		}
		try {
			cdOrigen = Integer.parseInt(componentes[5]);
		} catch (NumberFormatException nfe) {
			logger.error("No se pudo convertir el codigo de origen");
			throw new TypeConversionException(
					"No se pudo construir el objeto canceladoId debido a que el código de origen no fue proporcionada en la cadena");
		}
		canceladoId = new CanceladoId(componentes[0], componentes[1],
				componentes[2], componentes[3], fecCiclo, cdOrigen);
		return canceladoId;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		String cadena = "";
		CanceladoId canceladoId = (CanceladoId) o;
		if (canceladoId != null) {
			cadena = canceladoId.getCdEmpresa() + SEPARADOR
					+ canceladoId.getNbGrupo() + SEPARADOR
					+ canceladoId.getNbProceso() + SEPARADOR
					+ canceladoId.getCdPaso() + SEPARADOR
					+ canceladoId.getFhCiclo().getTime() + SEPARADOR
					+ canceladoId.getCdOrigen();
		}
		return cadena;
	}

}

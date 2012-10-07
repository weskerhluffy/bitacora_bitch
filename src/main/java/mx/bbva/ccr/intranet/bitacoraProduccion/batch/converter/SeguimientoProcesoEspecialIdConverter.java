package mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter;

import java.util.Date;
import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.SeguimientoProcesoEspecialId;

import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class SeguimientoProcesoEspecialIdConverter extends StrutsTypeConverter {

	private Logger logger = Logger
			.getLogger(SeguimientoProcesoEspecialIdConverter.class);

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		SeguimientoProcesoEspecialId seguimientoProcesoEspecialId = null;
		Date fecCiclo = null;
		Date tiempoInicio = null;
		Long fecCicloTime = null;
		Long tiempoInicioTime = null;
		Integer cdOrigen = null;
		String[] componentes = null;

		logger.trace("Conviritiendo cadena " + values[0]);

		if (values == null || values.length < 1 || values[0] == null
				|| values[0].matches("")) {
			logger.error("La cadena a convertir en seguimientoProcesoEspecialId es nula o vacia");
			throw new TypeConversionException(
					"No se pudo construir el objeto seguimientoProcesoEspecial a traves de los valores pasados, estos eran nulos o vacios");
		}

		componentes = values[0].split(CanceladoIdConverter.SEPARADOR);
		if (componentes.length != 6) {
			logger.error("Al separar la cadena para obtener un seguimientoProcesoEspecialId hubo un error");
			throw new TypeConversionException(
					"No se pudo construir el objeto seguimientoProcesoEspecialId a traves de los valores pasados, no esta en el formato esperado");
		} else {
			if (componentes[2].matches("")) {
				logger.error("El nombre del proceso es una cadena vacia, es un error");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que la empresa aparecio como cadena vacia");
			}
			if (componentes[0].matches("")) {
				logger.error("El idEmpresa es una cadena vacia, es un error");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que el grupo aparecio como cadena vacia");
			}
			if (componentes[1].matches("")) {
				logger.error("El nombre del grupo es una cadena vacia, es un error");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que el proceso aparecio como cadena vacia");
			}

			if (componentes[5].matches("")) {
				logger.error("El codigo de origen esta vacio, es un error");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que el código de origen aparecio como cadena vacia");
			}
			try {
				fecCicloTime = Long.parseLong(componentes[4]);
				fecCiclo = new Date(fecCicloTime);
			} catch (NumberFormatException nfe) {
				logger.error("La fecha de seguimientoProcesoEspecialId no fue proporcionada");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que la fecha no fue proporcionada en la cadena");
			}

			try {
				tiempoInicioTime = Long.parseLong(componentes[3]);
				tiempoInicio = new Date(tiempoInicioTime);
			} catch (NumberFormatException nfe) {
				logger.error("El tiempo de inicio de seguimientoProcesoEspecialId no fue proporcionada");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que el tiempo de inicio no fue proporcionado en la cadena");
			}

			try {
				cdOrigen = Integer.parseInt(componentes[5]);
			} catch (NumberFormatException nfe) {
				logger.error("No se pudo convertir el codigo de origen");
				throw new TypeConversionException(
						"No se pudo construir el objeto seguimientoProcesoEspecialId debido a que el código de origen no fue proporcionada en la cadena");
			}
			seguimientoProcesoEspecialId = new SeguimientoProcesoEspecialId(
					componentes[0], componentes[1], componentes[2], cdOrigen,
					fecCiclo, tiempoInicio);
			logger.trace("El id es : " + componentes[0] + " " + componentes[1]
					+ " " + componentes[2] + " " + cdOrigen + " " + fecCiclo
					+ " " + tiempoInicio);
		}
		return seguimientoProcesoEspecialId;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		String cadena = null;
		SeguimientoProcesoEspecialId seguimientoProcesoEspecialId = (SeguimientoProcesoEspecialId) o;
		if (seguimientoProcesoEspecialId != null) {
			cadena = seguimientoProcesoEspecialId.getCdEmpresa()
					+ CanceladoIdConverter.SEPARADOR
					+ seguimientoProcesoEspecialId.getNbGrupo()
					+ CanceladoIdConverter.SEPARADOR
					+ seguimientoProcesoEspecialId.getNbProceso()
					+ CanceladoIdConverter.SEPARADOR
					+ seguimientoProcesoEspecialId.getTmInicio().getTime()
					+ CanceladoIdConverter.SEPARADOR
					+ seguimientoProcesoEspecialId.getFhCiclo().getTime()
					+ CanceladoIdConverter.SEPARADOR
					+ seguimientoProcesoEspecialId.getCdOrigen();
		}
		logger.trace("Se ha convertido el id " + cadena);
		return cadena;
	}

}

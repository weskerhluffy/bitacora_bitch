package mx.bbva.ccr.intranet.bitacoraProduccion.batch.converter;

import java.util.Date;
import java.util.Map;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.HoldeadoId;

import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class HoldeadoIdConverter extends StrutsTypeConverter {

	private Logger logger = Logger.getLogger(HoldeadoIdConverter.class);

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		HoldeadoId holdeadoId = null;
		String[] componentesHoldeadoId;
		Date tmEvento;
		logger.trace("Conviritiendo cadena " + values[0]);
		componentesHoldeadoId = values[0].split(CanceladoIdConverter.SEPARADOR);
		if (componentesHoldeadoId.length < 4) {
			logger.error("Al separar la cadena de holdeadoId no se encontraron el numero de componentes necesarios");
			throw new TypeConversionException(
					"No se pudo construir el objeto holdeadoId debido a que no esta en el formato correcto");
		} else {
			try {
				tmEvento = new Date(Long.parseLong(componentesHoldeadoId[3]));
				holdeadoId = new HoldeadoId(componentesHoldeadoId[0],
						Integer.parseInt(componentesHoldeadoId[1]),
						componentesHoldeadoId[2], tmEvento);
			} catch (NumberFormatException nfe) {
				logger.error("El formato de epoca " + componentesHoldeadoId[3]
						+ " o de cdOrigen " + componentesHoldeadoId[1]
						+ " de holdeadoId no es numérico");
				throw new TypeConversionException(
						"No se pudo construir el objeto holdeadoId debido a que la epoca de tmEvento o el id del origen no son numéricos o son nulos");
			}
		}
		return holdeadoId;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		String holdeadoIdString = null;
		HoldeadoId holdeadoId;
		holdeadoId = (HoldeadoId) o;
		holdeadoIdString = holdeadoId.getCdEmpresa()
				+ CanceladoIdConverter.SEPARADOR + holdeadoId.getCdOrigen()
				+ CanceladoIdConverter.SEPARADOR + holdeadoId.getCdCiclo()
				+ CanceladoIdConverter.SEPARADOR
				+ holdeadoId.getTmEvento().getTime();
		return holdeadoIdString;
	}

}

package mx.bbva.ccr.intranet.bitacoraProduccion.batch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MÃ©todos Ãºtiles de cadenas y fechas.
 * 
 * @author Ernesto Alvarado Gaspar
 * 
 */
public class CcrUtil {
	private static SimpleDateFormat formateador = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final String NOMBRE_SCHEMA = "gbp001";

	/**
	 * 
	 * @param fecha
	 * @param formato
	 * @return La fecha en el formato indicado
	 */
	public static String getFechaFormateada(Date fecha, String formato) {
		String fechaFormateada = "";
		formateador.applyPattern(formato);
		fechaFormateada = formateador.format(fecha);
		return fechaFormateada;
	}

	/**
	 * 
	 * @param fecha
	 * @return La fecha en formato yyyy-MMM-dd
	 */
	public static String getFechaFormateadaBonita(Date fecha) {
		return getFechaFormateada(fecha, "dd 'de' MMMMM 'de' yyyy");
	}

	/**
	 * 
	 * @param fecha
	 * @return La fecha en formato yyyy-MM-dd
	 */
	public static String getFechaFormateadaCorta(Date fecha) {
		return getFechaFormateada(fecha, "yyyy-MM-dd");
	}

	/**
	 * 
	 * @param hora
	 * @return La hora en formato HH:mm
	 */
	public static String getHoraFormateada(Date hora) {
		return getFechaFormateada(hora, "HH:mm");
	}

	/**
	 * Convierte la cadena <code>tiempo</code> a un objeto Date usando el
	 * formato <code>formato</code>
	 * 
	 * @param tiempo
	 *            La cadena que define el punto en el tiempo
	 * @param formato
	 *            El formato en el que esta dada la cadena <code>tiempo</code>
	 * @return
	 * @throws ParseException
	 */
	public static Date getTiempoDeString(String tiempo, String formato)
			throws ParseException {
		Date tiempoTmp = null;
		formateador.applyPattern(formato);
		tiempoTmp = formateador.parse(tiempo);
		return tiempoTmp;
	}

	/**
	 * 
	 * @param hora
	 * @return Un objeto Date, siempre que <code>hora</code> sea una cadena
	 *         representando tiempo con el formato "HH:mm"
	 * @throws ParseException
	 */
	public static Date getHoraDeString(String hora) throws ParseException {
		return getTiempoDeString(hora, "HH:mm");
	}

	/**
	 * 
	 * @param tiempo
	 * @return La estampa de tiempo que representa el objeto <code>tiempo</code>
	 *         en el formato "yyyy-MM-dd_hh:mm:ss"
	 */
	public static String getTimeStampFormateado(Date tiempo) {
		String tiempoFormateado = "";
		formateador.applyPattern("yyyy-MM-dd_hh:mm:ss");
		tiempoFormateado = formateador.format(tiempo);
		return tiempoFormateado;
	}

	public static String escaparHTML(String cadena) {
		// System.out.println("Escapando cadena "+cadena);
		String cadenaTmp = cadena;
		cadenaTmp = cadenaTmp.replace("\\", "\\\\");
		cadenaTmp = cadenaTmp.replace("\"", "\\\"");
		cadenaTmp = cadenaTmp.replace("<", "\\<");
		cadenaTmp = cadenaTmp.replace(">", "\\>");
		StringBuffer sb = new StringBuffer();
		int n = cadenaTmp.length();
		for (int i = 0; i < n; i++) {
			char c = cadenaTmp.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case ' ':
				sb.append("&nbsp;");
				break;

			default:
				sb.append(c);
				break;
			}
		}
		// System.out.println("La cadena transformada es "+sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * @param fechaCadena
	 * @return Objeto <code>Date</code> que representa la fecha indicada en
	 *         <code>fecha</code> que debe estar en el formato "yyyy-MM-dd"
	 * @throws ParseException
	 */
	public static Date getFechaDeString(String fechaCadena)
			throws ParseException {
		Date fecha = null;
		fecha = getTiempoDeString(fechaCadena, "yyyy-MM-dd");
		return fecha;
	}

	/**
	 * 
	 * @param tiempo
	 * @return La estampa de tiempo que representa el objeto <code>tiempo</code>
	 *         en el formato "yyyy-MM-dd'T'hh:mm:ss" el mismo que usa struts2
	 */
	public static String getTimeStampStruts(Date tiempo) {
		String tiempoFormateado = "";
		formateador.applyPattern("yyyy-MM-dd'T'hh:mm:ss");
		tiempoFormateado = formateador.format(tiempo);
		return tiempoFormateado;
	}

	/**
	 * 
	 * @param tiempo
	 * @return La estampa de tiempo que representa el objeto <code>tiempo</code>
	 *         en el formato "yyyy-MM-dd hh:mm:ss"
	 */
	public static String getTimeStampFormateadoCorto(Date tiempo) {
		String tiempoFormateado = "";
		formateador.applyPattern("yyyy-MM-dd hh:mm:ss");
		tiempoFormateado = formateador.format(tiempo);
		return tiempoFormateado;
	}

	/**
	 * Itera sobre cada uno de los elementos de <code>elements</code>, aplicando
	 * {@link Object#toString()} a cada elemento (si no es {@link String} claro)
	 * y concatena dichas cadenas en una sola, separando los componentes con
	 * <code>separator</code>.
	 * 
	 * @param elements
	 * @param separator
	 * @return
	 */
	public static final <T> String implode(Iterable<T> elements,
			Object separator) {
		String sepStr = separator.toString();
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (Object s : elements) {
			if (s == null)
				continue;

			if (first)
				first = false;
			else
				out.append(sepStr);
			out.append(s);
		}
		return out.toString();
	}
}

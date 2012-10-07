/**
 * 
 */
package mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo;

import java.io.Serializable;

/**
 * @author Ernesto Alvarado Gaspar
 * 
 */
public interface IModelo<ID> extends Serializable {
	public abstract ID getId();

	public abstract void setId(final ID pId);
}

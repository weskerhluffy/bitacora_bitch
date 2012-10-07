package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.DiaInhabilDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IDiaInhabilService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.util.CcrUtil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabil;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.DiaInhabilId;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.transaction.annotation.Transactional;

@Singleton
@Named("diaInhabilService")
public class DiaInhabilService extends
		GenericoService<DiaInhabil, DiaInhabilId> implements IDiaInhabilService {

	public void setDiaInhabilDao(DiaInhabilDao diaInhabilDao) {
		setEntidadDao(diaInhabilDao);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void agregarFechaInhabil(String empSel, List<String> fechasOriginal,
			List<String> fechasSel) {
		DiaInhabil diaInhabil;
		DiaInhabilId diaInhabilId = new DiaInhabilId();
		List<Date> fechasO = new ArrayList<Date>();
		List<Date> fechasS = new ArrayList<Date>();
		Empresa emp;
		Date temp = null;

		emp = traerPorIdGenerico(Empresa.class, empSel);

		for (String fechaCadS : fechasSel) {
			try {
				temp = CcrUtil.getFechaDeString(fechaCadS);
			} catch (ParseException e) {
				logger.error("No se pudo parsear la fecha " + fechaCadS
						+ " en agregarFechaInhabil");
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			logger.trace("Fecha:" + temp);
			fechasS.add(temp);
		}

		if (fechasOriginal != null) {
			for (String fechaCadO : fechasOriginal) {
				try {
					temp = CcrUtil.getFechaDeString(fechaCadO);
				} catch (ParseException e) {
					logger.error("No se pudo parsear la fecha " + fechaCadO
							+ " en agregarFechaInhabil");
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				logger.trace("Fechaklk:" + temp);
				fechasO.add(temp);
			}

			for (Date fechaSel : fechasS) {
				if (fechasOriginal.contains(CcrUtil
						.getFechaFormateadaCorta(fechaSel))) {

				} else {
					diaInhabilId = new DiaInhabilId();
					diaInhabilId.setFhInhabil(fechaSel);
					diaInhabilId.setCdEmpresa(emp.getCdEmpresa());
					diaInhabil = new DiaInhabil(diaInhabilId);
					logger.trace("la mierda que se va a guardar: "
							+ diaInhabilId);
					guardar(diaInhabil);
				}
			}

			for (Date fechaOrg : fechasO) {
				if (fechasSel.contains(CcrUtil
						.getFechaFormateadaCorta(fechaOrg))) {

				} else {
					diaInhabilId = new DiaInhabilId();
					diaInhabilId.setFhInhabil(fechaOrg);
					diaInhabilId.setCdEmpresa(emp.getCdEmpresa());
					diaInhabil = traerPorId(diaInhabilId);
					logger.trace("la mierda que se va a eliminar: "
							+ diaInhabilId);
					eliminar(diaInhabil);
				}
			}
		} else {
			for (Date fechaSel : fechasS) {
				diaInhabilId = new DiaInhabilId();
				diaInhabilId.setFhInhabil(fechaSel);
				diaInhabilId.setCdEmpresa(emp.getCdEmpresa());
				diaInhabil = new DiaInhabil(diaInhabilId);
				logger.trace("La puta fecha: " + diaInhabil);
				guardar(diaInhabil);

			}
		}

	}

}

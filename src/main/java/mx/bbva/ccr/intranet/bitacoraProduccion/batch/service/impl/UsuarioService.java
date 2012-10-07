package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.dao.UsuarioDao;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.service.IUsuarioService;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Empresa;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.EmpresaUsuario;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Permiso;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;
import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.UsuarioPermiso;

import org.springframework.transaction.annotation.Transactional;

@Singleton
@Named("usuarioService")
public class UsuarioService extends GenericoService<Usuario, Integer> implements
		IUsuarioService {
	public void setUsuarioDao(UsuarioDao usuarioDao) {
		setEntidadDao(usuarioDao);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void agregarUsuario(Usuario usuario, List<Integer> permisos,
			List<String> empresas) {
		UsuarioPermiso usuarioPermiso = null;
		EmpresaUsuario empresaUsuario = null;

		Usuario usuarioGuardado;

		usuarioGuardado = guardar(usuario);
		for (Integer permiso : permisos) {
			usuarioPermiso = new UsuarioPermiso(usuarioGuardado.getId(),
					permiso);
			guardarGenerico(usuarioPermiso);
		}

		for (String cdEmpresa : empresas) {
			empresaUsuario = new EmpresaUsuario(cdEmpresa,
					usuarioGuardado.getId());
			guardarGenerico(empresaUsuario);
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void actualizarUsuario(Usuario usuario, List<Integer> permisos,
			List<String> empresas) {
		logger.trace("En actualizarUsuario");
		Boolean permisoEncontrado = false;
		Boolean empresaEncontrada = false;
		List<Permiso> permisosActuales = new ArrayList<Permiso>();
		List<Empresa> empresasActuales = new ArrayList<Empresa>();
		for (Permiso permiso : usuario.getPermisos()) {
			permisosActuales.add(permiso);
		}
		logger.trace("el usuario tiene " + permisosActuales.size()
				+ " permisos");
		logger.trace("Se le asignaran " + permisos.size() + " nuevos permisos");
		for (UsuarioPermiso usuarioPermisoEliminar : usuario
				.getUsuarioPermisos()) {
			if (!permisos.contains(usuarioPermisoEliminar.getId()
					.getCdPermiso())) {
				logger.trace("Se elimina el permiso "
						+ usuarioPermisoEliminar.getId().getCdPermiso());
				eliminarGenerico(usuarioPermisoEliminar);
			}
		}
		for (Integer cdPermiso : permisos) {
			for (Permiso permiso : permisosActuales) {
				if (cdPermiso.equals(permiso.getId())) {
					permisoEncontrado = true;
					break;
				}
			}
			if (!permisoEncontrado) {
				logger.trace("Se añade el permiso " + cdPermiso);
				guardarGenerico(new UsuarioPermiso(usuario.getId(), cdPermiso));
			}
			permisoEncontrado = false;
		}

		for (Empresa empresa : usuario.getEmpresas()) {
			empresasActuales.add(empresa);
		}
		for (EmpresaUsuario empresaUsuarioEliminar : usuario
				.getEmpresaUsuarios()) {
			if (!empresas.contains(empresaUsuarioEliminar.getId()
					.getCdEmpresa())) {
				eliminarGenerico(empresaUsuarioEliminar);
			}
		}
		for (String cdEmpresa : empresas) {
			for (Empresa empresa : empresasActuales) {
				if (cdEmpresa.equals(empresa.getCdEmpresa())) {
					empresaEncontrada = true;
					break;
				}
			}
			if (!empresaEncontrada) {
				guardarGenerico(new EmpresaUsuario(cdEmpresa, usuario.getId()));
			}
			empresaEncontrada = false;
		}
		actualizar(usuario);
	}

	@Transactional
	@Override
	public Boolean validarLdapUnico(Usuario usuario) {
		Boolean yaRegistrado = false;
		Usuario usuarioEjemplo = null;
		List<Usuario> usuariosEncontrados;
		usuarioEjemplo = new Usuario();
		usuarioEjemplo.setCdLdap(usuario.getCdLdap());
		usuariosEncontrados = encontrarPorInstanciaEjemplo(usuarioEjemplo);
		logger.trace("Al buscar el cdldap " + usuario.getCdLdap()
				+ " se encontraron " + usuariosEncontrados);

		for (Usuario usuario2 : usuariosEncontrados) {
			if (!usuario2.getCdUsuario().equals(usuario.getCdUsuario())) {
				yaRegistrado = true;
				break;
			}
		}

		return yaRegistrado;
	}
}

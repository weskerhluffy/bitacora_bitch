package mx.bbva.ccr.intranet.bitacoraProduccion.batch.service;

import java.util.List;

import mx.bbva.ccr.intranet.bitacoraProduccion.batch.vo.Usuario;

public interface IUsuarioService extends IGenericoService<Usuario, Integer> {

	/**
	 * Se crea el usuario en el sistema con los permisos especificados por los
	 * ids en la lista <code>permisos</code> y con accceso a las empresas con
	 * identificador contenido en <code>empresas</code>
	 * 
	 * @param usuario
	 * @param permisos
	 * @param empresas
	 */
	public void agregarUsuario(Usuario usuario, List<Integer> permisos,
			List<String> empresas);

	/**
	 * Se actualiza el usuario con los permisos especificados por los ids en la
	 * lista <code>permisos</code> y con acceso a las empresas identificadas en
	 * la lista <code>empresas</code>
	 * 
	 * @param usuario
	 * @param permisos
	 * @param empresas
	 */
	public void actualizarUsuario(Usuario usuario, List<Integer> permisos,
			List<String> empresas);

	/**
	 * 
	 * @param usuario
	 * @return Si el ldap que se pretende dar al usuario ya esta registrado
	 */
	public Boolean validarLdapUnico(Usuario usuario);
}

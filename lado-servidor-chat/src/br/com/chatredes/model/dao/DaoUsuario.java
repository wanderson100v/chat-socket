/**
 * 
 */
package br.com.chatredes.model.dao;

import br.com.chatredes.model.pojo.Usuario;

/**
 * @author wanderson
 *
 */
public class DaoUsuario extends Dao<Usuario> implements IDaoUsuario{

	public DaoUsuario() {
		super(Usuario.class);
	}
	
}

/**
 * 
 */
package br.com.chatredes.model.dao;

import java.util.List;

import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Usuario;
import br.com.chatredes.model.viewbanco.UsuarioPublico;

/**
 * @author wanderson
 *
 */
public interface IDaoUsuario extends IDao<Usuario> {

	public Usuario login(String login, String senha) throws DaoException;
	
	public List<UsuarioPublico> buscarTodos() throws DaoException;
	
}

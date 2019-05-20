/**
 * 
 */
package br.com.chatredes.model.dao;

import javax.persistence.TypedQuery;

import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Usuario;

/**
 * @author wanderson
 *
 */
public class DaoUsuario extends Dao<Usuario> implements IDaoUsuario{

	public DaoUsuario() {
		super(Usuario.class);
	}

	@Override
	public Usuario login(String login, String senha) throws DaoException {
		try {

			TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.login = :login AND u.senha = :senha", tipoDaClasse);
			query.setParameter("login", login);
			query.setParameter("senha", senha);

			return query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO REALIZAR LOGIN "+tipoDaClasse.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}

	}
	
}

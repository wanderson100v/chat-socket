/**
 * 
 */
package br.com.chatredes.model.dao;

import java.util.List;

import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Mensagem;
import br.com.chatredes.model.viewbanco.MensagemGlobal;

/**
 * @author wanderson
 *
 */
public interface IDaoMensagem extends IDao<Mensagem> {

	public List<MensagemGlobal> buscarMensagensGlobais(String loginDestinatario) throws DaoException;
	
	public List<MensagemGlobal> buscarMensagensPrivadas(String loginRemetente, String loginDestinatario) throws DaoException;
	
}

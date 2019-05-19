/**
 * 
 */
package br.com.chatredes.model.dao;

import br.com.chatredes.model.pojo.Mensagem;

/**
 * @author wanderson
 *
 */
public class DaoMensagem extends Dao<Mensagem> implements IDaoMensagem{

	public DaoMensagem() {
		super(Mensagem.class);
	}

}

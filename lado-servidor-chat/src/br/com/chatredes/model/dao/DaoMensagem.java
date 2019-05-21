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
public class DaoMensagem extends Dao<Mensagem> implements IDaoMensagem{

	public DaoMensagem() {
		super(Mensagem.class);
	}
	
	public List<MensagemGlobal> buscarMensagensGlobais(String loginDestinatario) throws DaoException{
		try {
			return em.createQuery("select msgg from MensagemGlobal msgg where msgg.loginDestinatario = :login", MensagemGlobal.class)
					.setParameter("login", loginDestinatario)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR TODAS AS MENSAGES GLOBAIS "+MensagemGlobal.class.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}
	}
	
	

}

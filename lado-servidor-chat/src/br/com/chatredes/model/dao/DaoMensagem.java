/**
 * 
 */
package br.com.chatredes.model.dao;

import java.util.List;

import javax.persistence.TypedQuery;

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
//			return em.createQuery(
//					"select msgg from MensagemGlobal msgg "
//					+ "where msgg.loginDestinatario = :loginDestinatario "
//					+ "and msgg.tipo = 0", MensagemGlobal.class)
//					.setParameter("loginDestinatario",loginDestinatario)
//					.getResultList();
			return em.createQuery(
					"select msgg from MensagemGlobal msgg "
					+ "where msgg.tipo = 0", MensagemGlobal.class)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR TODAS AS MENSAGES GLOBAIS "+MensagemGlobal.class.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}
	}

	@Override
	public List<MensagemGlobal> buscarMensagensPrivadas(String loginRemetente, String loginDestinatario)
			throws DaoException {
		
		try {
			
			TypedQuery<MensagemGlobal> query = em.createQuery("select ms from MensagemGlobal ms where (ms.loginDestinatario = :destinatario"
					+ " and ms.loginRemetente = :remetente) or (ms.loginDestinatario = :destinatario1 and ms.loginRemetente = :remetente1) and ms.tipo = 1", MensagemGlobal.class); 
			query.setParameter("destinatario", loginDestinatario);
			query.setParameter("remetente", loginRemetente);
			
			query.setParameter("destinatario1", loginRemetente);
			query.setParameter("remetente1", loginDestinatario);
			
			return query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR TODAS AS MENSAGES PRIVADAS "+MensagemGlobal.class.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}
	}
	
	

}

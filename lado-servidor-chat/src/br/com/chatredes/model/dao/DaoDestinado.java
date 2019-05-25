package br.com.chatredes.model.dao;

import java.util.List;

import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Destinado;
import br.com.chatredes.model.pojo.Mensagem;
import br.com.chatredes.model.pojo.Usuario;
import br.com.chatredes.model.viewbanco.MensagemGlobal;
import br.com.chatredes.model.viewbanco.VisualizadoDetalhes;

/**
 * @author wanderson
 *
 */
public class DaoDestinado  extends Dao<Destinado> implements IDaoDestinado {

	public DaoDestinado() {
		super(Destinado.class);
	}
	
	/**
	 * Busca o registro de mensagem destinada a determinado usuário.
	 * @param destinatario, usuário que recebeu a mensagem.
	 * @param mensagem, mensagem enviada.
	 * @return registro que há correspondência.
	 */
	public Destinado buscarPorDesginadoMsg(Usuario destinatario, Mensagem mensagem)throws DaoException {
		try {
			return em.createQuery(
					"SELECT d FROM Destinado d "
					+ "WHERE d.destinatario = :dest "
					+ "AND d.mensagem = :msg", Destinado.class)
					.setParameter("dest", destinatario)
					.setParameter("msg", mensagem)
					.getSingleResult();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR MENSAGEM DESTINADA A USUÁRIO "
					+MensagemGlobal.class.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}
	}
	
	public List<VisualizadoDetalhes> detelhesMensagem(long mensagemId) throws DaoException{
		try {
			return em.createQuery(
					"SELECT vd FROM VisualizadoDetalhes vd "
					+ "WHERE vd.mensagemId = :mensagemId "
					+ "AND vd.horarioVisualizado != null", VisualizadoDetalhes.class)
					.setParameter("mensagemId", mensagemId)
					.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR DETELHES DE VISUALIZAÇÃO DE MENSAGEM "
					+MensagemGlobal.class.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}
	}

}

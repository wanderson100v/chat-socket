/**
 * 
 */
package br.com.chatredes.model.dao;

import java.util.List;

import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Destinado;
import br.com.chatredes.model.pojo.Mensagem;
import br.com.chatredes.model.pojo.Usuario;
import br.com.chatredes.model.viewbanco.VisualizadoDetalhes;

/**
 * @author wanderson
 *
 */
public interface IDaoDestinado extends IDao<Destinado>{
	
	public Destinado buscarPorDesginadoMsg(Usuario destinatario, Mensagem mensagem)throws DaoException;
	
	public List<VisualizadoDetalhes> detelhesMensagem(long mensagemId) throws DaoException;
	
}

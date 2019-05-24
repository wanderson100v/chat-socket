/**
 * 
 */
package br.com.chatredes.controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.com.chatredes.model.viewbanco.MensagemGlobal;
import br.com.chatredes.model.viewbanco.UsuarioPublico;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * @author Mael Santos
 *
 */
public class ControleServidor extends Controle implements Observer{

	@FXML
	private ListView<MensagemGlobal> msgList;

	@FXML
	private ListView<UsuarioPublico> userList;

	private Servidor servidor;

	@Override
	public void init() {
		Servidor.iniciarServidor();
		servidor = Servidor.getInstance();
		servidor.addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if (arg1 instanceof MensagemGlobal)
		{
			MensagemGlobal mensagem = (MensagemGlobal) arg1;
			msgList.getItems().add(mensagem);
		}
		else
		{
			@SuppressWarnings("unchecked")
			List<UsuarioPublico> list = (List<UsuarioPublico>) arg1;
			userList.getItems().setAll(list);
		}
	}

}

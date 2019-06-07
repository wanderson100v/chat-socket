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
import javafx.scene.input.ScrollEvent;

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

	private boolean scrollUser = true, scrollMsg = true;
	
	@Override
	public void init() {
		msgList.addEventFilter(ScrollEvent.ANY, (e) ->{ //get every scroll event
	      if(e.getTextDeltaY() > 0){ //set Scroll event to false when the user scrolls up
  	        scrollMsg = false;
  	      }
  	    });
  	
  	userList.addEventFilter(ScrollEvent.ANY, (e) ->{ //get every scroll event
  	      if(e.getTextDeltaY() > 0){ //set Scroll event to false when the user scrolls up
  	        scrollUser = false;
  	      }
  	    });
		
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
			
			if(scrollMsg)
		        msgList.scrollTo(msgList.getItems().size() -1 );
		}
		else
		{
			@SuppressWarnings("unchecked")
			List<UsuarioPublico> list = (List<UsuarioPublico>) arg1;
			userList.getItems().setAll(list);			if(scrollUser)
				userList.scrollTo(userList.getItems().size() -1);
		}
	}

}

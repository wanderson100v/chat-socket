/**
 * 
 */
package br.com.chatredes.controle;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.chatredes.view.Notificacao;
import javafx.fxml.Initializable;

/**
 * @author mael santos
 *
 */
public abstract class Controle implements Initializable {

	protected Notificacao notificacao;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
		notificacao = Notificacao.getInstance();
	}

	/**
	 * <p>Método responsavel por inicializar os componetes da tela</p>
	 */
	public abstract void init();
	
}

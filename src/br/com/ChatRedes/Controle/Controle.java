/**
 * 
 */
package br.com.ChatRedes.Controle;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/**
 * @author mael santos
 *
 */
public abstract class Controle implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	/**
	 * <p>Método responsavel por inicializar os componetes da tela</p>
	 */
	public abstract void init();
	
}

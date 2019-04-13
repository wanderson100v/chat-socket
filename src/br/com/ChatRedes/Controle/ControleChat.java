/**
 * 
 */
package br.com.ChatRedes.Controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * @author mael santos
 *
 */
public class ControleChat extends Controle {

	@FXML
    private Label lblEstado;

    @FXML
    private TextArea txa;

    @FXML
    private TextField tfdMensagem;

    @FXML
    private Button btnEnviar;
	
	@Override
	public void init() {
		

	}

    /**
     * @param event
     * <p>Método responsável por tratar a ação do clique do Button</p>
     */    		
	@FXML
    void clickAction(ActionEvent event) {

		System.out.println("Click");
		
    }

    /**
     * @param event
     * <p>Método responsável por tratar a ação de entrada do TextField</p>
     */
	@FXML
	void inputAction(KeyEvent event) {
		
		System.out.println("Digitando...");

	}
}

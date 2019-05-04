package br.com.ChatRedes.Controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ControleCliente extends Controle{

    @FXML
    private Label lblEnviadas;

    @FXML
    private Label lblRecebidas;

    @FXML
    private Label lblStatus;

    @FXML
    private TextArea txaMensagens;

    @FXML
    private TextField tfdMensagem;

    @FXML
    private Button btnEnviar;

    @Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
    
    @FXML
    void clickAction(ActionEvent event) {
    	System.out.println("Click");
    }

    @FXML
    void inputAction(KeyEvent event) {
    	System.out.println("Digitando...");
    }

}

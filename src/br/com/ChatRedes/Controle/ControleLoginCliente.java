package br.com.ChatRedes.Controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ControleLoginCliente extends Controle{

    @FXML
    private TextField tfdPorta;

    @FXML
    private TextField tfdHost;

    @FXML
    private TextField tfdNome;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
    @FXML
    void clickAction(ActionEvent event) {
    	System.out.println("Click");
    }

}

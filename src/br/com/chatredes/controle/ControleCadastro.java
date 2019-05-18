package br.com.chatredes.controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControleCadastro extends Controle{

    @FXML
    private TextField tfdLogin;

    @FXML
    private TextField tfdNome;

    @FXML
    private PasswordField tfdSenha;

    @FXML
    private PasswordField tfdConfirmarSenha;

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

    }

}

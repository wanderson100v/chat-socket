package br.com.chatredes.controller;

import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControleLogin {

    @FXML
    private URL location;

    @FXML
    private TextField tfdLogin;

    @FXML
    private PasswordField tfdSenha;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;

    @FXML
    void clickAction(ActionEvent event) {

    	Cliente.getInstance().protocoloLOGIN(tfdLogin.getText(),tfdSenha.getText());
    	
    }

    @FXML
    void initialize() {
        

    }
}

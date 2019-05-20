package br.com.chatredes.controller;

import java.io.IOException;

import br.com.chatredes.app.AppCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * @author Mael Santos
 *
 */
public class ControleCliente extends Controle{

	@FXML
    private Button btnSair;

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
    
    private Pane loginCliente;
    
    @Override
	public void init() {
		
    	
	}
    
    @FXML
    void clickAction(ActionEvent event) {
    	
    	Object obj = event.getSource();//componente que disparou a ação

		if(obj == btnEnviar)
		{
			enviarMensagem();
		}
		else if(obj == btnSair)
		{
			try {
				if(loginCliente == null)
					loginCliente = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/chatredes/view/LoginCliente.fxml"));
				AppCliente.changeStage(loginCliente);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    }

    @FXML
    void inputAction(KeyEvent event) {
    	
    	if (event.getCode() == KeyCode.ENTER) {
    		if(!tfdMensagem.getText().trim().equals(""))
    			enviarMensagem();
    	}
    	
    }

    private void enviarMensagem()
    {
    	
    }
    

}

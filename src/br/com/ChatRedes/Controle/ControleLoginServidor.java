package br.com.ChatRedes.Controle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ControleLoginServidor extends Controle{

    @FXML
    private TextField tfdPorta;

    @FXML
    private TextField tfdNome;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;

    @Override
	public void init() {
		
	}
    
    @FXML
    void clickAction(ActionEvent event) {
    	
    	Object obj = event.getSource();
    	
    	if(obj == btnEntrar)
    	{
    		notificacao.mensagemAguarde();
    		notificacao.mensagemErro();
    	}
    	else if(obj == btnCancelar)
    	{
    		System.exit(0);
    	}
    	
    }

}

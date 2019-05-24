package br.com.chatredes.controller;

import java.io.IOException;

import br.com.chatredes.app.AppServidor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ControleLoginServidor extends Controle{

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;
    
    private Pane paneServidor;
    
    @Override
	public void init() {
	}
    
    @FXML
    void clickAction(ActionEvent event) {
    	
    	Object obj = event.getSource();
    	
    	if(obj == btnEntrar)
    	{
    		try {
    			paneServidor = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/ChatRedes/View/Servidor.fxml"));
    			AppServidor.changeStage(paneServidor);
    		} catch (IOException e) {
    			System.err.println("Erro ao iniciar servidor: localização - ControleLoginServidor.clickAction()");
    			e.printStackTrace();
    		}
    		
    	}
    	else if(obj == btnCancelar)
    	{
    		System.exit(0);
    	}
    	
    }

}

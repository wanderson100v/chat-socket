package br.com.ChatRedes.Controle;

import java.io.IOException;

import br.com.ChatRedes.App.AppServidor;
import br.com.ChatRedes.Model.Servidor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ControleLoginServidor extends Controle{

    @FXML
    private TextField tfdPorta;

    @FXML
    private TextField tfdNome;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;

    private Servidor servidor;
    
    private Pane paneServidor;
    
    @Override
	public void init() {
	}
    
    @FXML
    void clickAction(ActionEvent event) {
    	
    	Object obj = event.getSource();
    	
    	if(obj == btnEntrar)
    	{
    		servidor = new Servidor(Integer.parseInt(tfdPorta.getText().trim()), tfdNome.getText().trim());
    		try {
    			ControleServidor.iniciarServidor(servidor);
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

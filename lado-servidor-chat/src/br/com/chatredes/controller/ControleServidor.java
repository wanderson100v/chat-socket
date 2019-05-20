/**
 * 
 */
package br.com.chatredes.controller;

import br.com.chatredes.model.enums.Conexao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Mael Santos
 *
 */
public class ControleServidor extends Controle {

	@FXML
	private Label lblEstado;

	@FXML
	private TextArea txaMensagens;

	@FXML
	private TextField tfdMensagem;

	@FXML
	private Button btnEnviar;

	@Override
	public void init() {
		
		Servidor.iniciarServidor();
		lblEstado.setText(Conexao.CONECTADO+"");//modifica o estado para conectado
	}

	/**
	 * @param event
	 * <p>Método responsável por tratar a ação do clique do Button</p>
	 */    		
	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();//componente que disparou a ação

		if(obj == btnEnviar)
		{
			enviarMensagem();
		}

	}

	/**
	 * @param event
	 * <p>Método responsável por tratar a ação de entrada do TextField</p>
	 */
	@FXML
	void inputAction(KeyEvent event) {
		
		if (event.getCode() == KeyCode.ENTER) {
			if(!tfdMensagem.getText().trim().equals(""))
				enviarMensagem();
		}
	}

	/**
	 * 
	 */
	private void enviarMensagem() {

	}
}

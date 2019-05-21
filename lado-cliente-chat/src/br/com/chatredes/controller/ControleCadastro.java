package br.com.chatredes.controller;

import java.io.IOException;

import br.com.chatredes.app.AppCliente;
import br.com.chatredes.util.Mensagem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ControleCadastro extends Controle{

	@FXML
	private TextField tfdNome;

	@FXML
	private TextField tfdLogin;

	@FXML
	private PasswordField tfdSenha;

	@FXML
	private PasswordField tfdConfirmarSenha;

	@FXML
	private Button btnCadastro;

	@FXML
	private Button btnCancelar;

	private Pane loginCliente;

	@Override
	public void init() {
	}

	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();

		if(obj == btnCadastro)
		{
			if(tfdSenha.getText().trim().equals(tfdConfirmarSenha.getText().trim()))
			{
				cliente.protocoloCNU(tfdNome.getText().trim(), tfdLogin.getText().trim(), tfdSenha.getText().trim());

				notificacao.mensagemAguarde();
				new Thread(()-> {
					Mensagem mensagem = cliente.getMensagem();
		    		Platform.runLater(()->{ 
		    			if(mensagem == Mensagem.SUCESSO)
						{
							notificacao.mensagemSucesso();
							voltar();
						}
						else
							notificacao.mensagemErro();
			    	});
				}).start();
			}
			else
				notificacao.mensagemErro();
			
		}
		else if(obj == btnCancelar)
		{
			voltar();
		}

	}

	private void voltar()
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

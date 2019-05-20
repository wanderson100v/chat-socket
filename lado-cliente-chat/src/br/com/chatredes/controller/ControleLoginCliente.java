package br.com.chatredes.controller;

import java.io.IOException;

import br.com.chatredes.app.AppCliente;
import br.com.chatredes.util.Mensagem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ControleLoginCliente extends Controle{

	@FXML
	private TextField tfdLogin;

	@FXML
	private PasswordField tfdSenha;

	@FXML
	private Button btnEntrar;

	@FXML
	private Button btnCadastrar;

	@FXML
	private Button btnCancelar;

	private Pane paneCliente;
	private Pane paneCadastro;

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();

		if(obj == btnEntrar)
		{
			cliente.protocoloLOGIN(tfdLogin.getText(), tfdSenha.getText());
			try {

				notificacao.mensagemAguarde();

				if(cliente.getMensagem() == Mensagem.SUCESSO)
				{
					paneCliente = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/chatredes/view/Cliente.fxml"));
					AppCliente.changeStage(paneCliente);
				}
				else
					notificacao.mensagemErro();

			} catch (IOException e) {
				System.err.println("Erro ao iniciar servidor: localização - ControleLoginServidor.clickAction()");
			}

		}
		else if(obj == btnCadastrar)
		{
			try {
				paneCadastro = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/chatredes/view/Cadastro.fxml"));
				AppCliente.changeStage(paneCadastro);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(obj == btnCancelar)
		{
			System.exit(0);
		}

	}

}
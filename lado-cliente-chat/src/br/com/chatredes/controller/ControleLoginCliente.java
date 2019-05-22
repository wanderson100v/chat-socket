package br.com.chatredes.controller;

import java.io.IOException;
import java.util.Observable;

import br.com.chatredes.app.AppCliente;
import javafx.application.Platform;
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
			notificacao.mensagemAguarde();
			
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

	@Override
	public void update(Observable o, Object arg) {
		String[] respostaServidor = (String[]) arg;
		if(respostaServidor[0].equals("LOGIN")) {
    		if(respostaServidor[1].equals("02 SUC"))
			{
    			try {
					paneCliente = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/chatredes/view/Cliente.fxml"));
					notificacao.mensagemSucesso();
					AppCliente.changeStage(paneCliente);
				} catch (IOException e) {
					System.err.println("Erro ao iniciar servidor: localização - ControleLoginServidor.clickAction()");
				}
			}
			else
				notificacao.mensagemErro();
		}
	}

}

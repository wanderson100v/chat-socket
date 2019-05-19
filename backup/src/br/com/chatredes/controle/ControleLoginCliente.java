package br.com.chatredes.controle;

import java.io.IOException;

import br.com.chatredes.app.AppCliente;
import br.com.chatredes.app.AppServidor;
import br.com.chatredes.model.Cliente;
import br.com.chatredes.model.Servidor;
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
	private Button btnCancelar;

	private Cliente cliente;

	private Pane paneCliente;
    
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();

		if(obj == btnEntrar)
		{
			cliente = new Cliente(tfdLogin.getText().trim(), tfdLogin.getText().trim(), tfdSenha.getText().trim(), "localhost", 9000);
			try {
				ControleCliente.iniciarCliente(cliente);
				paneCliente = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/ChatRedes/View/Cliente.fxml"));
				AppCliente.changeStage(paneCliente);
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

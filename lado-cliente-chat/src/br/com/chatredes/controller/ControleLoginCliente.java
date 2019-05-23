package br.com.chatredes.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;

import br.com.chatredes.app.AppCliente;
import br.com.chatredes.model.UsuarioPublico;
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

	private UsuarioPublico usuarioLogado;
	
	@Override
	public void init() {
		
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
    			if(usuarioLogado == null)
					usuarioLogado = converterStringEmUsuarioPublico(respostaServidor[3]);
				System.out.println("Usuario Logado: "+usuarioLogado);
    			
    			try {
    				
    				FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("br/com/chatredes/view/Cliente.fxml")); 
					paneCliente = loader.load();
					ControleCliente cliente = loader.getController();
					cliente.setUsuarioLogado(usuarioLogado);
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

	private UsuarioPublico converterStringEmUsuarioPublico(String linha) {
		String[] atributosUser = linha.split(";");
		
		System.out.println("Linha: "+linha);
		
		for (String string : atributosUser) {
			System.out.println("Atributo: "+string);
		}
		
		return new UsuarioPublico(atributosUser[0],
				atributosUser[1],((!atributosUser[2].equals("null"))?LocalDateTime.parse(atributosUser[2],
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")): null), atributosUser[3]);
	}
	
}

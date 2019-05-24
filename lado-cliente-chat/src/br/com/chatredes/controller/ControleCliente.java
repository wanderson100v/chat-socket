package br.com.chatredes.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;

import br.com.chatredes.app.AppCliente;
import br.com.chatredes.model.MensagemGlobal;
import br.com.chatredes.model.UsuarioPublico;
import br.com.chatredes.view.Dialogo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private Label lblNome;

    @FXML
    private Label lblStatus;

    @FXML
    private TextField tfdMensagem;

    @FXML
    private Button btnEnviar;
    
    @FXML
    private ListView<MensagemGlobal> msgList;
    
    @FXML
    private ListView<UsuarioPublico> userList;
    
    private Pane loginCliente;
    
    private Dialogo dialogo;
    
    private UsuarioPublico usuarioLogado;
    
    @Override
	public void init() {
    	
    	dialogo = Dialogo.getInstance();
    	
    	userList.getItems().clear();
		msgList.getItems().clear();
    	Cliente.getInstance().protocoloGetUSERS();
    	Cliente.getInstance().protocoloGetMSG();
    	lblStatus.setText("Online");
    	
    	userList.setOnMouseClicked(e -> {
    		if (e.getClickCount() > 1)
    			if (userList.getSelectionModel().getSelectedItem() != null) {
    				dialogo.show(usuarioLogado, userList.getSelectionModel().getSelectedItem());
    			}
    	});
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
			cliente.protocoloLOGOUT(LocalDateTime.now());
			notificacao.mensagemAguarde();
			userList.getItems().clear();
			msgList.getItems().clear();
				try {
					if(loginCliente == null)
						loginCliente = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/chatredes/view/LoginCliente.fxml"));
					AppCliente.changeStage(loginCliente);
					notificacao.mensagemSucesso();
					usuarioLogado = null;
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
    	String msg = tfdMensagem.getText().trim();
    	if(msg.length() > 0) {
    		cliente.protocoloMSG(LocalDateTime.now(),tfdMensagem.getText());
    		tfdMensagem.setText("");
    	}
    }

	@Override
	public void update(Observable o, Object arg) {
		String[] respostaServidor = (String[]) arg;
		
		System.out.println(respostaServidor[0]);
		
		if(respostaServidor[0].equals("LOGIN")){
			if(respostaServidor[1].equals("02 SUC")) {
				try {
					for(UsuarioPublico p : userList.getItems())
					{
						System.out.println(p.getLogin());
						if(p.getLogin().equals(respostaServidor[2]))
						{
							userList.getItems().remove(p);
							p.setEstado("online");
							p.setUltimoLogin(LocalDateTime.now());
							userList.getItems().add(p);
							System.out.println("Estado do Usuario Atualizado: "+p.getNome());							
						}
					}
				} catch (Exception e) {
				}
			}
		}
		if(respostaServidor[0].equals("LOGOUT")){
			if(respostaServidor[1].equals("04 EFE"))
			{
				try {
					for(UsuarioPublico p : userList.getItems())
					{
						System.out.println(p.getLogin());
						if(p.getLogin().equals(respostaServidor[2]))
						{
							userList.getItems().remove(p);
							p.setEstado("offline");
							p.setUltimoLogin(LocalDateTime.now());
							userList.getItems().add(p);
							System.out.println("Estado do Usuario Atualizado: "+p.getNome());

						}
					}
				} catch (Exception e) {
				}
			}
			else 
				notificacao.mensagemErro();
		}
		else if(respostaServidor[0].equals("USERS")) {
    		if(respostaServidor[1].equals("02 SUC"))
			{
    			for(int i = 2; i<respostaServidor.length ; i++) {
    				userList.getItems().add(converterStringEmUsuarioPublico(respostaServidor[i]));
    			}
			}
			else
				notificacao.mensagemErro();
		}else if(respostaServidor[0].equals("MSG")) {
			if(respostaServidor[1].equals("02 SUC"))
			{
    			for(int i = 2; i<respostaServidor.length ; i++) {
    				msgList.getItems().add(converterStringEmMensagemGlobal(respostaServidor[i]));
    			}
			}else if(respostaServidor[1].equals("04 EFE") && respostaServidor[2].equals("GLOBAL")) {
				System.out.println("recebi mensagem");
				msgList.getItems().add(converterStringEmMensagemGlobal(respostaServidor[3]));
			}
			else
				notificacao.mensagemErro();
		}
	}
	
	private MensagemGlobal converterStringEmMensagemGlobal(String linha) {
		String[] atributosMsg = linha.split(";");
		return new MensagemGlobal(
				Long.parseLong(atributosMsg[0])/*id*/,
				atributosMsg[1]/*nomeRemetente*/,
				atributosMsg[2]/*loginRemetente*/,
				((!atributosMsg[3].equals("null"))?LocalDateTime.parse(atributosMsg[3],DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")): null)/*horarioEnvio*/, 
				atributosMsg[4]/*mensagem*/, 
				((!atributosMsg[5].equals("null"))?LocalDateTime.parse(atributosMsg[5],DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")): null)/*horaVizualizado*/,
				atributosMsg[6]/*loginDestinatario*/);
	}
	
	private UsuarioPublico converterStringEmUsuarioPublico(String linha) {
		String[] atributosUser = linha.split(";");
		
		return new UsuarioPublico(atributosUser[0],
				atributosUser[1],((!atributosUser[2].equals("null"))?LocalDateTime.parse(atributosUser[2],
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")): null), atributosUser[3]);
	}
	
	public void setUsuarioLogado(UsuarioPublico usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
		lblNome.setText(usuarioLogado.getNome());
	}
}

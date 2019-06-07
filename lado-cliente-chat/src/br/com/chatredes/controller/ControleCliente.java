package br.com.chatredes.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import br.com.chatredes.app.AppCliente;
import br.com.chatredes.model.MensagemGlobal;
import br.com.chatredes.model.UsuarioPublico;
import br.com.chatredes.view.DialogoDtlMsgPublica;
import br.com.chatredes.view.DialogoPrivado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import jdk.nashorn.internal.runtime.ListAdapter;

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
    private Label lblDigitando;

    @FXML
    private TextField tfdMensagem;

    @FXML
    private Button btnEnviar;
    
    @FXML
    private ListView<MensagemGlobal> msgList;
    
    @FXML
    private ListView<UsuarioPublico> userList;
    
    private Pane loginCliente;
    
    private DialogoPrivado dialogo;
    
    private UsuarioPublico usuarioLogado;
    
    private MensagemGlobal mensagemSelecionada;
    
    private boolean scrollUser = true, scrollMsg = true;
    
    @Override
	public void init() {
    	
    	msgList.addEventFilter(ScrollEvent.ANY, (e) ->{ //get every scroll event
    	      if(e.getTextDeltaY() > 0){ //set Scroll event to false when the user scrolls up
    	        scrollMsg = false;
    	      }
    	    });
    	
    	userList.addEventFilter(ScrollEvent.ANY, (e) ->{ //get every scroll event
    	      if(e.getTextDeltaY() > 0){ //set Scroll event to false when the user scrolls up
    	        scrollUser = false;
    	      }
    	    });
    	
    	dialogo = DialogoPrivado.getInstance();
    	
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
    	
    	msgList.setOnMouseClicked(e -> {
    		if (e.getClickCount() > 1)
    			if (msgList.getSelectionModel().getSelectedItem() != null) {
    				cliente.protocoloGetVISU(msgList.getSelectionModel().getSelectedItem().getId());
    				mensagemSelecionada = msgList.getSelectionModel().getSelectedItem();
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
    	else {
    		cliente.protocoloDIGIT();
    	}
    }
    
    @FXML
    void outputAction(KeyEvent event) {
    	cliente.protocoloNDIGIT();
    }


    private void enviarMensagem()
    {
    	String msg = tfdMensagem.getText().trim();
    	if(msg.length() > 0) {
    		cliente.protocoloMSG(LocalDateTime.now(),tfdMensagem.getText());
    		tfdMensagem.setText("");
    	}
    }
    
    
    /**
     * M�todo respons�vel por tratar a valida��o de visualiza��o de mensagens,
     * atrav�s da entrada de mouse em um dos campos de IO de texto(Tela de visualiza��o de
     * mensagens e entrada para envio). Assim, quando um dos respectivos campos
     * entra em foco, � sub-entendido que as mensagens foram visualizadas.
     * @param event evento de entrada de mouse em componente.
     */
    @FXML
    void focoEmCampoTexto(MouseEvent event) {
    	for(MensagemGlobal msgg : msgList.getItems()) 
    	{
    		// a mensagem n�o foi enviada pelo cliente logado e ainda n�o foi visualizada
    		if(!msgg.getLoginRemetente().equals(usuarioLogado.getLogin()) && msgg.getHoraVizualizado() == null) 
    		{
    			cliente.protocoloVISU(msgg.getId());
    		}
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
    			if(scrollUser)
    				userList.scrollTo(userList.getItems().size() -1 );
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
			if(scrollMsg)
		        msgList.scrollTo(msgList.getItems().size() -1 );
			else
				notificacao.mensagemErro();
		}else if(respostaServidor[0].equals("VISU"))
		{
			try{
				Long mensagemId = Long.parseLong(respostaServidor[2]);
				LocalDateTime horarioVisualizado = LocalDateTime.parse(respostaServidor[3],
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				if(respostaServidor[1].equals("02 SUC"))
				{
					
					for(MensagemGlobal msgg : msgList.getItems()) {
						if(msgg.getId() == mensagemId) {
							msgList.getItems().remove(msgg);
							msgg.setHoraVizualizado(horarioVisualizado);
							msgList.getItems().add(msgg);
						}
					}
				}else if(respostaServidor[1].equals("03 EXE")){
					// caso ocorreu erro deve-se tentar novamente atualizar o estado da mensagem para visualizado.
					cliente.protocoloVISU(mensagemId, horarioVisualizado);
				}
				else
					notificacao.mensagemErro();
			}catch(Exception e){ // se der exe��o significa que o retorno n tem hora ou id de mensagem, ou seja � uma resposta para detelhes de visualiaz��o e n�o edi��o de estado de mensagem
				if(respostaServidor[1].equals("02 SUC"))
				{
					System.err.println("confirmando que mensagem visualizada");
					List<String> detalhes = new ArrayList<>();
					for(int i = 2 ; i < respostaServidor.length; i ++)
						detalhes.add(respostaServidor[i]);
					System.err.println("detalhes retornado "+ detalhes);
					if(mensagemSelecionada!= null)
						DialogoDtlMsgPublica.getInstance().show(mensagemSelecionada.getNomeRemetente()+" / "+mensagemSelecionada.getLoginRemetente()
							, mensagemSelecionada.getMensagem(), mensagemSelecionada.getHorarioEnvio(),detalhes);
					mensagemSelecionada = null;
				}
				else
					notificacao.mensagemErro();
			}
		}
		else if(respostaServidor[0].equals("DIGIT/ 02 SUC")) {
			if(respostaServidor.length < 3)
				lblDigitando.setText(respostaServidor[1]+" está digitando");
		}
		else if(respostaServidor[0].equals("NDIGIT/ 02 SUC")) {
			if(respostaServidor.length < 3)
				lblDigitando.setText("");
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

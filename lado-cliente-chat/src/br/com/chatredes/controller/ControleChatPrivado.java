package br.com.chatredes.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;

import br.com.chatredes.model.MensagemGlobal;
import br.com.chatredes.model.UsuarioPublico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ControleChatPrivado extends Controle{

	@FXML
	private Label lblRemetente;

	@FXML
	private Label lblDestinatario;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblDigitando;

	@FXML
	private ListView<MensagemGlobal> msgList;

	@FXML
	private TextField tfdMensagem;

	@FXML
	private Button btnEnviar;

	private UsuarioPublico remetente;
	private UsuarioPublico destinatario;
	private MensagemGlobal mensagemSelecionada;
	

	@Override
	public void init() {

	}

	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();//componente que disparou a aÃ§Ã£o

		if(obj == btnEnviar)
		{
			if(!tfdMensagem.getText().trim().equals(""))
				enviarMensagem();
		}
	}

	@FXML
	void inputAction(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if(!tfdMensagem.getText().trim().equals(""))
				enviarMensagem();
		}
		else
			cliente.protocoloDIGITPRIV(remetente.getLogin(), destinatario.getLogin());
	}

	@FXML
	void outputAction(KeyEvent event) {
		cliente.protocoloNDIGITPRIV(destinatario.getLogin());
	}

	private void enviarMensagem()
	{
		String msg = tfdMensagem.getText().trim();
		if(msg.length() > 0) {
			cliente.protocoloMSGPRIV(destinatario.getLogin(), LocalDateTime.now(),tfdMensagem.getText());
			tfdMensagem.setText("");
		}
	}
	
	 /**
     * Método responsável por tratar a validação de visualização de mensagens,
     * através da entrada de mouse em um dos campos de IO de texto(Tela de visualização de
     * mensagens e entrada para envio). Assim, quando um dos respectivos campos
     * entra em foco, é sub-entendido que as mensagens foram visualizadas.
     * @param event evento de entrada de mouse em componente.
     */
    
	@FXML
    void focoEmCampoTexto(MouseEvent event) {
    	for(MensagemGlobal msgg : msgList.getItems()) 
    	{
    		// a mensagem não foi enviada pelo cliente logado e ainda não foi visualizada
    		if(!msgg.getLoginRemetente().equals(remetente.getLogin()) && msgg.getHoraVizualizado() == null) 
    		{
    			cliente.protocoloVISU(msgg.getId());
    		}
    	}
    }

	@Override
	public void update(Observable o, Object arg) {
		String[] respostaServidor = (String[]) arg;

		if(respostaServidor[0].equals("MSG PRIV")) {
			if(respostaServidor[1].equals("02 SUC"))
			{
				for(int i = 2; i<respostaServidor.length ; i++) {
					msgList.getItems().add(converterStringEmMensagemGlobal(respostaServidor[i]));
				}
			}else if(respostaServidor[1].equals("04 EFE") && respostaServidor[2].equals("PRIVADA")) {

				msgList.getItems().add(converterStringEmMensagemGlobal(respostaServidor[3]));
			}
			else
				notificacao.mensagemErro();
		}

		else if(respostaServidor[0].equals("LOGIN")){
			if(respostaServidor[1].equals("02 SUC")) {

				if(destinatario != null)
					if(destinatario.getLogin().equals(respostaServidor[2]))
					{
						lblStatus.setText("Online");
					}
			}
		}
		else if(respostaServidor[0].equals("LOGOUT")){
			if(destinatario != null)
				if(respostaServidor[1].equals("04 EFE"))
				{
					if(destinatario.getLogin().equals(respostaServidor[2]))
					{
						lblStatus.setText("Offline");
					}
				}
		}else if(respostaServidor[0].equals("VISU")) {
			if(destinatario!= null) {
				
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
				}
				else if(respostaServidor[1].equals("04 EFE"))
				{
					if(destinatario.getLogin().equals(respostaServidor[4]))
					{
						for(MensagemGlobal msg : msgList.getItems()) {
							if(msg.getId() == mensagemId) {
								msgList.getItems().remove(msg);
								msg.setHoraVizualizado(horarioVisualizado);
								msgList.getItems().add(msg);
							}
						}
					}
				}else if(respostaServidor[1].equals("03 EXE")){
					// caso ocorreu erro deve-se tentar novamente atualizar o estado da mensagem para visualizado.
					cliente.protocoloVISU(mensagemId, horarioVisualizado);
				}
			}
		}
		else if(respostaServidor[0].equals("DIGIT/ 02 SUC")) {
			System.out.println(respostaServidor.length);
			if(respostaServidor.length > 2)
				if(remetente.getLogin().equals(respostaServidor[1]) || destinatario.getLogin().equals(respostaServidor[2]))
					lblDigitando.setText(respostaServidor[1]+" estÃ¡ digitando");
		}
		else if(respostaServidor[0].equals("NDIGIT/ 02 SUC")) {
			if(respostaServidor.length > 2)
				if(remetente.getLogin().equals(respostaServidor[1]) || destinatario.getLogin().equals(respostaServidor[2]))
						lblDigitando.setText("");
		}
	}
	
	public void setUsuarios(UsuarioPublico remetente, UsuarioPublico destinatario)
	{
		this.destinatario = destinatario;
		this.remetente = remetente;

		msgList.getItems().clear();

		lblRemetente.setText(remetente.getNome());
		lblDestinatario.setText(destinatario.getNome());
		lblStatus.setText(destinatario.getEstado());

		cliente.protocoloGetMSGPRIV(remetente.getLogin(), destinatario.getLogin());
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
}

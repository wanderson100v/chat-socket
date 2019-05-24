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

public class ControleChatPrivado extends Controle{

	@FXML
	private Label lblRemetente;

	@FXML
	private Label lblDestinatario;

	@FXML
	private Label lblStatus;

	@FXML
	private ListView<MensagemGlobal> msgList;

	@FXML
	private TextField tfdMensagem;

	@FXML
	private Button btnEnviar;

	private UsuarioPublico remetente;
	private UsuarioPublico destinatario;

	@Override
	public void init() {

	}

	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();//componente que disparou a ação

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
	}

	private void enviarMensagem()
	{
		String msg = tfdMensagem.getText().trim();
		if(msg.length() > 0) {
			cliente.protocoloMSGPRIV(destinatario.getLogin(), LocalDateTime.now(),tfdMensagem.getText());
			tfdMensagem.setText("");
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

	private UsuarioPublico converterStringEmUsuarioPublico(String linha) {
		String[] atributosUser = linha.split(";");
		return new UsuarioPublico(atributosUser[0],
				atributosUser[1],((!atributosUser[2].equals("null"))?LocalDateTime.parse(atributosUser[2],
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")): null), atributosUser[3]);
	}
}

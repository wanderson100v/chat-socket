package br.com.chatredes.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class Notificacao {

	private static Notificacao instance;

	private Alert alert;
	
	private Notificacao() {
		alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("");
		alert.setHeaderText("");
		alert.setContentText("");

		alert.initModality(Modality.APPLICATION_MODAL);
	}

	public static Notificacao getInstance() {
		if (instance == null)
			instance = new Notificacao();
		return instance;
	}

	public void mensagemAguarde()
	{
		alert.setAlertType(AlertType.WARNING);
		alert.setTitle("Aguarde");
		alert.setContentText("Aguardando finaliza��o da opera��o");
		alert.show();
		
	}
	
	public void mensagemErro()
	{
		alert.setAlertType(AlertType.ERROR);
		alert.setTitle("Erro!!!");
		alert.setContentText("Opera��o n�o pode ser conclu�da!!!");
		alert.show();
	}
	
	public void mensagemSucesso()
	{
		alert.setAlertType(AlertType.INFORMATION);
		alert.setTitle("Conclu�do");
		alert.setContentText("Opera��o conclu�da com sucesso");
		alert.show();
	}
	
	public Boolean showConfirmacao(String titulo, String conteudo) {

		alert.setAlertType(AlertType.CONFIRMATION);
		alert.setTitle(titulo);
		alert.setContentText(conteudo);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		return false;
	}
}

package br.com.chatredes.view;

import java.io.IOException;
import java.util.Optional;

import br.com.chatredes.controller.ControleChatPrivado;
import br.com.chatredes.model.UsuarioPublico;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

public class DialogoPrivado {

	private static DialogoPrivado instance;

	private Dialog<ButtonType> dialog;
	
	private ControleChatPrivado privado;

	private DialogoPrivado() {
		dialog = new Dialog<>();
		dialog.setResizable(false);
		dialog.setWidth(780);
		dialog.setHeight(500);
		dialog.initModality(Modality.NONE);
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("br/com/chatredes/view/ChatPrivado.fxml"));
			
			Pane pane = loader.load();
			privado = loader.getController();
			
			dialog.getDialogPane().setContent(pane);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
	}	

	public static DialogoPrivado getInstance() {
		if(instance == null)
			instance = new DialogoPrivado();
		return instance;
	}
	
	public void show(UsuarioPublico remetente, UsuarioPublico destinatario)
	{
		privado.setUsuarios(remetente, destinatario);
		dialog.setWidth(780);
		dialog.setHeight(500);
		dialog.show();
	}
}

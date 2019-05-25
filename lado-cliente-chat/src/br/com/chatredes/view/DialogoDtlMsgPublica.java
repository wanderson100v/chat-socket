package br.com.chatredes.view;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;

public class DialogoDtlMsgPublica {

	private static DialogoDtlMsgPublica instance;

	private Dialog<ButtonType> dialog;
	
	private DtlMsgPublica dtlMsgPublica;
	
	public static DialogoDtlMsgPublica getInstance() {
		if(instance == null)
			instance = new DialogoDtlMsgPublica();
		return instance;
	}

	private DialogoDtlMsgPublica() {
		dialog = new Dialog<>();
		dialog.setResizable(false);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("br/com/chatredes/view/DetalhesMsgGlobal.fxml"));
			Pane pane = loader.load();
			dtlMsgPublica = loader.getController();
			dialog.getDialogPane().setContent(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
	}
	
	
	public void show(String usuarioRemetente, String textoMensagem, LocalDateTime horarioEnviado, List<String> listaVisualizadores)
	{
		dtlMsgPublica.getRemetenteLbl().setText(usuarioRemetente);
		dtlMsgPublica.getHorarioLbl().setText(horarioEnviado.toString());
		dtlMsgPublica.getMsgArea().setText(textoMensagem);
		dtlMsgPublica.getDtlMsgList().getItems().setAll(listaVisualizadores);
		dialog.show();
	}
	
}

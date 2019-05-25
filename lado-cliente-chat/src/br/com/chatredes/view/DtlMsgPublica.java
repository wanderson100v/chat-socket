package br.com.chatredes.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class DtlMsgPublica {
	
	@FXML
    private Label horarioLbl;

    @FXML
    private Label remetenteLbl;

    @FXML
    private TextArea msgArea;

    @FXML
    private ListView<String> dtlMsgList;

	public Label getHorarioLbl() {
		return horarioLbl;
	}

	public Label getRemetenteLbl() {
		return remetenteLbl;
	}

	public TextArea getMsgArea() {
		return msgArea;
	}

	public ListView<String> getDtlMsgList() {
		return dtlMsgList;
	}
}

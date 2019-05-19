package br.com.chatredes.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author mael santos
 */
public class AppCliente extends Application {

	private Pane cliente;
	private Scene sceneCliente;
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		try {
			cliente = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/ChatRedes/View/LoginCliente.fxml"));
			sceneCliente = new Scene(cliente);
			
			primaryStage.setScene(sceneCliente);
			primaryStage.setTitle("Cliente");
			primaryStage.setResizable(false);
//			primaryStage.getIcons().add(new Image(""));
			primaryStage.centerOnScreen();
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * <p>Troca os panes da tela</p>
	 * @param pane
	 */
	public static void changeStage(Pane pane) {
		stage.setScene(new Scene(pane));
	}
	
	/**
	 * @param args
	 * <p>Main Servidor</p>
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}

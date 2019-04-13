package br.com.ChatRedes.App;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author mael santos
 */
public class AppServidor extends Application {

	private Pane chat;
	private Scene sceneChat;
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			chat = FXMLLoader.load(getClass().getClassLoader().getResource("br/com/ChatRedes/View/Chat.fxml"));
			sceneChat = new Scene(chat);
			
			primaryStage.setScene(sceneChat);
			primaryStage.setTitle("ChatRedes");
//			primaryStage.getIcons().add(new Image(""));
			primaryStage.setResizable(false);
			primaryStage.centerOnScreen();
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 * <p>Main Servidor</p>
	 */
	public static void main(String[] args) {
		launch(args);
	}
}

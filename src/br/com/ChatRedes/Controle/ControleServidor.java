/**
 * 
 */
package br.com.ChatRedes.Controle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import br.com.ChatRedes.Model.Servidor;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * @author mael santos
 *
 */
public class ControleServidor extends Controle {

	@FXML
	private Label lblEstado;

	@FXML
	private TextArea txaMensagens;

	@FXML
	private TextField tfdMensagem;

	@FXML
	private Button btnEnviar;

	private static Service<Object> service;
	private static Servidor servidor;

	@Override
	public void init() {

		new Thread(new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				while (true) {

					System.out.println("Esperando...");
					Socket socket = servidor.ouvir();
					System.out.println("Ouve uma requisição");
					System.out.println("Dados do socket: "+ socket.toString());

					//Entrada de dado...(Thread separada)
					new Thread(() -> {
						try {
							BufferedReader entrada = new BufferedReader(
									new InputStreamReader(
											socket.getInputStream()
											)
									);
							PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

							String linha; 
							while((linha = entrada.readLine()) != null){
								System.out.println("Cliente ("+ socket.toString()+") Disse >>>" + linha);
								saida.println("Olá, eu recebi a sua mensagem. (" + linha + ")");
								txaMensagens.setText(txaMensagens.getText()+"\n"+"Cliente ("+ socket.toString()+") Disse >>>" + linha);
							}
							entrada.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}).start();
				}
			}
		}).start();

	}

	/**
	 * @param event
	 * <p>Método responsável por tratar a ação do clique do Button</p>
	 */    		
	@FXML
	void clickAction(ActionEvent event) {

		System.out.println("Click");

	}

	/**
	 * @param event
	 * <p>Método responsável por tratar a ação de entrada do TextField</p>
	 */
	@FXML
	void inputAction(KeyEvent event) {

		System.out.println("Digitando...");

	}

	/**
	 * @return the servidor
	 */
	/**
	 * @param servidor the servidor to set
	 */
	public static void iniciarServidor(Servidor servidor) {
		ControleServidor.servidor = servidor;
	}
}

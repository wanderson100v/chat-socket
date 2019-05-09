/**
 * 
 */
package br.com.ChatRedes.Controle;

import br.com.ChatRedes.Model.Servidor;
import br.com.ChatRedes.Model.Enum.Conexao;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * @author Mael Santos
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

	private static Servidor servidor;

	@Override
	public void init() {

		new Thread(new Task<Object>() {//thread responsavel por receber mensagens dos clientes
			@Override
			protected Object call() throws Exception {
				while (true) {

					servidor.ouvir();//esperando requisições

					String linha; 
					while((linha = servidor.trasmitir()) != null){//verificando se há mensagens
						txaMensagens.setText(txaMensagens.getText()+"Cliente: " + linha+"\n");//exibidindo as mensagens
					}
					servidor.encerrar();//encerrando o servidor
				}
			}
		}).start();//inicia a thread responsavel por receber e exibir as mensagens dos clientes

		lblEstado.setText(Conexao.CONECTADO+"");//modifica o estado para conectado
	}

	/**
	 * @param event
	 * <p>Método responsável por tratar a ação do clique do Button</p>
	 */    		
	@FXML
	void clickAction(ActionEvent event) {

		Object obj = event.getSource();//componente que disparou a ação

		if(obj == btnEnviar)
		{
			servidor.falar(tfdMensagem.getText().trim());//envia a mensagem digitada
			tfdMensagem.setText("");
		}

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

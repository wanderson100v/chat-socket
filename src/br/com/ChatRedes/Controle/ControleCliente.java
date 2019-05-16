package br.com.ChatRedes.Controle;

import br.com.ChatRedes.Model.Cliente;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Mael Santos
 *
 */
public class ControleCliente extends Controle{

    @FXML
    private Label lblEnviadas;

    @FXML
    private Label lblRecebidas;

    @FXML
    private Label lblStatus;

    @FXML
    private TextArea txaMensagens;

    @FXML
    private TextField tfdMensagem;

    @FXML
    private Button btnEnviar;

    private static Cliente cliente;
    
    @Override
	public void init() {
		
    	new Thread(new Task<Object>() {//thread responsavel por receber mensagens
			@Override
			protected Object call() throws Exception {
				while (true) {

					String linha; 
					while((linha = cliente.trasmitir()) != null){//verificando se há mensagens
						txaMensagens.setText(txaMensagens.getText()+ linha+"\n");//exibidindo as mensagens
					}
					cliente.encerrar();//encerrando o cliente
				}
			}
		}).start();//inicia a thread responsavel por receber e exibir as mensagens
		
	}
    
    @FXML
    void clickAction(ActionEvent event) {
    	
    	Object obj = event.getSource();//componente que disparou a ação

		if(obj == btnEnviar)
		{
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
    	String mensagem = cliente.getNome()+": "+tfdMensagem.getText().trim();
		cliente.falar(mensagem);//envia a mensagem digitada
		txaMensagens.setText(txaMensagens.getText() + mensagem + "\n");//atualiza as mensagens
		tfdMensagem.setText("");
    }
    
    /**
     * @param cliente the cliente to set
     */
    public static void iniciarCliente(Cliente cliente) {
    	ControleCliente.cliente = cliente;
    }

}

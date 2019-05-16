/**
 * 
 */
package br.com.ChatRedes.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Mael Santos
 *
 */

public class Servidor extends Conexao{

	private ServerSocket serverSocket;
	private int porta;
	private String nome;
	
	
	/**
	 * <p>Servidor responsavel por centralizar a troca de informações</p>
	 * @param porta
	 * @param nome
	 */
	public Servidor(int porta, String nome) {

		this.porta = porta;
		this.nome = nome;
		
		try {
			serverSocket = new ServerSocket(porta);
		} catch (IOException e) {
			System.err.println("Erro ao criar serverSocket: localização - Servidor.Servidor()");
		}
	}
	
	/**
	 * <p>Metodo aguarda uma nova requisição para somente depois iniciar os canais de entrada e saida<p/>
	 */
	public void ouvir()
	{
		try {
			Socket socket = serverSocket.accept();
			
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			saida = new PrintWriter(socket.getOutputStream(), true);
			
		} catch (IOException e) {
			System.err.println("Erro ao ouvir requisições: localização - Servidor.ouvir()");
		}
	}

	
	/**
	 * @return porta ao qual esta conectado
	 */
	public int getPorta() {
		return porta;
	}

	/**
	 * @return nome do servidor
	 */
	public String getNome() {
		return nome;
	}
	
}

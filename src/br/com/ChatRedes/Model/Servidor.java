/**
 * 
 */
package br.com.ChatRedes.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Mael Santos
 *
 */

public class Servidor {

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
	
	public Socket ouvir()
	{
		try {
			Socket socket = serverSocket.accept();
			return socket;
		} catch (IOException e) {
			System.err.println("Erro ao ouvir requisições: localização - Servidor.ouvir()");
		}
		return null;
	}

	public int getPorta() {
		return porta;
	}

	public String getNome() {
		return nome;
	}
	
}

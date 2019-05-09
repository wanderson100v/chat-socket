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

public class Servidor {

	private ServerSocket serverSocket;
	private int porta;
	private String nome;
	
	/*
	 * Variavel responsavel por receber os dados
	 */
	private BufferedReader entrada;
	/*
	 * Variavel resposavel por enviar dados
	 */
	private PrintWriter saida;
	
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
	 * <p>Transmite as mensagens recebidas pelos clientes<p/>
	 * @return mensagem enviada pelo cliente
	 */
	public String trasmitir()
	{
		String linha = null;
		try {
			linha = entrada.readLine();
		} catch (IOException e) {
			System.err.println("Erro ao Trasmitir Mensagens");
			e.printStackTrace();
		}
		
		return linha;
	}
	
	/**
	 * <p>Envia mensagens para os clientes pelo canal aberto (publico)<p/> 
	 * @param fala
	 */
	public void falar(String fala)
	{
		saida.println(fala);
	}
	
	/**
	 * <p>Encerra os canais de entrada e saida<p/>
	 */
	public void encerrar()
	{
		try {
			entrada.close();
			saida.close();
		} catch (IOException e) {
			System.err.println("Erro ao Encerrar Servidor");
			e.printStackTrace();
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

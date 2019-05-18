/**
 * 
 */
package br.com.chatredes.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Mael Santos
 *
 */
public abstract class Conexao {

	/*
	 * Variavel responsavel por receber os dados
	 */
	protected BufferedReader entrada;
	/*
	 * Variavel resposavel por enviar dados
	 */
	protected PrintWriter saida;
	
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

}

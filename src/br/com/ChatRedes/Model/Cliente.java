/**
 * 
 */
package br.com.ChatRedes.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Mael Santos
 *
 */
public class Cliente extends Conexao{

	//variavel resposanveis para fazer a conexão
	private Socket socket;
	
	//variaveis de indetificação do cliente
	private String nome;
	private String login;
	private String senha;
	
	public Cliente(String nome, String login, String senha, String host, int porta) {
		
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		
		try {
			this.socket = new Socket(host, porta);
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			saida = new PrintWriter(socket.getOutputStream(), true);
		}catch (IOException e) {
			System.err.println("Erro ao iniciar cliente - localização: Cliente.Cliente()");
		}
	}

	//getters e setters
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}

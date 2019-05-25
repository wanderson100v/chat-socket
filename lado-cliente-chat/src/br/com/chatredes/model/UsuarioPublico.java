package br.com.chatredes.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioPublico {
	
	private String nome;
	
	private String login;
	
	private LocalDateTime ultimoLogin;
	
	private String estado;
	
	public UsuarioPublico(String nome, String login, LocalDateTime ultimoLogin, String estado) {
		this.nome = nome;
		this.login = login;
		this.ultimoLogin = ultimoLogin;
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

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

	public LocalDateTime getUltimoLogin() {
		return ultimoLogin;
	}

	public void setUltimoLogin(LocalDateTime ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}

	@Override
	public String toString() {
		return estado.equals("online") ? nome + " - " + estado : nome + " - " + ((ultimoLogin != null)?ultimoLogin.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")):"null");
	}
}

package br.com.chatredes.model.viewbanco;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Subselect;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Entity
@Immutable
@Subselect("select u.nome as nome, u.login as login, u.ultimo_login as ultimoLogin from Usuario u")
public class UsuarioPublico {
	
	private String nome;
	
	@Id
	private String login;
	
	private LocalDateTime ultimoLogin;
	
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
		return nome + " - " + login;
	}
	
	public String toUsuario() {
		return nome + ";" + login +";"+((ultimoLogin != null)?ultimoLogin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")):"null");
	}
}

/**
 * 
 */
package br.com.chatredes.model.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 * @author wanderson
 *
 */
@Entity
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "usuario_seq", allocationSize =1)
public class Usuario extends Entidade{

	private static final long serialVersionUID = -583199230435379972L;
	
	@Column(length = 20, nullable = false)
	private String senha;
	
	@Column(length = 30, unique = true, nullable = false)
	private String login;
	
	@Column(length = 50, nullable = false)
	private String nome;
	
	@Column(name = "ultimo_login")
	private LocalDateTime ultimoLogin;
	
	public Usuario(Long id, String senha, String login, String nome, LocalDateTime ultimoLogin) {
		super(id);
		this.senha = senha;
		this.login = login;
		this.nome = nome;
		this.ultimoLogin = ultimoLogin;
	}

	public Usuario(String nome, String login, String senha) {
		this.senha = senha;
		this.login = login;
		this.nome = nome;
	}
	
	public Usuario() {}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

		public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome + " - " + login;
	}
	
	public String toUsuario() {
		return nome + ";" + login +";"+((ultimoLogin != null)?ultimoLogin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")):"null");
	}

	public LocalDateTime getUltimoLogin() {
		return ultimoLogin;
	}

	public void setUltimoLogin(LocalDateTime ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
	
}

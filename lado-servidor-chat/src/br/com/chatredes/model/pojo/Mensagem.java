/**
 * 
 */
package br.com.chatredes.model.pojo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.chatredes.model.enums.TipoMensagem;

/**
 * @author wanderson
 *
 */
@Entity
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "mensagem_seq", allocationSize =1)
public class Mensagem extends Entidade{

	private static final long serialVersionUID = -3963506667947690069L;
	
	@Column(name = "horario_envio", nullable = false)
	private LocalDateTime horarioEnvio;
	
	@Column(nullable = false)
	private String texto;
	
	@Column(nullable = false)
	private TipoMensagem tipo;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private Usuario remetente;
	
	public Mensagem(LocalDateTime horarioEnvio, String texto, TipoMensagem tipo, Usuario remetente) {
		this.horarioEnvio = horarioEnvio;
		this.texto = texto;
		this.tipo = tipo;
		this.remetente = remetente;
	}
	
	public Mensagem() {}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDateTime getHorarioEnvio() {
		return horarioEnvio;
	}

	public void setHorarioEnvio(LocalDateTime horarioEnvio) {
		this.horarioEnvio = horarioEnvio;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public TipoMensagem getTipo() {
		return tipo;
	}

	public void setTipo(TipoMensagem tipo) {
		this.tipo = tipo;
	}
	
	public Usuario getRemetente() {
		return remetente;
	}

	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((horarioEnvio == null) ? 0 : horarioEnvio.hashCode());
		result = prime * result + ((remetente == null) ? 0 : remetente.hashCode());
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Mensagem other = (Mensagem) obj;
		if (horarioEnvio == null) {
			if (other.horarioEnvio != null)
				return false;
		} else if (!horarioEnvio.equals(other.horarioEnvio))
			return false;
		if (remetente == null) {
			if (other.remetente != null)
				return false;
		} else if (!remetente.equals(other.remetente))
			return false;
		if (texto == null) {
			if (other.texto != null)
				return false;
		} else if (!texto.equals(other.texto))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mensagem [horarioEnvio=" + horarioEnvio + ", texto=" + texto + ", tipo=" + tipo + ", remetente="
				+ remetente + "]";
	}

}

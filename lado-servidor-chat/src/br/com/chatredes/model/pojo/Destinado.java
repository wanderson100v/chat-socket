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

/**
 * @author wanderson
 *
 */
@Entity
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "destinado_seq", allocationSize =1)
public class Destinado extends Entidade{

	private static final long serialVersionUID = -8713423301338838068L;
	
	@Column(name = "h_visu")
	private LocalDateTime hVisu;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private Usuario destinatario;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private Mensagem mensagem;

	
	public Destinado(Usuario destinatario, Mensagem mensagem) {
		this.destinatario = destinatario;
		this.mensagem = mensagem;
	}

	public Destinado() {}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDateTime gethVisu() {
		return hVisu;
	}

	public void sethVisu(LocalDateTime hVisu) {
		this.hVisu = hVisu;
	}
	
	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}

	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((destinatario == null) ? 0 : destinatario.hashCode());
		result = prime * result + ((hVisu == null) ? 0 : hVisu.hashCode());
		result = prime * result + ((mensagem == null) ? 0 : mensagem.hashCode());
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
		Destinado other = (Destinado) obj;
		if (destinatario == null) {
			if (other.destinatario != null)
				return false;
		} else if (!destinatario.equals(other.destinatario))
			return false;
		if (hVisu == null) {
			if (other.hVisu != null)
				return false;
		} else if (!hVisu.equals(other.hVisu))
			return false;
		if (mensagem == null) {
			if (other.mensagem != null)
				return false;
		} else if (!mensagem.equals(other.mensagem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Destinado [hVisu=" + hVisu + ", destinatario=" + destinatario + ", mensagem=" + mensagem + "]";
	}
	
}

package br.com.chatredes.model.viewbanco;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Subselect;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Entity
@Immutable
@Subselect(
		"select msg.id as id, "
		+ "remetente.login as loginRemetente,"
		+ "destinatario.login as loginDestinatario, "
		+ "msg.horario_envio as horarioEnvio, "
		+ "msg.texto as mensagem, "
		+ "dest.h_visu as horaVizualizado "
		+ "from Usuario remetente "
		+ "inner join Mensagem msg on(msg.remetente_id = remetente.id) "
		+ "inner join Destinado dest on(dest.mensagem_id = msg.id) "
		+ "inner join Usuario destinatario on(dest.destinatario_id = destinatario.id) "
		+ "where msg.tipo = 0")
public class MensagemGlobal {

	@Id
	private long id;
	
	private String loginRemetente ;
	
	private LocalDateTime horarioEnvio;
	
	private String mensagem;
	
	private LocalDateTime horaVizualizado;
	
	private String loginDestinatario;	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getLoginDestinatario() {
		return loginDestinatario;
	}

	public void setLoginDestinatario(String loginDestinatario) {
		this.loginDestinatario = loginDestinatario;
	}

	public String getLoginRemetente() {
		return loginRemetente;
	}

	public void setLoginRemetente(String loginRemetente) {
		this.loginRemetente = loginRemetente;
	}

	public LocalDateTime getHorarioEnvio() {
		return horarioEnvio;
	}

	public void setHorarioEnvio(LocalDateTime horarioEnvio) {
		this.horarioEnvio = horarioEnvio;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public LocalDateTime getHoraVizualizado() {
		return horaVizualizado;
	}

	public void setHoraVizualizado(LocalDateTime horaVizualizado) {
		this.horaVizualizado = horaVizualizado;
	}

	@Override
	public String toString() {
		return "MensagemGlobal [id=" + id + ", loginRemetente=" + loginRemetente + ", horarioEnvio=" + horarioEnvio
				+ ", mensagem=" + mensagem + ", horaVizualizado=" + horaVizualizado + ", loginDestinatario="
				+ loginDestinatario + "]";
	}
	
	
	
	
	
	
}

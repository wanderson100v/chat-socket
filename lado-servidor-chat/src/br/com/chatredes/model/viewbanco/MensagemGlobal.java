package br.com.chatredes.model.viewbanco;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Subselect;

import br.com.chatredes.model.enums.TipoMensagem;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Entity
@Immutable
@Subselect(
		"select msg.id as id, "
		+ "remetente.nome as nomeRemetente,"
		+ "remetente.login as loginRemetente,"
		+ "destinatario.login as loginDestinatario, "
		+ "msg.horario_envio as horarioEnvio, "
		+ "msg.texto as mensagem, "
		+ "dest.h_visu as horaVizualizado, "
		+ "msg.tipo as tipo "
		+ "from Usuario remetente "
		+ "inner join Mensagem msg on(msg.remetente_id = remetente.id) "
		+ "inner join Destinado dest on(dest.mensagem_id = msg.id) "
		+ "inner join Usuario destinatario on(dest.destinatario_id = destinatario.id)")
public class MensagemGlobal {

	@Id
	private Long id;
	
	private String nomeRemetente ;
	
	private String loginRemetente ;
	
	private LocalDateTime horarioEnvio;
	
	private String mensagem;
	
	private LocalDateTime horaVizualizado;
	
	private TipoMensagem tipo;
	
	private String loginDestinatario;
	
	
	public MensagemGlobal(Long id, String nomeRemetente, String loginRemetente, LocalDateTime horarioEnvio,
			String mensagem, String loginDestinatario) {
		super();
		this.id = id;
		this.nomeRemetente = nomeRemetente;
		this.loginRemetente = loginRemetente;
		this.horarioEnvio = horarioEnvio;
		this.mensagem = mensagem;
		this.loginDestinatario = loginDestinatario;
	}
	
	public MensagemGlobal() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	public String getNomeRemetente() {
		return nomeRemetente;
	}



	public void setNomeRemetente(String nomeRemetente) {
		this.nomeRemetente = nomeRemetente;
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

	public TipoMensagem getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoMensagem tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return nomeRemetente + ": " + mensagem + " - Recebido: " + horarioEnvio;
	}

	public String toMensagem() {
		return id + ";" + nomeRemetente + ";" + loginRemetente + ";" + ((horarioEnvio != null)?horarioEnvio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")):"null") + ";" 
				+ mensagem + ";" + ((horaVizualizado != null)?horaVizualizado.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")):"null")+ ";" + loginDestinatario;
	}
	
}

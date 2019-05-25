package br.com.chatredes.model.viewbanco;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;


@Entity
@Immutable
@Subselect(
		"select dest.id as id, "
		+ "destinatario.nome as nomeDestinatario,"
		+ "destinatario.login as loginDestinatario,"
		+ "dest.h_visu as horarioVisualizado, "
		+ "msg.id as mensagemId "
		+ "from Mensagem msg "
		+ "inner join Destinado dest on(dest.mensagem_id = msg.id) "
		+ "inner join Usuario destinatario on(dest.destinatario_id = destinatario.id) ")
public class VisualizadoDetalhes {

	@Id
	private Long id;
	
	private String nomeDestinatario;
	
	private String loginDestinatario;
	
	private LocalDateTime horarioVisualizado;
	
	private Long mensagemId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getLoginDestinatario() {
		return loginDestinatario;
	}

	public void setLoginDestinatario(String loginDestinatario) {
		this.loginDestinatario = loginDestinatario;
	}

	public LocalDateTime getHorarioVisualizado() {
		return horarioVisualizado;
	}

	public void setHorarioVisualizado(LocalDateTime horarioVisualizado) {
		this.horarioVisualizado = horarioVisualizado;
	}

	public Long getMensagemId() {
		return mensagemId;
	}

	public void setMensagemId(Long mensagemId) {
		this.mensagemId = mensagemId;
	}

	@Override
	public String toString() {
		return nomeDestinatario + " / "+ loginDestinatario + ",  visualizou as : " + horarioVisualizado;
	}
}

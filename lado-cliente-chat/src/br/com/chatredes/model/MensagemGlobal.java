package br.com.chatredes.model;

import java.time.LocalDateTime;

public class MensagemGlobal {

	private Long id;
	
	private String nomeRemetente ;
	
	private String loginRemetente ;
	
	private LocalDateTime horarioEnvio;
	
	private String mensagem;
	
	private LocalDateTime horaVizualizado;
	
	private String loginDestinatario;
	
	
	public MensagemGlobal(Long id, String nomeRemetente, String loginRemetente, LocalDateTime horarioEnvio,
			String mensagem, LocalDateTime horaVizualizado, String loginDestinatario) {
		super();
		this.id = id;
		this.nomeRemetente = nomeRemetente;
		this.loginRemetente = loginRemetente;
		this.horarioEnvio = horarioEnvio;
		this.mensagem = mensagem;
		this.horaVizualizado = horaVizualizado;
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

	@Override
	public String toString() {
		return nomeRemetente + ": " + mensagem + " - Recebido: " + horarioEnvio + " - Vizualizado: " + horaVizualizado;
	}
	
}

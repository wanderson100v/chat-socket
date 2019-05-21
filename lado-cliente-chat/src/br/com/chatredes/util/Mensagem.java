package br.com.chatredes.util;

public enum Mensagem {

	SUCESSO("Sucesso"), FALHA("Falha"), EXCECAO("Exceção");
	
	private String mensagem;
	private String[] protocoloCompleto;

	private Mensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	@Override
	public String toString() {
		return mensagem;
	}

	public String[] getProtocoloCompleto() {
		return protocoloCompleto;
	}

	public void setProtocoloCompleto(String[] protocoloCompleto) {
		this.protocoloCompleto = protocoloCompleto;
	}
	
}

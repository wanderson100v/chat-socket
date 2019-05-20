package br.com.chatredes.util;

public enum Mensagem {

	SUCESSO("Sucesso"), FALHA("Falha"), EXCECAO("Exceção");
	
	private String mensagem;

	private Mensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	@Override
	public String toString() {
		return mensagem;
	}
	
}

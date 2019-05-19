/**
 * 
 */
package br.com.chatredes.model.enums;

/**
 * @author mael santos
 *
 */
public enum Conexao {

	CONECTADO("Conectado"), DESCONECTADO("Desconectado"), SEM_CONEXAO("Sem Conexão");
	
	private String conexao;

	private Conexao(String conexao) {
		this.conexao = conexao;
	};
	
	@Override
	public String toString() {
		return conexao;
	}
	
}

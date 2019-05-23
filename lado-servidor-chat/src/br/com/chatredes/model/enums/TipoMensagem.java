/**
 * 
 */
package br.com.chatredes.model.enums;

/**
 * @author wanderson
 *
 */
public enum TipoMensagem {
	GLOBAL("Global"), PRIVADO("Privado");
	
	private String value;
	
	private TipoMensagem(String value) {
		this.value = value;
	}
	
	public static TipoMensagem getTipo(String tipoMensagem) {
		for(TipoMensagem e : values())
			if(e.getValue().equalsIgnoreCase(tipoMensagem))
				return e;
		return null;
	}
	
	public String getValue() {
		return value;
	}

}

package br.com.chatredes.model.excecoes;

public class DaoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public DaoException(String msg) {
		super(msg);
	}
	
	public DaoException(String msg, Throwable causa) {
		super(msg,causa);
	}

}

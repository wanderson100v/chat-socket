package br.com.chatredes.app;

import br.com.chatredes.controller.Servidor;
import br.com.chatredes.model.dao.DaoDestinado;
import br.com.chatredes.model.excecoes.DaoException;

public class App {

	public static void main(String[] args) {
		System.out.println("Sou o servidor\n\n");
		Servidor.iniciarServidor();
		try {
			new DaoDestinado().buscarID(new Long(1));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

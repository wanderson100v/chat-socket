package br.com.chatredes.app;

import br.com.chatredes.controller.Servidor;

public class App {

	public static void main(String[] args) {
		System.out.println("Sou o servidor\n\n");
		Servidor.iniciarServidor();
	}
	
}

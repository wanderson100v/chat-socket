package br.com.chatredes.app;

import br.com.chatredes.controller.Cliente;

public class App {

	public static void main(String[] args) {
		System.out.println("Sou o cliente\n\n");
		Cliente c = Cliente.getInstance();
		c.protocoloCNU("Wanderson", "123");
	}
	
}

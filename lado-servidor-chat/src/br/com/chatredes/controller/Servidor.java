package br.com.chatredes.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;

public class Servidor extends Observable{

	private static Servidor instance;

	public static final int porta = 17000;

	private ExecutorService clientes;

	private ServerSocket serverSocket;

	private AceitarLigacaoCliente aceitarLigacaoCliente;

	private Servidor() throws IOException {
		this.clientes = Executors.newCachedThreadPool();
		this.serverSocket = new ServerSocket(porta);
		aceitarLigacaoCliente = new AceitarLigacaoCliente();
	}
	/*
	 * garantindo apenas um servidor por execução.
	 */
	public static Servidor getInstance() {
		
		try {
			if(instance == null)
				instance = new Servidor();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return instance;
	}

	/**
	 * garantindo tratamento de exceção para visualização em tela 
	 */
	public static void iniciarServidor() {
		getInstance().aceitarLigacaoCliente.start();
	}
	/**
	 * thread responsavel por aceitar clientes e adiciona-los ao pool de theads
	 */
	private class AceitarLigacaoCliente extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					// cliente criou uma ligação com a porta. essa ligação é aceita, e aguardará o 
					// o cliente fazer requisições
					Socket socketComCliente = serverSocket.accept();
					System.out.println("Novo cliente aceito!");
					clientes.execute(new Cliente(socketComCliente));
				}catch (IOException e) {

				}
			}

		}
	}

	public void notificarOuvintes(Object object)
	{
		Platform.runLater(() -> {
			setChanged();
			notifyObservers(object);			
		});
	}
}

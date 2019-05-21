package br.com.chatredes.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Scanner;

import javafx.application.Platform;

public class Cliente extends Observable{
	
	private static Cliente instance;
	
	private Socket socket;
	
	public static final int porta = 17000;
	
	public static String ipServido;
	
	private EntradaRespostaServidor entrada;
	
	private PrintStream requisicaoServidor;	
	

	private Cliente() {
		entrada = new EntradaRespostaServidor();
		control();
		
	}
	
	public static Cliente getInstance() {
		if(instance == null)
			instance = new Cliente();
		return instance;
	}
	
	public void control() {
		try {
			this.socket = new Socket(ipServido,porta);
			this.requisicaoServidor = new PrintStream(socket.getOutputStream(),false);
			this.entrada.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// métodos para requisitar ao servidor

	public void protocoloCNU(String nome, String login, String senha) {
		requisicaoServidor.print(
				"CNU\r\n"
				+nome+"\r\n"
				+login+"\r\n"
				+senha+"\r\n"
				+ "\r\n");
	}
	
	public void protocoloLOGIN(String login, String senha) {
		
		requisicaoServidor.print(
				"LOGIN\r\n"
				+ login +"\r\n"
				+ senha+"\r\n" 
				+ "\r\n");
	}
	
	public void protocoloLOGOUT() {
		
	}
	
	public void protocoloGetUSERS() {
		
		requisicaoServidor.print(
				"GET/ USERS\r\n"
				+"\r\n");
		
	}
	
	public void protocoloGetMSG() {
		requisicaoServidor.print(
				"GET/ MSG\r\n"
				+"\r\n");
	}
	
	public void protocoloMSG(LocalDateTime horario,String texto) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		requisicaoServidor.print(
				"MSG\r\n"
				+horario.format(formatter)+"\r\n"
				+texto+"\r\n"
				+"\r\n");
	}
	
	
	public void protocoloDIGIT() {
		
	}
	
	public void protocoloVISU() {
		
	}
	
	public void protocoloGetVISU() {
		
	}
	
	
	public void protocoloNDIGIT() {
		
	}
	
	
	
	// a partir desta linha está a implementação para tratar das respostas do servidor
	private class EntradaRespostaServidor extends Thread{
		@Override
		public void run() {
			try {
				
				Scanner respostaServidor = new Scanner(socket.getInputStream());
				StringBuffer protocoloCompleto = new StringBuffer();
				while(respostaServidor.hasNextLine()) { 
					String linha = respostaServidor.nextLine();
					System.out.println(linha);
					if(linha.equals("")) { 
						String[] resposta = protocoloCompleto.toString().split("\n");
						notificarTelasDeRespostaServidor(resposta);
						protocoloCompleto = new StringBuffer();
					}else 
						protocoloCompleto.append(linha+"\n");
				}
				
				respostaServidor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void notificarTelasDeRespostaServidor(String[] protocoloResposta) {
		Platform.runLater(()->{
			setChanged();
			notifyObservers(protocoloResposta);
		});
	}

}

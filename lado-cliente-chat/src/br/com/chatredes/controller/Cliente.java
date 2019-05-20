package br.com.chatredes.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import br.com.chatredes.util.Mensagem;
import javafx.application.Platform;

public class Cliente {
	
	private static Cliente instance;
	
	private Socket socket;
	
	public static final int porta = 17000;
	
	public static String ipServido;
	
	private EntradaRespostaServidor entrada;
	
	private PrintStream requisicaoServidor;	
	
	private HashMap<String,Protocolo> protocolos;
	
	private Mensagem mensagem;

	private Cliente() {
		protocolos = new HashMap<>();
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
		
	}
	
	public void protocoloGetMSG() {
		
	}
	
	public void protocoloMSG() {
		
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
				iniciarTratamentoProtocolos();
				StringBuffer protocoloCompleto = new StringBuffer();
				while(respostaServidor.hasNextLine()) { 
					String linha = respostaServidor.nextLine();
					if(linha.equals("")) { 
						String[] requisicao = protocoloCompleto.toString().split("\n");
						Protocolo protocolo = protocolos.get(requisicao[0]);
						if(protocolo != null)
							protocolo.executarProtocolo(requisicao);
						protocoloCompleto = new StringBuffer();
					}else 
						protocoloCompleto.append(linha+"\n");
				}
				
				protocolos.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void iniciarTratamentoProtocolos() {
		protocoloRespostaCNU();
		protocoloRespostaDIGIT();
		protocoloRespostaLOGIN();
		protocoloRespostaLOGOUT();
		protocoloRespostaMSG();
		protocoloRespostaNDIGIT();
		protocoloRespostaUSERS();
		protocoloRespostaVISU();
	}
	
	public void protocoloRespostaCNU() {
		protocolos.put("CNU",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaLOGIN() {
		protocolos.put("LOGIN",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaLOGOUT() {
		protocolos.put("LOGOUT",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaUSERS() {
		protocolos.put("USERS",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaMSG() {
		protocolos.put("MSG",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaDIGIT() {
		protocolos.put("DIGIT",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaVISU() {
		protocolos.put("VISU",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	public void protocoloRespostaNDIGIT() {
		protocolos.put("NDIGIT",(String[] resposta)->{
			tratarResposta(resposta);
		});
	}
	
	private void tratarResposta(String[] resposta){

		if(resposta[1].equals("02 SUC"))
			mensagem = Mensagem.SUCESSO;
		else if(resposta[1].equals("01 ERRO"))
			mensagem = Mensagem.FALHA;
		else if(resposta[1].equals("03 EXE"))
			mensagem = Mensagem.EXCECAO;

	}
	
	public Mensagem getMensagem() {
		Mensagem temp = mensagem;
		mensagem = null;
		return temp;
	}
	
}

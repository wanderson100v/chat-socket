package br.com.chatredes.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Cliente implements Runnable {
	
	private Socket socket;
	
	private HashMap<String,Protocolo> protocolos;
	
	//meio para mandar respostas para cliente.
	private PrintStream respostasCliente;
	
	public Cliente(Socket socket) {
		this.socket = socket;
		this.protocolos = new HashMap<>();
		
	}

	@Override
	public void run() {
		try {
			this.respostasCliente = new PrintStream(socket.getOutputStream(), false);
			Scanner requisicoesCliente = new Scanner(socket.getInputStream());
			iniciarTratamentoProtocolos();
			// método hasNextLine() é bloqueante, dessa forma o laço fica parado até o cliente enviar uma requisição
			while(requisicoesCliente.hasNextLine()) { // enquanto o cliente estiver requisitando
				// vetor onde cada index é uma linha do protocolo de requisição enviado pelo cliente.
				System.out.println(Arrays.deepToString(requisicoesCliente.nextLine().split("\n")));
				/*String requisicao = requisicoesCliente.nextLine();
				System.out.println("Recebendo dados do cliente");
				System.out.println(Arrays.toString(requisicao));
				//pegando no mapa de protocolos o protocolo que o cabeçario corresponde e pedindo para o mesmo tratar a requisiçõ
				System.out.println(protocolos);
				protocolos.get
				protocolos.get(requisicao[0]).executarProtocolo(requisicao);*/
			}
			
			protocolos.clear();
			// ao sair do laço siginifica que a conexão foi fechada, então o usuário deve ser removido da lista de logados
			// quando sair do metódo a thread será removida altomaticamente do poll de threads.
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void iniciarTratamentoProtocolos(){
		//intânciando protocolos e adicionando a mapa
		protocoloCNU();
		protocoloLOGIN();
		protocoloLOGOUT();
		protocoloGetUSERS();
		protocoloGetMSG();
		protocoloMSG();
		protocoloDIGIT();
		protocoloNDIGIT();
		protocoloVISU();
		protocoloGetVISU();
	
	}
	
	public void protocoloCNU() {
		protocolos.put("CNU",(String[] requisicao)->{
			System.out.println(requisicao);
		});
	}
	
	public void protocoloLOGIN() {
		protocolos.put("LOGIN",(String[] requisicao)->{
			
		});
	}
	
	public void protocoloLOGOUT() {
		protocolos.put("LOGOUT",(String[] requisicao)->{
			
		});
	}
	
	public void protocoloGetUSERS() {
		protocolos.put("GET/ USERS",(String[] requisicao)->{
			
		});
	}
	
	public void protocoloGetMSG() {
		protocolos.put("GET/ MSG",(String[] requisicao)->{
			
		});
	}
	
	public void protocoloMSG() {
		protocolos.put("MSG",(String[] requisicao)->{
			
		});
	}
	
	
	public void protocoloDIGIT() {
		protocolos.put("DIGIT",(String[] requisicao)->{
			
		});
	}
	
	public void protocoloVISU() {
		protocolos.put("VISU",(String[] requisicao)->{
			
		});
	}
	
	public void protocoloGetVISU() {
		protocolos.put("GET/ VISU",(String[] requisicao)->{
			
		});
	}
	
	
	public void protocoloNDIGIT() {
		protocolos.put("NDIGIT",(String[] requisicao)->{
			
		});
	}
	
}

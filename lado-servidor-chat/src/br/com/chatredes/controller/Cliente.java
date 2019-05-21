package br.com.chatredes.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import br.com.chatredes.model.dao.DaoUsuario;
import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Usuario;

public class Cliente implements Runnable {
	
	private Socket socket;
	
	private HashMap<String,Protocolo> protocolos;
	
	private DaoUsuario daoUsuario;
	
	private static Vector<Cliente> clientesLogados;
	
	private Usuario usuario;
	
	//meio para mandar respostas para cliente.
	private PrintStream respostasCliente;
	
	public Cliente(Socket socket) {
		this.socket = socket;
		this.protocolos = new HashMap<>();
		this.clientesLogados = new Vector<Cliente>();
		this.daoUsuario = new DaoUsuario();
		
	}

	@Override
	public void run() {
		try {
			this.respostasCliente = new PrintStream(socket.getOutputStream(), false);
			Scanner requisicoesCliente = new Scanner(socket.getInputStream());
			iniciarTratamentoProtocolos();
			StringBuffer protocoloCompleto = new StringBuffer();
			// método hasNextLine() é bloqueante, dessa forma o laço fica parado até o cliente enviar uma requisição
			while(requisicoesCliente.hasNextLine()) { // enquanto o cliente estiver requisitando
				String linha = requisicoesCliente.nextLine();
				if(linha.equals("")) { // sgnifica que requisição chegou totalmente
					String[] requisicao = protocoloCompleto.toString().split("\n");
					//pegando no mapa de protocolos o protocolo que o cabeçario corresponde e pedindo para o mesmo tratar a requisiçõ
					Protocolo protocolo = protocolos.get(requisicao[0]);
					if(protocolo != null)
						protocolo.executarProtocolo(requisicao);
					protocoloCompleto = new StringBuffer();
				}else // se não significa faltar mais linhas
					protocoloCompleto.append(linha+"\n");
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
			for (int i = 0; i < requisicao.length; i++) {
				System.out.println(requisicao[i]);
			}
			try {
				daoUsuario.cadastrar(new Usuario(requisicao[1], requisicao[2], requisicao[3]));
				
				respostasCliente.print(
						"CNU\r\n"
						+ "02 SUC\r\n"
								+ "\r\n");
				
			} catch (DaoException e) {
				respostasCliente.print(
						"CNU\r\n"
						+ "03 EXE\r\n"
								+ "\r\n");
			}
		});
	}
	
	public void protocoloLOGIN() {
		protocolos.put("LOGIN",(String[] requisicao)->{
			try {
				this.usuario = daoUsuario.login(requisicao[1], requisicao[2]);
				if(this.usuario != null) {
					clientesLogados.add(this);
					respostasCliente.print(
							"LOGIN\r\n"
							+ "02 SUC\r\n"
									+ "\r\n");
				}
				else
					respostasCliente.print(
							"LOGIN\r\n"
							+ " 01 ERRO\r\n"
									+ "\r\n");
			} catch (DaoException e) {
				respostasCliente.print(
						"LOGIN\r\n"
						+ "03 EXE\r\n"
								+ "\r\n");
			}
			
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

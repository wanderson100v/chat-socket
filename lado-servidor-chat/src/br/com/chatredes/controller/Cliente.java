package br.com.chatredes.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
			// mÃ©todo hasNextLine() Ã© bloqueante, dessa forma o laÃ§o fica parado atÃ© o cliente enviar uma requisiÃ§Ã£o
			while(requisicoesCliente.hasNextLine()) { // enquanto o cliente estiver requisitando
				String linha = requisicoesCliente.nextLine();
				if(linha.equals("")) { // sgnifica que requisiÃ§Ã£o chegou totalmente
					String[] requisicao = protocoloCompleto.toString().split("\n");
					//pegando no mapa de protocolos o protocolo que o cabeÃ§ario corresponde e pedindo para o mesmo tratar a requisiÃ§Ãµ
					Protocolo protocolo = protocolos.get(requisicao[0]);
					if(protocolo != null)
						protocolo.executarProtocolo(requisicao);
					protocoloCompleto = new StringBuffer();
				}else // se nÃ£o significa faltar mais linhas
					protocoloCompleto.append(linha+"\n");
			}
			System.out.println("Cliente está saindo do servidor");
			operacoesDeSaida();
			protocolos.clear();
			// ao sair do laÃ§o siginifica que a conexÃ£o foi fechada, entÃ£o o usuÃ¡rio deve ser removido da lista de logados
			// quando sair do metÃ³do a thread serÃ¡ removida altomaticamente do poll de threads.
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void iniciarTratamentoProtocolos(){
		//intÃ¢nciando protocolos e adicionando a mapa
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
			operacoesDeSaida();
		});
	}
	
	/**
	 * ações padrão para logout, criado método a fim de também ser chamado pelo servidor para quando o socke fechar
	 * ou seja o cliente parar de enviar requisições (pode se observar essa açaõ no método run desta mesma classe)
	 */
	public void operacoesDeSaida() {
		this.usuario = null;
		clientesLogados.remove(this);
	}
	
	public void protocoloGetUSERS() {
		protocolos.put("GET/ USERS",(String[] requisicao)->{
			try {
				
				List<Usuario> usuarios = daoUsuario.buscarTodos();
				String protocolo = 
						"USERS\r\n"
						+ "02 SUC\r\n";
				for(Usuario usuario: usuarios) 
					protocolo+= usuario.toString()+";"+buscarUsuarioNaListaDeLogados(usuario.getLogin())+"\r\n";
				protocolo+="\r\n";
				System.out.println(protocolo);
				respostasCliente.print(protocolo);
			} catch (DaoException e) {
				respostasCliente.print(
						"USERS\r\n"
						+ "03 EXE\r\n"
						+ "\r\n");
			}
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
	
	private boolean buscarUsuarioNaListaDeLogados(String login) {
		for(Cliente cliente : clientesLogados)
			if(cliente.usuario != null && cliente.usuario.getLogin().equals(login))
				return true;
		return false;
	}
	
}

package br.com.chatredes.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.Vector;

import br.com.chatredes.model.dao.DaoDestinado;
import br.com.chatredes.model.dao.DaoMensagem;
import br.com.chatredes.model.dao.DaoUsuario;
import br.com.chatredes.model.enums.TipoMensagem;
import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Destinado;
import br.com.chatredes.model.pojo.Mensagem;
import br.com.chatredes.model.pojo.Usuario;
import br.com.chatredes.model.viewbanco.MensagemGlobal;
import br.com.chatredes.model.viewbanco.UsuarioPublico;

public class Cliente extends Observable implements Runnable {
	
	private Socket socket;
	
	private HashMap<String,Protocolo> protocolos;
	
	private DaoUsuario daoUsuario;
	
	private DaoMensagem daoMensagem;
	
	private DaoDestinado daoDestinado;
	
	private static Vector<Cliente> clientesLogados = new Vector<Cliente>();
	
	private Usuario usuario;
	
	private PrintStream respostasCliente;
	
	public Cliente(Socket socket) {
		this.socket = socket;
		this.protocolos = new HashMap<>();
		this.daoUsuario = new DaoUsuario();
		this.daoMensagem = new DaoMensagem();
		this.daoDestinado = new DaoDestinado();
		
	}

	@Override
	public void run() {
		try {
			this.respostasCliente = new PrintStream(socket.getOutputStream(), false);
			Scanner requisicoesCliente = new Scanner(socket.getInputStream());
			iniciarTratamentoProtocolos();
			StringBuffer protocoloCompleto = new StringBuffer();
			while(requisicoesCliente.hasNextLine()) { // enquanto o cliente estiver requisitando
				String linha = requisicoesCliente.nextLine();
				if(linha.equals("")) {
					String[] requisicao = protocoloCompleto.toString().split("\n");
					Protocolo protocolo = protocolos.get(requisicao[0]);
					if(protocolo != null)
						protocolo.executarProtocolo(requisicao);
					protocoloCompleto = new StringBuffer();
				}else 
					protocoloCompleto.append(linha+"\n");
			}
			System.out.println("Cliente est� saindo do servidor");
			operacoesDeSaida();
			requisicoesCliente.close();
			
			protocolos.clear();
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
				System.out.println("inciando protocolo de login do lado so servidor");
				this.usuario = daoUsuario.login(requisicao[1], requisicao[2]);
				if(this.usuario != null) {
					clientesLogados.add(this);
					System.out.println("Todos os clientes logados at� o momento");
					for(Cliente cliente : clientesLogados)
						System.out.println("Cliente logado "+ cliente.usuario.getLogin());
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
			
			try {
				usuario.setUltimoLogin(LocalDateTime.parse(requisicao[1]));
				daoUsuario.editar(usuario);
				
				respostasCliente.print(
						"LOGOUT\r\n" 
						+"04 EFE\r\n"
						+usuario.getLogin()+"\r\n"
						+"\r\n");
				
				for (Cliente c : clientesLogados) {
					c.respostasCliente.print(
							"LOGOUT\r\n" 
									+"04 EFE\r\n"
									+usuario.getLogin()+"\r\n"
									+"\r\n");
				}
				
				operacoesDeSaida();
				
			} catch (DaoException e) {
				respostasCliente.print(
						"LOGOUT\r\n"
						+ "+03 EXE\r\n"
						+ "+\r\n");
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * a��es padr�o para logout, criado m�todo a fim de tamb�m ser chamado pelo servidor para quando o socke fechar
	 * ou seja o cliente parar de enviar requisi��es (pode se observar essa a�a� no m�todo run desta mesma classe)
	 */
	public void operacoesDeSaida() {
		this.usuario = null;
		clientesLogados.remove(this);
	}
	
	public void protocoloGetUSERS() {
		protocolos.put("GET/ USERS",(String[] requisicao)->{
			try {
				System.out.println("iniciando protocolo de dados publicos de todos os usu�rios do lado do servidor");
				List<UsuarioPublico> usuarios = daoUsuario.buscarTodos();
				String protocolo = 
						"USERS\r\n"
						+ "02 SUC\r\n";
				for(UsuarioPublico usuario: usuarios) 
							protocolo+= usuario.toString()+";"
							+((buscarUsuarioNaListaDeLogados(usuario.getLogin()))? "online":"offline")+"\r\n";
				protocolo+="\r\n";
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
			try {
				System.out.println("iniciando protocolo de buscar mensagem globais do lado do servidor");
				List<MensagemGlobal> mensagensGlobais =  daoMensagem.buscarMensagensGlobais(this.usuario.getLogin());
				String protocolo = "MSG\r\n"
								 + "02 SUC\r\n";
				for(MensagemGlobal mensagemGlobal :mensagensGlobais)
					protocolo+= mensagemGlobal+"\r\n";
				protocolo+= "\r\n";
				System.out.println("mensagens globais que seram enviadas para o cliente");
				System.out.println(protocolo);
				respostasCliente.print(protocolo);
			} catch (DaoException e) {
				respostasCliente.print(
						"MSG\r\n"
						+ "03 EXE\r\n"
								+ "\r\n");
			}
		});
	}
	
	public void protocoloMSG() {
		protocolos.put("MSG",(String[] requisicao)->{
			System.out.println("iniciando protocolo de envio de mensagem do lado do servidor");
			LocalDateTime horarioEnvio = LocalDateTime.parse(requisicao[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			String texto = requisicao[2];
			Mensagem mensagem = new Mensagem(horarioEnvio, texto,TipoMensagem.global,usuario);
			try {
				// cadastrando menssagem em banco e destinando a cada individou, a fim de
				// possibilidade de valida��o de que visualizou.
				daoMensagem.cadastrar(mensagem);
				System.out.println("cadastrando mensagem = "+ mensagem);
				for(Usuario destinatario : daoUsuario.buscarAll()) {
					daoDestinado.cadastrar(new Destinado(destinatario, mensagem));
					System.out.println("enviando mensagem para "+destinatario.getLogin());
				}
				// avisar a todos os clientes ativos
				for(Cliente clienteReceptor :clientesLogados) {
					System.out.println("enviando mensagem para clientes ativos atrav�s do protocolo");
					clienteReceptor.respostasCliente.print(
							"MSG\r\n"
							+"04 EFE\r\n"
							+"GLOBAL\r\n"
							+(new MensagemGlobal(mensagem.getId(),usuario.getNome(),
									usuario.getLogin(), horarioEnvio, texto,
									clienteReceptor.usuario.getLogin()))+"\r\n"
							+"\r\n"
							);
				}
				
				
			} catch (DaoException e) {
				e.printStackTrace();
			}
			
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

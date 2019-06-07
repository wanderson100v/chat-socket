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

import br.com.chatredes.model.viewbanco.VisualizadoDetalhes;

public class Cliente extends Observable implements Runnable {
	
	private Socket socket;
	
	private HashMap<String,Protocolo> protocolos;
	
	private DaoUsuario daoUsuario;
	
	private DaoMensagem daoMensagem;
	
	private DaoDestinado daoDestinado;
	
	private static Vector<Cliente> clientesLogados = new Vector<Cliente>();
	
	private Usuario usuario;
	
	private Servidor servidor;
	
	private PrintStream respostasCliente;
	
	public Cliente(Socket socket) {
		this.socket = socket;
		this.protocolos = new HashMap<>();
		this.daoUsuario = new DaoUsuario();
		this.daoMensagem = new DaoMensagem();
		this.daoDestinado = new DaoDestinado();
		this.servidor = Servidor.getInstance();
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
				
				System.err.println("total de conex�es: "+clientesLogados.size() );
				System.out.println("inciando protocolo de login do lado so servidor");
				this.usuario = daoUsuario.login(requisicao[1], requisicao[2]);
				if(this.usuario != null) {
					if(!clientesLogados.contains(this))
						clientesLogados.add(this);
					System.out.println("Todos os clientes logados até o momento");
					
					String protocolo = 
							"LOGIN\r\n"
							+"02 SUC\r\n"
							+requisicao[1]+"\r\n";

					protocolo+= usuario.toUsuario()+";"
					+((buscarUsuarioNaListaDeLogados(usuario.getLogin()))? "online":"offline")+"\r\n";
					protocolo+="\r\n";
					
					notificarTodosClientes(protocolo);
					
					servidor.notificarOuvintes(clientesLogados);
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
				
				notificarTodosClientes("LOGOUT\r\n"
						+"04 EFE\r\n"
						+usuario.getLogin()+"\r\n"
						+"\r\n");
				
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
		System.err.println("Saindo do servdor");
		this.usuario = null;
		clientesLogados.remove(this);
		servidor.notificarOuvintes(clientesLogados);
	}
	
	public void protocoloGetUSERS() {
		protocolos.put("GET/ USERS",(String[] requisicao)->{
			try {
				System.out.println("iniciando protocolo de dados publicos de todos os usuários do lado do servidor");
				List<UsuarioPublico> usuarios = daoUsuario.buscarTodos();
				String protocolo = 
						"USERS\r\n"
						+ "02 SUC\r\n";
				for(UsuarioPublico usuario: usuarios) 
							protocolo+= usuario.toUsuario()+";"
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
			
			if(requisicao.length > 1 && requisicao[1] != null)
			{
				try {
					
					List<MensagemGlobal> mensagensGlobais =  daoMensagem
							.buscarMensagensPrivadas(requisicao[1], requisicao[2]);
					String protocolo = "MSG PRIV\r\n"
							+ "02 SUC\r\n";
					for(MensagemGlobal mensagemGlobal :mensagensGlobais)
						protocolo+= mensagemGlobal.toMensagem()+"\r\n";
					protocolo+= "\r\n";
					
					respostasCliente.print(protocolo);
					
				} catch (DaoException e) {
					respostasCliente.print(
							"MSG\r\n"
									+ "03 EXE\r\n"
									+ "\r\n");
				}
			}
			else
			{
				try {
					System.out.println("iniciando protocolo de buscar mensagem globais do lado do servidor");
					List<MensagemGlobal> mensagensGlobais =  daoMensagem.buscarMensagensGlobais(this.usuario.getLogin());
					String protocolo = "MSG\r\n"
							+ "02 SUC\r\n";
					for(MensagemGlobal mensagemGlobal :mensagensGlobais)
						protocolo+= mensagemGlobal.toMensagem()+"\r\n";
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
			}
			
		});
	}
	
	public void protocoloMSG() {
		protocolos.put("MSG",(String[] requisicao)->{
			System.out.println(requisicao.length);
			if(requisicao.length > 3)
			{
				LocalDateTime horarioEnvio = LocalDateTime.parse(requisicao[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				String texto = requisicao[2];
				Mensagem mensagem = new Mensagem(horarioEnvio, texto, TipoMensagem.PRIVADO, usuario);
				try {
					
					daoMensagem.cadastrar(mensagem);
					
					for(Usuario destinatario : daoUsuario.buscarAll()) {
						if(destinatario.getLogin().equals(requisicao[3]))
							daoDestinado.cadastrar(new Destinado(destinatario, mensagem));
					}
					
					
					for(Cliente clienteReceptor : clientesLogados) {
						if(clienteReceptor.usuario != null && 
								clienteReceptor.usuario.getLogin().equals(requisicao[3]))
							clienteReceptor.respostasCliente.print(
									"MSG PRIV\r\n"
											+"04 EFE\r\n"
											+"PRIVADA\r\n"
											+(new MensagemGlobal(mensagem.getId(),usuario.getNome(),
													usuario.getLogin(), horarioEnvio, texto,
													clienteReceptor.usuario.getLogin())).toMensagem()+"\r\n"
													+"\r\n"
									);
					}
					respostasCliente.print(
							"MSG PRIV\r\n"
									+"04 EFE\r\n"
									+"PRIVADA\r\n"
									+(new MensagemGlobal(mensagem.getId(),usuario.getNome(),
											usuario.getLogin(), horarioEnvio, texto,
											requisicao[3])).toMensagem()+"\r\n"
											+"\r\n"
							);
					
				} catch (DaoException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("iniciando protocolo de envio de mensagem do lado do servidor");
				LocalDateTime horarioEnvio = LocalDateTime.parse(requisicao[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				String texto = requisicao[2];
				Mensagem mensagem = new Mensagem(horarioEnvio, texto,TipoMensagem.GLOBAL,usuario);
				try {
					// cadastrando menssagem em banco e destinando a cada individou, a fim de
					// possibilidade de validação de que visualizou.
					daoMensagem.cadastrar(mensagem);
					System.out.println("cadastrando mensagem = "+ mensagem);
					for(Usuario destinatario : daoUsuario.buscarAll()) {
						daoDestinado.cadastrar(new Destinado(destinatario, mensagem));
						System.out.println("enviando mensagem para "+destinatario.getLogin());
					}
					// avisar a todos os clientes ativos
					for(Cliente clienteReceptor :clientesLogados) {
						System.out.println("enviando mensagem para clientes ativos através do protocolo");
						
						MensagemGlobal global = new MensagemGlobal(mensagem.getId(),usuario.getNome(),
								usuario.getLogin(), horarioEnvio, texto,
								clienteReceptor.usuario.getLogin()); 
						
						clienteReceptor.respostasCliente.print(
								"MSG\r\n"
										+"04 EFE\r\n"
										+"GLOBAL\r\n"
										+global.toMensagem()+"\r\n"
												+"\r\n"
								);
						
					}
					servidor.notificarOuvintes(new MensagemGlobal(mensagem.getId(),usuario.getNome(),
							usuario.getLogin(), horarioEnvio, texto,""));
					
				} catch (DaoException e) {
					e.printStackTrace();
				}
			}
			
			
		});
	}
	
	
	public void protocoloDIGIT() {
		protocolos.put("DIGIT",(String[] requisicao)->{
			
			if(requisicao.length > 1)
			{
				
				for(Cliente cliente : clientesLogados)
				{
					if(cliente.getUsuario().getLogin().equals(requisicao[2]))
					{
						cliente.respostasCliente.print(
								"DIGIT/ 02 SUC\r\n"
								+usuario.getLogin()+"\r\n"
								+requisicao[2]+"\r\n"
								+"\r\n");
						break;
					}
				}
				respostasCliente.print(
						"DIGIT/ 02 SUC\r\n"
						+usuario.getLogin()+"\r\n"
						+requisicao[2]+"\r\n"
						+"\r\n");
			}
			else
			{
				notificarTodosClientes(
						"DIGIT/ 02 SUC\r\n"
								+usuario.getNome()+"\r\n"
								+"\r\n");
			}
		});
	}
	

	public void protocoloNDIGIT() {
		protocolos.put("NDIGIT",(String[] requisicao)->{
			if(requisicao.length > 1)
			{
				
				for(Cliente cliente : clientesLogados)
				{
					if(cliente.getUsuario().getLogin().equals(requisicao[1]))
					{
						cliente.respostasCliente.print(
								"NDIGIT/ 02 SUC\r\n"
								+usuario.getLogin()+"\r\n"
								+requisicao[1]+"\r\n"
								+"\r\n");
//						break;
					}
				}
				respostasCliente.print(
						"NDIGIT/ 02 SUC\r\n"
						+usuario.getLogin()+"\r\n"
						+requisicao[1]+"\r\n"
						+"\r\n");
			}
			else
			{
				notificarTodosClientes(
						"NDIGIT/ 02 SUC\r\n"
								+usuario.getNome()+"\r\n"
								+"\r\n");
			}
		});
	}

	public void protocoloVISU() {
		protocolos.put("VISU",(String[] requisicao)->{
			Long mensagemId = -1l;
			LocalDateTime horarioVisualizado = null;
			try {
				System.err.println("inciando protocolo de atualizar estado de mensagem do lado do servidor");
				mensagemId = Long.parseLong(requisicao[1]);
				Mensagem mensagem = daoMensagem.buscarID(mensagemId);
				Usuario destinatario = this.usuario;
				Destinado destinado = daoDestinado.buscarPorDesginadoMsg(destinatario, mensagem);
				horarioVisualizado = LocalDateTime.parse(requisicao[2],DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				destinado.sethVisu(horarioVisualizado);
				daoDestinado.editar(destinado);
				System.err.println("\ntudo certo, enviando sucesso pro cliente");
				respostasCliente.print(
						"VISU\r\n"
						+"02 SUC\r\n"
						+mensagemId+"\r\n"
						+horarioVisualizado.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"\r\n"
						+"\r\n"
						);
				
				// caso a mensagem seja privada
				if(mensagem.getTipo().equals(TipoMensagem.PRIVADO)) 
				{
					System.err.println("----------------msg privaada mandando efetiva��o para ");
					//procurar o cliente que a enviou
					for(Cliente cliente : clientesLogados) 
					{
						// se estiver logado
						if(cliente.usuario != null 
								&& cliente.usuario.getLogin().equals(mensagem.getRemetente().getLogin())) 
						{
							System.err.println("----------------"+mensagem.getRemetente().getLogin());
							//Avisar ao cliente que a mensagem dele foi visualizada
							cliente.respostasCliente.print(
									"VISU\r\n"
									+"04 EFE\r\n"
									+mensagemId+"\r\n"
									+horarioVisualizado.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"\r\n"
									+usuario.getLogin()+"\r\n"
									+"\r\n"
									);
						}
					}
				}
				
			} catch (Exception e) {
				respostasCliente.print(
						"VISU\r\n"
						+"03 EXE\r\n"
						+mensagemId+"\r\n"
						+horarioVisualizado.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"\r\n"
						+"\r\n"
						);
			}

			
			if(requisicao.length > 1)
			{
				
				for(Cliente cliente : clientesLogados)
				{
					if(cliente.getUsuario().getLogin().equals(requisicao[1]))
					{
						cliente.respostasCliente.print("NDIGIT/ 02 SUC\r\n"
								+usuario.getLogin()+"\r\n"
								+requisicao[1]+"\r\n"
								+"\r\n");
						break;
					}
				}
				respostasCliente.print("NDIGIT/ 02 SUC\r\n"
						+usuario.getLogin()+"\r\n"
						+requisicao[1]+"\r\n"
						+"\r\n");
			}
			else
			{
				notificarTodosClientes(
						"NDIGIT/ 02 SUC\r\n"
								+usuario.getLogin()+"\r\n"
								+"\r\n");
			}
		});
	}
	

	public void protocoloGetVISU() {
		protocolos.put("GET/ VISU",(String[] requisicao)->{
			try {
				Long mensagemId = Long.parseLong(requisicao[1]);
				List<VisualizadoDetalhes> detalhes = daoDestinado.detelhesMensagem(mensagemId);
				String protocoloReposta= 
						"VISU\r\n"
						+"02 SUC\r\n";
				for(VisualizadoDetalhes vDetalhes : detalhes)
					protocoloReposta+= (vDetalhes+"\r\n");
				protocoloReposta += "\r\n";
				respostasCliente.print(protocoloReposta);
			}catch (Exception e) {
				respostasCliente.print(
						"VISU\r\n"
						+"03 EXE\r\n"
						+"\r\n"
						);
			}
		});
	}
	
//	public void protocoloGetVISU() {
//		protocolos.put("GET/ VISU",(String[] requisicao)->{
//			notificarTodosClientes(
//					"DIGIT/ 02 SUC\r\n"
//					+usuario.getNome()+"\r\n"
//					+"\r\n");
//		});
//	}
	
	private boolean buscarUsuarioNaListaDeLogados(String login) {
		for(Cliente cliente : clientesLogados)
			if(cliente.usuario != null && cliente.usuario.getLogin().equals(login))
				return true;
		return false;
	}
	
	private static void notificarTodosClientes(String resposta)
	{
		for (Cliente c : clientesLogados) {
			if(c.usuario != null)
				c.respostasCliente.print(resposta);
		}
	}
	
	@Override
	public String toString() {
		return usuario+"";
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
}

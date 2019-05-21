package br.com.chatredes.model.dao;

import java.util.List;

import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Entidade;

public interface IDao <T extends Entidade>{
	
	public void cadastrar(T t) throws DaoException;
	
	public void editar(T t) throws DaoException;
	
	public T buscarID(Long id) throws DaoException;
	
	public void excluir(T t) throws DaoException;
	
	public List<T> buscarAll() throws DaoException;

}

package br.com.chatredes.model.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.chatredes.model.dao.util.ConnectionFactory;
import br.com.chatredes.model.excecoes.DaoException;
import br.com.chatredes.model.pojo.Entidade;

public abstract class Dao<T extends Entidade>{
	
	protected Class<T> tipoDaClasse;
	
	protected EntityManager em;
	
	public Dao(Class<T> tipoDaClasse) {
		this.tipoDaClasse = tipoDaClasse;
		em = ConnectionFactory.getConnection();
	}
	
	public void cadastrar(T t) throws DaoException{
		try{
//			em = ConnectionFactory.getConnection();
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
		}catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO CADASTRAR "+tipoDaClasse.getSimpleName().toUpperCase()+" CONTATE O ADM.");
		}
//		finally {
//			em.close();
//		}
	}
	
	public void editar(T t) throws DaoException {
		try{
//			em = ConnectionFactory.getConnection();
			em.getTransaction().begin();
			em.merge(t);
			em.getTransaction().commit();
		}catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO EDITAR "+tipoDaClasse.getSimpleName().toUpperCase()+", CONTATE O ADM.");
		}
//		finally {
//			em.close();
//		}
	}
	
	public T buscarID(Long id) throws DaoException {
		T t = null;
		try{
//			em = ConnectionFactory.getConnection();
			t = em.find(tipoDaClasse,id);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR "+tipoDaClasse.getSimpleName().toUpperCase()+" POR ID, CONTATE O ADM.");
		}
//		finally {
//			em.close();
//		}
		return t;
	}
	
	public void excluir(T t) throws DaoException {
		try{
//			em = ConnectionFactory.getConnection();
			em.getTransaction().begin();
			Entidade entidade = em.merge(t);
			em.remove(entidade);
			em.getTransaction().commit();
		}catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO EXCLUIR "+tipoDaClasse.getSimpleName().toUpperCase()+", CONTATE O ADM",e);
		}
//		finally {
//			em.close();
//		}
	}
	
	public List<T> buscarAll() throws DaoException {
		List<T> t = new ArrayList<>();
		try {
			t =  em.createQuery("from "+tipoDaClasse.getSimpleName()+" elemento",tipoDaClasse).getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("OCORREU UM ERRO AO BUSCAR TODOS "+tipoDaClasse.getSimpleName().toUpperCase()+", CONTATE O ADM.");
		}
		return t;
	}
}

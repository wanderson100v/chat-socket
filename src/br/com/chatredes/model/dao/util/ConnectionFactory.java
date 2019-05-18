package br.com.chatredes.model.dao.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ConnectionFactory{
	private static EntityManagerFactory entityManagerFactory;
	
	public static EntityManager getConnection() {
		return entityManagerFactory.createEntityManager();
	}
	
}
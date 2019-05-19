package br.com.chatredes.model.dao.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory{
	private static EntityManagerFactory entityManagerFactory;
	
	public static EntityManager getConnection() {
		if(entityManagerFactory == null ) {
			entityManagerFactory = Persistence.createEntityManagerFactory("banco");
		}
		return entityManagerFactory.createEntityManager();
	}
	
}
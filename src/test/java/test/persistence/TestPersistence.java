package test.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import test.po.Pet;

public class TestPersistence {
	
	public void save(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAPU");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Pet pet = new Pet();
		pet.setNumber("weejkl");
		pet.setAge(2);
		pet.setName("非常小");
		pet.setType("dog");
		em.persist(pet);
		em.getTransaction().commit();
		emf.close();
	}
}

package creditcard.project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
	private static final String PERSISTENCE_UNIT_NAME = "creditcard";
	private static EntityManagerFactory factory;
	
	public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
         
        
        em.getTransaction().begin();
        Person person = new Person();
        person.setFirstName("Erlend");
        em.persist(person);
        em.getTransaction().commit();
        
        
        em.close();
        
	}
	
}

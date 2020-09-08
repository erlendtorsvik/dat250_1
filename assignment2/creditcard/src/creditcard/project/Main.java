package creditcard.project;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Main {
	private static final String PERSISTENCE_UNIT_NAME = "creditcard";
	private static EntityManagerFactory factory;
	
	public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
         
        
        em.getTransaction().begin();
        Person p = new Person();
        p.setFirstName("Erlend");
        em.persist(p);
        em.getTransaction().commit();
        
        
        em.close();
        
	}
	
}

package no.hvl.dat250.restapi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import javax.persistence.*;

public class TodoDAO {
    private final String PERSISTENCE_UNIT_NAME = "todos";
    private EntityManagerFactory factory;
    @PersistenceContext
    private EntityManager em;

    public TodoDAO(){
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    public void addTodo(Todo todo){
        em.getTransaction().begin();
        em.persist(todo);
        em.getTransaction().commit();

    }
    public List<Todo> getTodo()
    {
        Query query = em.createQuery("SELECT t FROM Todo t");
        return query.getResultList();
    }

    public Todo getTodo(long id){
        return em.find(Todo.class, id);
    }

    public void updateTodo(Todo todo){
        em.getTransaction().begin();
        em.merge(todo);
        em.getTransaction().commit();

    }

    public void deleteTodo(long id){
        Todo t = em.find(Todo.class, id);
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();

    }

}

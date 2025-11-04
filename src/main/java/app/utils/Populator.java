package app.utils;

import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;

public class Populator {
    public void createEntities(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Skill").executeUpdate();
            em.createQuery("DELETE FROM Candidate ").executeUpdate();

            Candidate c1 = new Candidate("Bob", 28567015, "Bachelor");
            Candidate c2 = new Candidate("Mimi", 38475649, "No background");

            Skill s1 = new Skill("Databases", Category.DB, "Databases and data storage technologies");
            Skill s2 = new Skill("Html, css", Category.FRONTEND, "Front-end and UI-related technologies");
            Skill s3 = new Skill("Test", Category.TESTING, "Tools and frameworks for testing and QA");

            c1.getSkills().add(s1);
            c2.getSkills().add(s2);

            em.persist(c1);
            em.persist(c2);
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);

            em.getTransaction().commit();

            System.out.println("Entities in DB:");
            em.createQuery("SELECT c FROM Candidate c ORDER BY c.id DESC ", Candidate.class)
                    .getResultList()
                    .forEach(System.out::println);
        }

    }
}

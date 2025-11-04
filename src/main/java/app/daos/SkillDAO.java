package app.daos;

import app.config.HibernateConfig;
import app.entities.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SkillDAO implements IDAO<Skill, Integer> {
    private static SkillDAO instance;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public SkillDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static SkillDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SkillDAO(emf);
        }
        return instance;
    }


    @Override
    public Skill create(Skill skill) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(skill);
            em.getTransaction().commit();
        }
        return skill;
    }

    @Override
    public List<Skill> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Skill> query = em.createQuery("SELECT s FROM Skill s", Skill.class);
            return query.getResultList();
        }
    }

    @Override
    public Skill getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Skill.class, id);
        }
    }

    @Override
    public Skill update(Skill skill) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Skill skillToUpdate = em.merge(skill);
            em.getTransaction().commit();
            return skillToUpdate;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skillToDelete = em.find(Skill.class, id);
            if(skillToDelete == null){
                em.getTransaction().rollback();
                return false;
            }
            em.remove(skillToDelete);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

package app.daos;

import app.config.HibernateConfig;
import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CandidateDAO implements IDAO<Candidate, Integer> {
    private static CandidateDAO instance;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public CandidateDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static CandidateDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CandidateDAO(emf);
        }
        return instance;
    }


    @Override
    public Candidate create(Candidate candidate) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(candidate);
            em.getTransaction().commit();
        }
        return candidate;
    }

    @Override
    public List<Candidate> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Candidate> query = em.createQuery("SELECT c FROM Candidate c", Candidate.class);
            return query.getResultList();
        }
    }

    @Override
    public Candidate getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Candidate.class, id);
        }
    }

    @Override
    public Candidate update(Candidate candidate) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Candidate c = em.merge(candidate);
            em.getTransaction().commit();
            return c;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Candidate c = em.find(Candidate.class, id);
            if(c != null){
                em.getTransaction().begin();
                em.remove(c);
                em.getTransaction().commit();
                return true;
            }else{
                return false;
            }
        }catch(PersistenceException ex){
            return false;
        }
    }

    public void attachSkillToCandidate(int candidateId, int skillId) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            Candidate candidate = em.find(Candidate.class, candidateId);
            Skill skill = em.find(Skill.class, skillId);

            if(skill == null || candidate == null){
                throw new IllegalArgumentException("Skill or candidate not found");
            }

            candidate.getSkills().add(skill);

            em.merge(candidate);
            em.getTransaction().commit();

        }
    }

    public List<Candidate> getAllCandidatesBySkillCategory(String category) {
        try (EntityManager em = emf.createEntityManager()) {
            Category cat = Category.valueOf(category.toUpperCase());
            return em.createQuery("SELECT DISTINCT c FROM Candidate c JOIN c.skills s WHERE s.category = :category", Candidate.class)
                    .setParameter("category", cat)
                    .getResultList();
        }catch(IllegalArgumentException e){
            System.out.println("Invalid category: " + category);
            return List.of();
        }
    }
}

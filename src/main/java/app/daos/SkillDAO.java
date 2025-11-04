package app.daos;

import app.entities.Candidate;
import app.entities.Skill;
import app.exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SkillDAO implements IDAO<Skill,Integer>{
    private EntityManagerFactory emf;

    public SkillDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Skill create(Skill skill) {
        try(EntityManager em = emf.createEntityManager()){
            try{
                em.getTransaction().begin();
                em.persist(skill);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();}
            }
        }
        return skill;
    }

    @Override
    public List<Skill> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            try {
                TypedQuery<Skill> query = em.createQuery("SELECT s FROM Skill s", Skill.class);
                return query.getResultList();
            } catch (Exception e) {
                e.printStackTrace(); //No rollback() in read-op
            }
            throw new DAOException("Kunne ikke hente alle gemte skills");
        }
    }


    @Override
    public Skill getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            try {//em.getTransaction().begin();
                Skill skill = em.find(Skill.class, id);
                //em.getTransaction().commit();
                return skill;
            }catch (Exception e){
                e.printStackTrace();
                throw new DAOException("Kunne ikke hente skill(id: "+id+ ") ud fra id");}
        }

    }

    @Override
    public Skill update(Skill skill) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Skill skillUpdated = em.merge(skill);
                em.getTransaction().commit();
                return skillUpdated;

            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new DAOException("Skill kunne ikke opdateres");
            }
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            try{
                em.getTransaction().begin();
                Skill skillToBeDeleted = em.find(Skill.class, id);
                if (skillToBeDeleted != null) {
                    em.remove(skillToBeDeleted);
                    em.getTransaction().commit();
                    return true;
                } else {
                    return false;
                }
            }catch (Exception e){
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new DAOException("Skill kunne ikke slettes");}
        }
    }

}

package app.daos;

import app.entities.Candidate;
import app.exceptions.DAOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CandidateDAO implements IDAO<Candidate,Integer> {
    private EntityManagerFactory emf;

    public CandidateDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Candidate create(Candidate candidate) {
        try(EntityManager em = emf.createEntityManager()){
            try{
                em.getTransaction().begin();
                em.persist(candidate);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();}
            }
        }
        return candidate;
    }

    @Override
    public List<Candidate> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            try {
                TypedQuery<Candidate> query = em.createQuery("SELECT c FROM Candidate c", Candidate.class);
                return query.getResultList();
            } catch (Exception e) {
                e.printStackTrace(); //No rollback() in read-op
            }
            throw new DAOException("Kunne ikke hente alle gemte candidate");
        }
    }


    @Override
    public Candidate getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            try {//em.getTransaction().begin();
                Candidate candidate = em.find(Candidate.class, id);
                //em.getTransaction().commit();
                return candidate;
            }catch (Exception e){
                e.printStackTrace();
                throw new DAOException("Kunne ikke hente candidate(id: "+id+ ") ud fra id");}
        }

    }

    @Override
    public Candidate update(Candidate candidate) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Candidate updatedCandidate = em.merge(candidate);
                em.getTransaction().commit();
                return updatedCandidate;

            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new DAOException("Candidate kunne ikke opdateres");
            }
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            try{
                em.getTransaction().begin();
                Candidate candidateToBeDeleted = em.find(Candidate.class, id);
                if (candidateToBeDeleted != null) {
                    em.remove(candidateToBeDeleted);
                    em.getTransaction().commit();
                    return true;
                } else {
                    return false;
                }
            }catch (Exception e){
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new DAOException("Candidate kunne ikke slettes");}
        }
    }
}

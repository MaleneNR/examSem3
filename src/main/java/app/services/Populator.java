package app.services;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.daos.SkillDAO;
import app.entities.Candidate;
import app.entities.Category;
import app.entities.Skill;
import app.security.daos.SecurityDAO;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {

    public static Handler populate (){
        return ctx -> {
            EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
            EntityManager em = emf.createEntityManager();
            CandidateDAO candidateDAO = new CandidateDAO(emf);
            SkillDAO skillDAO = new SkillDAO(emf);
            //SecurityDAO securityDAO = new SecurityDAO(emf);

            try {
                em.getTransaction().begin();

//                Role userRole = securityDAO.addRole("USER");   //Gem i DB først
//                Role adminRole = securityDAO.addRole("ADMIN");

                Skill java = new Skill("Java", Category.PROG_LANG, "Solid Java & Spring experience");
                Skill python = new Skill("Python", Category.PROG_LANG, "Data processing, APIs, and ML");
                Skill react = new Skill("React", Category.FRONTEND, "Modern React + TypeScript development");
                Skill docker = new Skill("Docker", Category.DEVOPS, "Containerization & deployment experience");
                Skill sql = new Skill("SQL", Category.DB, "Strong database modelling & query optimization");
                Skill figma = new Skill("Figma", Category.FRONTEND,"Colorful");

                skillDAO.create(java);
                skillDAO.create(python);
                skillDAO.create(react);
                skillDAO.create(docker);
                skillDAO.create(sql);
                skillDAO.create(figma);

                Candidate marie = Candidate.builder()
                        .name("Marie")
                        .phone("12345678")
                        .educationBackground("Computer Science")
                        .build();

                marie.addSkill(react);
                marie.addSkill(java);
                marie.addSkill(docker);
                marie.addSkill(figma);

                Candidate tim = Candidate.builder()
                        .name("Tim")
                        .phone("87654321")
                        .educationBackground("It-Architecture")
                        .build();

                tim.addSkill(java);
                tim.addSkill(python);
                tim.addSkill(docker);


                candidateDAO.create(marie);
                candidateDAO.create(tim);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

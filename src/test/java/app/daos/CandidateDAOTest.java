package app.daos;

import app.config.HibernateConfig;
import app.entities.Candidate;
import app.entities.Category;
import app.entities.Skill;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CandidateDAOTest {
    private static EntityManagerFactory emf;
    private static CandidateDAO candidateDAO;
    private static SkillDAO skillDAO;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        skillDAO = new SkillDAO(emf);
        candidateDAO = new CandidateDAO(emf);
    }


    @BeforeEach
    void setUp() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Candidate").executeUpdate();
            em.createQuery("DELETE FROM Skill").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void create() {
        //Arrange
        Skill sql = new Skill("SQL", Category.DB, "Strong database modelling & query optimization");
        Skill figma = new Skill("Figma", Category.FRONTEND,"Colorful");

        skillDAO.create(sql);
        skillDAO.create(figma);

        Candidate marie = Candidate.builder()
                .name("Marie")
                .phone("12345678")
                .educationBackground("Computer Science")
                .build();
        marie.addSkill(sql);
        marie.addSkill(figma);

        //Act
        Candidate saved = candidateDAO.create(marie);
        Candidate found = candidateDAO.getById(saved.getCandidateId());

        // Assert
        assertNotNull(found);
        assertEquals(saved.getCandidateId(), found.getCandidateId());
        assertEquals("Marie", found.getName());
        assertEquals("Computer Science", found.getEducationBackground());
        assertEquals("12345678", found.getPhone());
        assertEquals(2, found.getSkills().size());
    }

    @Test
    void getAll() {
        //Arrange
        Skill sql = new Skill("SQL", Category.DB, "Strong database modelling & query optimization");
        Skill figma = new Skill("Figma", Category.FRONTEND,"Colorful");

        skillDAO.create(sql);
        skillDAO.create(figma);

        Candidate marie = Candidate.builder()
                .name("Marie")
                .phone("12345678")
                .educationBackground("Computer Science")
                .build();

        marie.addSkill(sql);
        marie.addSkill(figma);

        Candidate tim = Candidate.builder()
                .name("Tim")
                .phone("87654321")
                .educationBackground("It-Architecture")
                .build();

        tim.addSkill(sql);
        tim.addSkill(figma);

        Candidate saved1 = candidateDAO.create(marie);
        Candidate saved2 =candidateDAO.create(tim);
        //Act
        List<Candidate> trips = candidateDAO.getAll();

        assertNotNull(trips);
        //Assert that it contains name of marie and tim
        assertEquals(2, trips.size());

    }

    @Test
    void getById() {
        // Arrange
        Skill docker = new Skill("Docker", Category.DEVOPS, "Containerization experience");
        skillDAO.create(docker);

        Candidate tim = Candidate.builder()
                .name("Tim")
                .phone("87654321")
                .educationBackground("IT-Architecture")
                .build();

        tim.addSkill(docker);
        Candidate saved = candidateDAO.create(tim);

        // Act
        Candidate found = candidateDAO.getById(saved.getCandidateId());

        // Assert
        assertNotNull(found);
        assertEquals(saved.getCandidateId(), found.getCandidateId());
        assertEquals("Tim", found.getName());
        assertEquals("IT-Architecture", found.getEducationBackground());
        assertEquals(1, found.getSkills().size());

    }

    @Test
    void update() {
        // Arrange
        Candidate tim = Candidate.builder()
                .name("Tim")
                .phone("87654321")
                .educationBackground("It-Architecture")
                .build();

        Candidate saved =candidateDAO.create(tim);

        // Act
        saved.setName("Nye tim");
        saved.setEducationBackground("Software Engineering");

        Candidate updated = candidateDAO.update(saved);

        // Assert
        assertNotNull(updated);
        assertEquals("Nye tim", updated.getName());
        assertEquals("Software Engineering", updated.getEducationBackground());
    }

    @Test
    void delete() {
        // Arrange
        Candidate tim = Candidate.builder()
                .name("Tim")
                .phone("87654321")
                .educationBackground("It-Architecture")
                .build();

        Candidate saved =candidateDAO.create(tim);

        // Act
        boolean deleted = candidateDAO.delete(saved.getCandidateId());

        // Assert
        assertTrue(deleted);
    }
}
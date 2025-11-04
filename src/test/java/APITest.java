import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Category;
import app.entities.Skill;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class APITest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final String BASE_URL = "http://localhost:7070/api";
    private static Javalin app;
    private static CandidateDAO candidateDAO;

    private static Candidate candidate1;
    private static Candidate candidate2;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7070);
        candidateDAO = new CandidateDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Candidate").executeUpdate();
            em.createQuery("DELETE FROM Skill").executeUpdate();

            Skill java = Skill.builder()
                    .name("Java")
                    .category(Category.PROG_LANG)
                    .description("Java skills")
                    .build();

            Skill react = Skill.builder()
                    .name("React")
                    .category(Category.FRONTEND)
                    .description("React experience")
                    .build();

            candidate1 = Candidate.builder()
                    .name("Marie")
                    .phone("12345678")
                    .educationBackground("Computer Science")
                    .build();
            candidate1.addSkill(java);
            candidate1.addSkill(react);

            candidate2 = Candidate.builder()
                    .name("Tim")
                    .phone("87654321")
                    .educationBackground("IT-Architecture")
                    .build();

            em.persist(java);
            em.persist(react);
            em.persist(candidate1);
            em.persist(candidate2);
            em.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Candidate").executeUpdate();
            em.createQuery("DELETE FROM Skill").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDownAll() {  //Den bad om static?
        ApplicationConfig.stopServer(app);
    }

    @Test
    void getAllCandidates(){
        List<CandidateDTO> candidates =
                given()
                        .when()
                        .get(BASE_URL + "/candidates")
                        .then()
                        .statusCode(200)
                        .body("size()", greaterThanOrEqualTo(2))
                        .extract()
                        .as(new TypeRef<List<CandidateDTO>>() {});

        assertThat(candidates.size(), is(2));
        assertThat(candidates.get(0).getName(), anyOf(is("Marie"), is("Tim")));
    }

    @Test
    void getCandidateById_shouldReturnCandidateWithSkills() {
        CandidateDTO dto =
                given()
                        .when()
                        .get(BASE_URL + "/candidates/" + candidate1.getCandidateId())
                        .then()
                        .statusCode(200)
                        .body("name", is("Marie"))
                        .body("skills.size()", is(2))
                        .extract()
                        .as(CandidateDTO.class);

        List<SkillDTO> skills = new ArrayList<>(dto.getSkills());
        assertThat(skills.size(), is(2));
        assertThat(
                skills.stream().map(skillDTO -> skillDTO.getName()).toList(),
                hasItems("Java", "React"));
    }

    @Test
    void createCandidate_shouldReturnCreatedCandidate() {
        CandidateDTO newCandidate = CandidateDTO.builder()
                .name("Sofie")
                .phone("12344321")
                .educationBackground("Software Engineering")
                .build();

        CandidateDTO created =
                given()
                        .contentType("application/json")
                        .body(newCandidate)
                        .when()
                        .post(BASE_URL + "/candidates")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(CandidateDTO.class);

        assertThat(created.getName(), is("Sofie"));
    }

    @Test
    void updateCandidate_shouldReturnUpdatedData() {
        CandidateDTO updated =
                given()
                        .contentType("application/json")
                        .body("""
                            {
                              "name": "Marie updated:)",
                              "phone": "888",
                              "educationBackground": "Updated Background"
                            }
                            """)
                        .when()
                        .put(BASE_URL + "/candidates/" + candidate1.getCandidateId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(CandidateDTO.class);

        assertThat(updated.getName(), is("Marie updated:)"));
        assertThat(updated.getPhone(), is("888"));
        assertThat(updated.getEducationBackground(), is("Updated Background"));
    }

    @Test
    void deleteCandidate_shouldReturn200() {
        given()
                .when()
                .delete(BASE_URL + "/candidates/" + candidate2.getCandidateId())
                .then()
                .statusCode(204);
    }
}

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class APITest {

    @Test
    public void testGetPoem(){
//EKSEMPEL:
        RestAssured.baseURI = "http://localhost:7007/api";
        given()
                .when()
                .get("/poem/1")
                .then()
                .statusCode(200)
                .body("poemName", is("The Old Pond"))
                .body("author", is("Matsuo Bashō"))
                .body("poem", is("An old silent pond\n" +
                        "A frog jumps into the pond—\n" +
                        "Splash! Silence again."));

    }
}

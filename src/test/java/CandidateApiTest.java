import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.utils.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CandidateApiTest {
    private static Javalin app;

    @BeforeAll
    static void setup(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        new Populator().createEntities(emf);

        app = ApplicationConfig.startServer(7070);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
        RestAssured.basePath = "/api/v1";
    }

    @AfterAll
    static void stopServer(){
        ApplicationConfig.stopServer(app);
    }

    @Test
    public void getAllCandidates() {
        given()
                .accept("application/json")
                .when()
                .get("/candidates")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void getCandidateById() {
        given()
        .accept("application/json")
                .when()
                .get("/candidates/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void getFakeCandidate(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/candidates/{id}", 999)
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void createCandidate() {
        String candidate = """
                {
                "candidateName": "Alice",
                  "phone": 12345678,
                  "educationBackground": "Master in Computer Science",
                  "skills": [
                    {
                      "id": 3,
                      "skillName": "Test",
                      "category": "TESTING",
                      "description": "Tools and frameworks for testing and QA"
                    }
                    ]
                }
                """;

        given()
                .contentType("application/json")
                .body(candidate)
                .when()
                .post("/candidates")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("candidateName", equalTo("Alice"));

    }

    @Test
    public void updateCandidate() {
        String updatedCandidate = """
                {
                "candidateName": "Alice portland",
                  "phone": 12345678,
                  "educationBackground": "Master in Computer Science",
                  "skills": [
                    {
                      "id": 3,
                      "skillName": "Test",
                      "category": "TESTING",
                      "description": "Tools and frameworks for testing and QA"
                    }
                    ]
                }
                """;

        given()
                .contentType("application/json")
                .body(updatedCandidate)
                .when()
                .put("/candidates/3")
                .then()
                .statusCode(200)
                .body("candidateName", equalTo("Alice portland"));
    }

    @Test
    public void deleteCandidate() {
        given()
        .accept("application/json")
                .when()
                .delete("/candidates/2")
                .then()
                .statusCode(200);
    }
}

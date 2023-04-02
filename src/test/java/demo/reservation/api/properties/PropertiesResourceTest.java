package demo.reservation.api.properties;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PropertiesResourceTest {

    @Test
    public void testRootEndpoint() {
        given()
            .when().get("/root")
            .then()
            .statusCode(200)
            .body(is("Resources available"));
    }
}
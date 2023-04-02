package demo.reservation.api.properties;

import io.quarkus.test.junit.QuarkusIntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusIntegrationTest
public class PropertiesResourceIT extends PropertiesResourceTest {
    public void testRootEndpoint() {
        given()
            .when().get("/root")
            .then()
            .statusCode(200)
            .body(is("Resources available"));
    }
}

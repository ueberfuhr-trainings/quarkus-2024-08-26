package de.samples.schulung.quarkus.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ApiExistingCustomerTests {

  String existingCustomerUri;

  @BeforeEach
  void setUp() {
    existingCustomerUri = given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .when()
      .post("/api/v1/customers")
      .then()
      .statusCode(201)
      .header("Location", is(notNullValue()))
      .extract()
      .header("Location");
  }

  // DELETE /customers/{uuid} -> 204
  @Test
  @DisplayName("[API] DELETE /customers/{uuid} -> 204")
  void given_whenDeleteCustomer_thenReturn204() {
    given()
      .when()
      .delete(existingCustomerUri)
      .then()
      .statusCode(204);
  }

  // DELETE /customers/{uuid} -> 204
  @Test
  @DisplayName("[API] GET /customers/{uuid} -> 200+JSON")
  void given_whenGetCustomer_thenReturn200() {
    given()
      .when()
      .accept(ContentType.JSON)
      .get(existingCustomerUri)
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("name", is(equalTo("John")));
  }

}

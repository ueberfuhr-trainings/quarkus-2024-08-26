package de.samples.schulung.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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

  // GET /customers -> 200 + JSON
  @Test
  @DisplayName("DELETE /customers/{uuid} -> 204")
  void given_whenDeleteCustomer_thenReturn204() {
    given()
      .when()
      .delete(existingCustomerUri)
      .then()
      .statusCode(204);
  }

}

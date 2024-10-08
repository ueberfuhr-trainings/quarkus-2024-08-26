package de.samples.schulung.quarkus.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class ApiNonExistingCustomerTests {

  String nonExistingCustomerUri;

  @BeforeEach
  void setUp() {
    nonExistingCustomerUri = "/api/v1/customers/" + UUID.randomUUID();
    given()
      .when()
      .delete(nonExistingCustomerUri)
      .then()
      .statusCode(is(anything()));
  }

  @Test
  @DisplayName("[API] GET /customers/{uuid} -> 404")
  void given_whenGetCustomer_thenReturn404() {
    given()
      .when()
      .get(nonExistingCustomerUri)
      .then()
      .statusCode(404);
  }

  @Test
  @DisplayName("[API] DELETE /customers/{uuid} -> 404")
  void given_whenDeleteCustomer_thenReturn404() {
    given()
      .when()
      .delete(nonExistingCustomerUri)
      .then()
      .statusCode(404);
  }

}

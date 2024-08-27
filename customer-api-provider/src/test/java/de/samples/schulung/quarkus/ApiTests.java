package de.samples.schulung.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ApiTests {

  // GET /customers -> 200 + JSON
  @Test
  @DisplayName("GET /customers -> 200")
  void given_whenGetCustomers_thenReturn200_andContentTypeJson() {
    // Setup - Test - Assertions
    // Arrange - Act - Assert
    // Given - When - Then
    given()
      .when()
      .accept(ContentType.JSON)
      .get("/api/v1/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body(startsWith("["))
      .body(endsWith("]"));
  }

  @Test
  @DisplayName("POST /customers -> 201")
  void givenOneCustomer_whenPostCustomers_thenReturn200_andLocationHeaderExists() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .when()
      .accept(ContentType.JSON)
      .post("/api/v1/customers")
      .then()
      .statusCode(201)
      .contentType(ContentType.JSON)
      .header("Location", is(notNullValue()))
      .body("name", is(equalTo("John")))
      .body("uuid", is(notNullValue()));
  }

  @Test
  @DisplayName("POST /customers -> Default State")
  void givenOneCustomerWithoutState_whenPostCustomers_thenReturnCustomerWithStateActive() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02"
        }
        """)
      .when()
      .accept(ContentType.JSON)
      .post("/api/v1/customers")
      .then()
      .statusCode(201)
      .body("state", is(equalTo("active")));
  }

}

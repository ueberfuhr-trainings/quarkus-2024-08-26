package de.samples.schulung.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ApiTests {

  // GET /customers -> 200 + JSON
  @Test
  @DisplayName("GET /customers -> 200")
  void given_whenGetCustomers_thenReturn200_andContentTypeJson() {
    // Setup - Test - Assertions
    // Arrange - Act - Assert
    // Given - When - Then
    RestAssured.given()
      .when()
      .accept(ContentType.JSON)
      .get("/api/v1/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body(Matchers.startsWith("["))
      .body(Matchers.endsWith("]"));
  }

  @Test
  @DisplayName("POST /customers -> 201")
  void givenOneCustomer_whenPostCustomers_thenReturn200_andLocationHeaderExists() {
    RestAssured.given()
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
      .body("name", Matchers.is(Matchers.equalTo("John")))
      .body("uuid", Matchers.is(Matchers.notNullValue()));
  }

}

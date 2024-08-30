package de.samples.schulung.quarkus.boundary;

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
  @DisplayName("[API] GET /customers -> 200")
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

  // GET /customers -> 200 + JSON
  @Test
  @DisplayName("[API] GET /customers (invalid state) -> 400")
  void given_whenGetCustomersWithInvalidState_thenReturn400() {
    given()
      .when()
      .get("/api/v1/customers?state=gelbekatze")
      .then()
      .statusCode(400);
  }

  // GET /customers -> 200 + JSON
  @Test
  @DisplayName("[API] GET /customers (XML) -> 406")
  void given_whenGetCustomersAsXml_thenReturn406() {
    // Setup - Test - Assertions
    // Arrange - Act - Assert
    // Given - When - Then
    given()
      .when()
      .accept(ContentType.XML)
      .get("/api/v1/customers")
      .then()
      .statusCode(406);
  }

  @Test
  @DisplayName("[API] POST /customers -> 201")
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
  @DisplayName("[API] POST /customers (XML) -> 415")
  void givenOneCustomerAsXml_whenPostCustomers_thenReturn415() {
    given()
      .contentType(ContentType.XML)
      .body("""
        <customer>
            <name>John</name>
            <birthdate>2004-05-02</birthdate>
            <state>active</state>
        </customer>
        """)
      .when()
      .post("/api/v1/customers")
      .then()
      .statusCode(415);
  }


  @Test
  @DisplayName("[API] POST /customers -> Default State")
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

  @Test
  @DisplayName("[API] POST /customers -> UUID readonly")
  void givenOneCustomerWithUuid_whenPostCustomers_thenReturnGeneratedId() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "active",
          "uuid": "db998f52-1db7-4f66-af42-365d9ac41df2"
        }
        """)
      .when()
      .accept(ContentType.JSON)
      .post("/api/v1/customers")
      .then()
      .statusCode(201)
      .contentType(ContentType.JSON)
      .body("uuid", is(not(equalTo("db998f52-1db7-4f66-af42-365d9ac41df2"))));
  }

  @Test
  @DisplayName("[API] POST /customers -> name must have at least 3 chars")
  void givenOneCustomerWithNameTooShort_whenPostCustomers_thenReturn400() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "J",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .when()
      .post("/api/v1/customers")
      .then()
      .statusCode(400);
  }

  @Test
  @DisplayName("[API] POST /customers -> name must have at most 100 chars")
  void givenOneCustomerWithNameTooLong_whenPostCustomers_thenReturn400() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "J01234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890",
          "birthdate": "2004-05-02",
          "state": "active"
        }
        """)
      .when()
      .post("/api/v1/customers")
      .then()
      .statusCode(400);
  }

  @Test
  @DisplayName("[API] POST /customers -> birthdate is required")
  void givenOneCustomerWithoutBirthdate_whenPostCustomers_thenReturn400() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "state": "active"
        }
        """)
      .when()
      .post("/api/v1/customers")
      .then()
      .statusCode(400);
  }

  @Test
  @DisplayName("[API] POST /customers -> state is enum")
  void givenOneCustomerWithInvalidState_whenPostCustomers_thenReturn400() {
    given()
      .contentType(ContentType.JSON)
      .body("""
        {
          "name": "John",
          "birthdate": "2004-05-02",
          "state": "gelbekatze"
        }
        """)
      .when()
      .post("/api/v1/customers")
      .then()
      .statusCode(400);
  }

}

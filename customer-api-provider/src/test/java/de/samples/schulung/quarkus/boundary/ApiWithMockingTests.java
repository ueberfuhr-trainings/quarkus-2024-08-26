package de.samples.schulung.quarkus.boundary;

import de.samples.schulung.quarkus.domain.Customer;
import de.samples.schulung.quarkus.domain.CustomersService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class ApiWithMockingTests {

  @InjectMock
  CustomersService customerService;


  String existingCustomerUri;
  Customer existingCustomer;

  @BeforeEach
  void setUp() {
    final var uuid = UUID.randomUUID();
    existingCustomer = Customer
      .builder()
      .uuid(uuid)
      .name("John")
      .birthday(LocalDate.of(2004, Month.MAY, 2))
      .build();
    existingCustomerUri = "/api/v1/customers/" + uuid;
  }

  // DELETE /customers/{uuid} -> 204
  @Test
  @DisplayName("[API-only] DELETE /customers/{uuid} -> 204")
  void given_whenDeleteCustomer_thenReturn204() {
    when(customerService.deleteCustomer(existingCustomer.getUuid()))
      .thenReturn(true);
    given()
      .when()
      .delete(existingCustomerUri)
      .then()
      .statusCode(204);
  }

  // DELETE /customers/{uuid} -> 204
  @Test
  @DisplayName("[API-only] GET /customers/{uuid} -> 200+JSON")
  void given_whenGetCustomer_thenReturn200() {
    when(customerService.findCustomerByUuid(existingCustomer.getUuid()))
      .thenReturn(Optional.of(existingCustomer));
    given()
      .when()
      .accept(ContentType.JSON)
      .get(existingCustomerUri)
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("name", is(equalTo("John")));
  }

  @Test
  @DisplayName("[API-only] GET /customers/{uuid} -> 404")
  void given_whenGetCustomer_thenReturn404() {
    when(customerService.findCustomerByUuid(any()))
      .thenReturn(Optional.empty());
    given()
      .when()
      .get("/api/v1/customers/{uuid}", UUID.randomUUID())
      .then()
      .statusCode(404);
  }

  @Test
  @DisplayName("[API-only] POST /customers -> name must have at least 3 chars")
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
    verify(customerService, never()).createCustomer(any());
  }

}

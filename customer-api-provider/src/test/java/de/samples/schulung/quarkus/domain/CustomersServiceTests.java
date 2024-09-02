package de.samples.schulung.quarkus.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
@Tag("Domain")
public class CustomersServiceTests {

  @Inject
  CustomersService customersService;

  @Test
  @DisplayName("create customer -> customer can be read")
  void givenOneCustomer_whenCreateCustomer_thenFindCustomer() {
    var customer = Customer
      .builder()
      .name("John")
      .birthday(LocalDate.of(2000, Month.APRIL, 15))
      .build();
    var count = customersService.getCount();
    customersService.createCustomer(customer);
    assertAll(
      () -> assertThat(customersService.getCount())
        .isEqualTo(count + 1),
      () -> assertThat(customer)
        .extracting(Customer::getUuid)
        .isNotNull()
    );
    assertThat(customersService.findCustomerByUuid(customer.getUuid()))
      .isPresent()
      .contains(customer);
  }

  // validation, and we cannot find it then (count has same size), and it has no uuid

  @Test
  @DisplayName("create customer with invalid name -> exception")
  void givenOneCustomerWithInvalidName_whenCreateCustomer_thenThrowException() {
    var customer = Customer
      .builder()
      .name("J")
      .birthday(LocalDate.of(2000, Month.APRIL, 15))
      .build();
    var count = customersService.getCount();
    assertThatThrownBy(() -> customersService.createCustomer(customer))
      .isInstanceOf(ValidationException.class);
    assertAll(
      () -> assertThat(customersService.getCount())
        .isEqualTo(count),
      () -> assertThat(customer)
        .extracting(Customer::getUuid)
        .isNull()
    );
  }

  @Test
  @DisplayName("create customer that is not an adult -> exception")
  void givenOneCustomerTooYoung_whenCreateCustomer_thenThrowException() {
    var customer = Customer
      .builder()
      .name("John")
      .birthday(LocalDate.now().minusYears(17))
      .build();
    var count = customersService.getCount();
    assertThatThrownBy(() -> customersService.createCustomer(customer))
      .isInstanceOf(ValidationException.class);
    assertAll(
      () -> assertThat(customersService.getCount())
        .isEqualTo(count),
      () -> assertThat(customer)
        .extracting(Customer::getUuid)
        .isNull()
    );
  }


}

package de.samples.schulung.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
public class CustomersServiceTests {

  @Inject
  CustomersService customersService;

  @Test
  @DisplayName("[Domain] create customer -> customer can be read")
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

  private ObjectAssert<Customer> assertThatValidationFailsFor(Customer customer, Consumer<Customer> operation) {
    return assertThatFailsFor(customer, operation, ValidationException.class);
  }

  private ObjectAssert<Customer> assertThatFailsFor(Customer customer, Consumer<Customer> operation, Class<? extends Throwable> exceptionType) {
    var count = customersService.getCount();
    assertThatThrownBy(() -> operation.accept(customer))
      .isInstanceOf(ValidationException.class);
    assertThat(customersService.getCount())
      .isEqualTo(count);
    return assertThat(customer);
  }

  // validation, and we cannot find it then (count has same size), and it has no uuid

  @Test
  @DisplayName("[Domain] create customer with invalid name -> fail")
  void givenOneCustomerWithInvalidName_whenCreateCustomer_thenThrowException() {
    assertThatValidationFailsFor(
      Customer
        .builder()
        .name("J")
        .birthday(LocalDate.of(2000, Month.APRIL, 15))
        .build(),
      customersService::createCustomer
    )
      .extracting(Customer::getUuid)
      .isNull();

  }

  @Test
  @DisplayName("[Domain] create customer that is not an adult -> fail")
  void givenOneCustomerTooYoung_whenCreateCustomer_thenThrowException() {
    assertThatValidationFailsFor(
      Customer
        .builder()
        .name("John")
        .birthday(LocalDate.now().minusYears(17))
        .build(),
      customersService::createCustomer
    ).extracting(Customer::getUuid)
      .isNull();
  }

  @Test
  @DisplayName("[Domain] create customer with uuid -> fail")
  void givenOneCustomerWithUuid_whenCreateCustomer_thenThrowException() {
    assertThatValidationFailsFor(
      Customer
        .builder()
        .uuid(UUID.randomUUID())
        .name("John")
        .birthday(LocalDate.of(2000, Month.APRIL, 15))
        .build(),
      customersService::createCustomer
    );
  }

  @Test
  @DisplayName("[Domain] update customer without uuid -> fail")
  void givenOneCustomerWithoutUuid_whenUpdateCustomer_thenThrowException() {
    assertThatValidationFailsFor(
      Customer
        .builder()
        .name("John")
        .birthday(LocalDate.of(2000, Month.APRIL, 15))
        .build(),
      customersService::updateCustomer
    );
  }

}

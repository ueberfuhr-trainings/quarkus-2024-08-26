package de.samples.schulung.quarkus.persistence;

import de.samples.schulung.quarkus.domain.Customer;
import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.domain.CustomersSink;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestTransaction
@TestProfile(UsePanacheImplementation.class)
@Tag("DB")
public class PersistencePanacheCustomersSinkTests {

  @Inject
  CustomersSink sink;

  @DisplayName("[DB - Panache] Simple Find By State")
  @Test
  void given_whenFindByState_thenResultAvailable() {
    var result = sink.findByState(CustomerState.ACTIVE);
    assertThat(result).isNotNull();
  }

  @DisplayName("[Panache] Create Customer")
  @Test
  @Tag("gelbekatze")
  void given_whenInsert_thenUuidIsGenerated() {
    var customer = Customer
      .builder()
      .name("John")
      .birthday(LocalDate.of(2000, Month.AUGUST, 20))
      .build();
    sink.insert(customer);
    assertThat(customer.getUuid())
      .isNotNull();
  }

}

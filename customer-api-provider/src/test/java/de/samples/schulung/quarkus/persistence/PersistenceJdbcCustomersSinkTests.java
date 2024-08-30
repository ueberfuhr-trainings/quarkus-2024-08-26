package de.samples.schulung.quarkus.persistence;

import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.domain.CustomersSink;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestTransaction
@TestProfile(UseJdbcImplementation.class)
public class PersistenceJdbcCustomersSinkTests {

  @Inject
  CustomersSink sink;

  @DisplayName("[DB - JDBC] Simple Find By State")
  @Test
  void given_whenFindByState_thenResultAvailable() {
    var result = sink.findByState(CustomerState.ACTIVE);
    assertThat(result).isNotNull();
  }

}

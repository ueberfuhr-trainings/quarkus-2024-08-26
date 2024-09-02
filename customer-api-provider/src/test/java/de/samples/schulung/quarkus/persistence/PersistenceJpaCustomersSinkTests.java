package de.samples.schulung.quarkus.persistence;

import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.domain.CustomersSink;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestTransaction
@TestProfile(UseJpaImplementation.class)
@Tag("DB")
public class PersistenceJpaCustomersSinkTests {

  @Inject
  CustomersSink sink;

  @DisplayName("[JPA] Simple Find By State")
  @Test
  void given_whenFindByState_thenResultAvailable() {
    var result = sink.findByState(CustomerState.ACTIVE);
    assertThat(result).isNotNull();
  }

}

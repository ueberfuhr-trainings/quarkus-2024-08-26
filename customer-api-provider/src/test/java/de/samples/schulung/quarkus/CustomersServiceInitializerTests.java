package de.samples.schulung.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@QuarkusTest
@TestProfile(CustomersServiceInitializerTests.ProfileForThisTest.class)
public class CustomersServiceInitializerTests {

  @Inject
  CustomersService customerService;

  public static class ProfileForThisTest implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
      return Map.of(
        "customers.initialization.enabled",
        "true"
      );
    }
  }

  @Test
  @DisplayName("[Domain] Sample customers initialized")
  void given_when_thenSampleCustomersExist() {
    Assertions.assertThat(customerService.getCount())
      .isGreaterThan(0L);
  }

}

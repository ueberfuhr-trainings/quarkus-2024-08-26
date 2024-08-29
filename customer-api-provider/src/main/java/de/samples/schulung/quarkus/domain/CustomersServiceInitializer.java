package de.samples.schulung.quarkus.domain;

import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.Dependent;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@Dependent
@RequiredArgsConstructor
// nur in dev profile
// @IfBuildProfile("dev")
// aktiviert, wenn NICHT prod profile
// @UnlessBuildProfile("prod")
@IfBuildProperty(
  name = "customers.initialization.enabled",
  stringValue = "true"
)
public class CustomersServiceInitializer {

  private final CustomersService customersService;

  @Startup
  public void init() {
    if (customersService.getCount() == 0) {
      customersService.createCustomer(
        Customer
          .builder()
          .name("Tom Mayer")
          .birthday(LocalDate.of(2006, Month.APRIL, 10))
          .build()
      );
      customersService.createCustomer(
        Customer
          .builder()
          .name("Julia Smith")
          .birthday(LocalDate.of(2001, Month.OCTOBER, 20))
          .state(CustomerState.LOCKED)
          .build()
      );
    }
  }

}

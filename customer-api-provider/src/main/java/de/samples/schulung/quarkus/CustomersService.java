package de.samples.schulung.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersService {

  private final Map<UUID, CustomerDto> customers = new HashMap<>();

  {
    var customer1 = new CustomerDto(
      UUID.randomUUID(),
      "Tom Mayer",
      LocalDate.of(2006, Month.APRIL, 10),
      "active"
    );
    customers.put(customer1.getUuid(), customer1);
    var customer2 = new CustomerDto(
      UUID.randomUUID(),
      "Julia Smith",
      LocalDate.of(2010, Month.OCTOBER, 20),
      "locked"
    );
    customers.put(customer2.getUuid(), customer2);
  }

  // Todo: CustomerDto ersetzen

  public Stream<CustomerDto> getCustomers() {
    return customers
      .values()
      .stream();
  }

  public Stream<CustomerDto> findCustomersByState(@NotNull String state) {
    return getCustomers()
      .filter(customer -> customer.getState().equals(state));
  }

  public Optional<CustomerDto> findCustomerByUuid(@NotNull UUID uuid) {
    return Optional.ofNullable(customers.get(uuid));
  }

  public void createCustomer(@NotNull CustomerDto customer) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
  }

  public boolean deleteCustomer(@NotNull UUID uuid) {
    // Eigene Exception werfen?
    return null != customers.remove(uuid);
  }

  public boolean existsCustomer(@NotNull UUID uuid) {
    return customers.containsKey(uuid);
  }

}


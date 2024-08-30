package de.samples.schulung.quarkus.domain;

import de.samples.schulung.quarkus.ValidationGroups;
import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.shared.FireEvent;
import de.samples.schulung.quarkus.shared.LogPerformance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersService {

  private final Map<UUID, Customer> customers = new HashMap<>();

  public long getCount() {
    return customers.size();
  }

  public Stream<Customer> getCustomers() {
    return customers
      .values()
      .stream();
  }

  public Stream<Customer> findCustomersByState(@NotNull CustomerState state) {
    return getCustomers()
      .filter(customer -> customer.getState() == state);
  }

  public Optional<Customer> findCustomerByUuid(@NotNull UUID uuid) {
    return Optional.ofNullable(customers.get(uuid));
  }

  @LogPerformance(Logger.Level.DEBUG)
  @FireEvent(CustomerCreatedEvent.class)
  public void createCustomer(
    @Valid
    @NotNull
    @ConvertGroup(to = ValidationGroups.New.class)
    Customer customer
  ) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
  }

  public void updateCustomer(
    @Valid
    @NotNull
    @ConvertGroup(to = ValidationGroups.Existing.class)
    Customer customer
  ) {
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


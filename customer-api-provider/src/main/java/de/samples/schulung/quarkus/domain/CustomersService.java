package de.samples.schulung.quarkus.domain;

import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.shared.LogPerformance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomersService {

  private final Map<UUID, Customer> customers = new HashMap<>();

  private final Event<CustomerCreatedEvent> eventPublisher;

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
 
  @LogPerformance
  public void createCustomer(@Valid @NotNull Customer customer) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
    eventPublisher.fireAsync(new CustomerCreatedEvent(customer));
  }

  public void updateCustomer(@Valid @NotNull Customer customer) {
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


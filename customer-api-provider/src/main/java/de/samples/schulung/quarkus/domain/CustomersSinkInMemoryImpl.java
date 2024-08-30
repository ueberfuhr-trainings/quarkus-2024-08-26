package de.samples.schulung.quarkus.domain;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@DefaultBean
public class CustomersSinkInMemoryImpl implements CustomersSink {

  private final Map<UUID, Customer> customers = new HashMap<>();

  @Override
  public long count() {
    return customers.size();
  }

  @Override
  public Stream<Customer> findAll() {
    return customers
      .values()
      .stream();
  }

  @Override
  public void insert(Customer customer) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
  }

  @Override
  public void update(Customer customer) {
    customers.put(customer.getUuid(), customer);
  }

  @Override
  public boolean delete(UUID uuid) {
    return null != customers.remove(uuid);
  }
}

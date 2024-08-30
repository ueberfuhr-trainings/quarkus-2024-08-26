package de.samples.schulung.quarkus.domain;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CustomersSink {

  Stream<Customer> findAll();

  default long count() {
    return findAll()
      .count();
  }

  default Stream<Customer> findByState(Customer.CustomerState state) {
    return findAll()
      .filter(customer -> customer.getState() == state);
  }

  default Optional<Customer> findByUuid(UUID uuid) {
    return findAll()
      .filter(customer -> customer.getUuid().equals(uuid))
      .findFirst();
  }

  default boolean existsByUuid(UUID uuid) {
    return findByUuid(uuid)
      .isPresent();
  }

  void insert(Customer customer);

  void update(Customer customer);

  boolean delete(UUID uuid);
}

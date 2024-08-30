package de.samples.schulung.quarkus.persistence;

import de.samples.schulung.quarkus.domain.Customer;
import de.samples.schulung.quarkus.domain.CustomersSink;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@IfBuildProperty(
  name = "persistence.sink.implementation",
  stringValue = "panache"
)
@ApplicationScoped
@Typed(CustomersSink.class)
@RequiredArgsConstructor
public class CustomersSinkPanacheImpl implements CustomersSink {

  private final CustomerEntityRepository repo;
  private final CustomerEntityMapper mapper;

  @Override
  public Stream<Customer> findAll() {
    return repo.findAll()
      .stream()
      .map(mapper::map);
  }

  @Override
  public long count() {
    return repo.count();
  }

  @Override
  public Stream<Customer> findByState(Customer.CustomerState state) {
    return repo.list("state", state)
      .stream()
      .map(mapper::map);
  }

  @Override
  public Optional<Customer> findByUuid(UUID uuid) {
    return repo.findByIdOptional(uuid)
      .map(mapper::map);
  }

  @Override
  public boolean existsByUuid(UUID uuid) {
    return repo.findByIdOptional(uuid)
      .isPresent();
  }

  @Override
  public void insert(Customer customer) {
    var entity = mapper.map(customer);
    repo.persist(entity);
    //customer.setUuid(entity.getUuid());
    mapper.copy(entity, customer);
  }

  @Override
  public void update(Customer customer) {
    var entity = mapper.map(customer);
    repo.persist(entity);
    //customer.setUuid(entity.getUuid());
    mapper.copy(entity, customer);
  }

  @Override
  public boolean delete(UUID uuid) {
    return repo.deleteById(uuid);
  }

}

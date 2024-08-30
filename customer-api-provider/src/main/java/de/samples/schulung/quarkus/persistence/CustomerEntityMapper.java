package de.samples.schulung.quarkus.persistence;

import de.samples.schulung.quarkus.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface CustomerEntityMapper {

  CustomerEntity map(Customer customer);

  Customer map(CustomerEntity customerEntity);

  void copy(CustomerEntity source, @MappingTarget Customer target);

}

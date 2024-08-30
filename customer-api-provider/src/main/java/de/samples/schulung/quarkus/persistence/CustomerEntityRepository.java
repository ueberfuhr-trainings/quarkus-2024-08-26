package de.samples.schulung.quarkus.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CustomerEntityRepository implements PanacheRepositoryBase<CustomerEntity, UUID> {

}

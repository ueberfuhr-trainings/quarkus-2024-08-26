package de.samples.schulung.quarkus.domain;

import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.shared.FireEvent;
import de.samples.schulung.quarkus.shared.LogPerformance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomersService {

    private final CustomersSink sink;

    public long getCount() {
        return sink.count();
    }

    public Stream<Customer> getCustomers() {
        return sink.findAll();
    }

    public Stream<Customer> findCustomersByState(@NotNull CustomerState state) {
        return sink.findByState(state);
    }

    public Optional<Customer> findCustomerByUuid(@NotNull UUID uuid) {
        return sink.findByUuid(uuid);
    }

    @LogPerformance(Logger.Level.DEBUG)
    @FireEvent(CustomerCreatedEvent.class)
    public void createCustomer(@Valid @NotNull Customer customer) {
        sink.insert(customer);
    }

    public void updateCustomer(@Valid @NotNull Customer customer) {
        sink.update(customer);
    }

    public boolean deleteCustomer(@NotNull UUID uuid) {
        return sink.delete(uuid);
    }

    public boolean existsCustomer(@NotNull UUID uuid) {
        return sink.existsByUuid(uuid);
    }

}


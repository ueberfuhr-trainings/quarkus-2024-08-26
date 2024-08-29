package de.samples.schulung.quarkus;

import de.samples.schulung.quarkus.Customer.CustomerState;
import de.samples.schulung.quarkus.ValidationGroups.Existing;
import de.samples.schulung.quarkus.ValidationGroups.New;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersService {

    private final Map<UUID, Customer> customers = new HashMap<>();

    {
        this.createCustomer(
                Customer
                        .builder()
                        .name("Tom Mayer")
                        .birthday(LocalDate.of(2006, Month.APRIL, 10))
                        .build()
        );

        this.createCustomer(
                Customer
                        .builder()
                        .name("Julia Smith")
                        .birthday(LocalDate.of(2010, Month.OCTOBER, 20))
                        .state(CustomerState.LOCKED)
                        .build()
        );
    }

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

    public void createCustomer(
            @Valid
            @NotNull
            @ConvertGroup(to = New.class)
            Customer customer
    ) {
        customer.setUuid(UUID.randomUUID());
        customers.put(customer.getUuid(), customer);
    }

    public void updateCustomer(
            @Valid
            @NotNull
            @ConvertGroup(to = Existing.class)
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


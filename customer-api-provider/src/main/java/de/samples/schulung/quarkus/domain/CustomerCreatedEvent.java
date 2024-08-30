package de.samples.schulung.quarkus.domain;

public record CustomerCreatedEvent(
        Customer customer
) {
}

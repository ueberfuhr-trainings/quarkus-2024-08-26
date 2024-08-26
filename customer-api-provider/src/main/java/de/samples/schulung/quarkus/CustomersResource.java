package de.samples.schulung.quarkus;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Path("/api/v1/customers")
public class CustomersResource {

    // GET /customers?state=disabled -> 200 mit Liste der Kunden (JSON)

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<CustomerDto> getCustomers(@QueryParam("state") String state) {
        return Stream.of(
              new CustomerDto(
                      UUID.randomUUID(),
                      "Tom Mayer",
                      LocalDate.of(2006, Month.APRIL, 10),
                      "active"
              ),
                new CustomerDto(
                        UUID.randomUUID(),
                        "Julia Smith",
                        LocalDate.of(2010, Month.OCTOBER, 20),
                        "locked"
                )
        )
                .filter(c -> null == state || c.getState().equals(state))
                .toList();
    }

    // POST /customers mit Kunde ohne UUID -> 201 mit Kunde + Location Header

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(CustomerDto customer) {
        customer.setUuid(UUID.randomUUID());
        return Response
                // TODO
                .created(URI.create("http://localhost:8080/api/v1/customers/" + customer.getUuid()))
                .entity(customer)
                .build();
    }

}

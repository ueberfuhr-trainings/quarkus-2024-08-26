package de.samples.schulung.quarkus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("/api/v1/customers")
public class CustomersResource {

  // GET /customers?state=disabled -> 200 mit Liste der Kunden (JSON)

  private final Map<UUID, CustomerDto> customers = new HashMap<>();

  {
    var customer1 = new CustomerDto(
      UUID.randomUUID(),
      "Tom Mayer",
      LocalDate.of(2006, Month.APRIL, 10),
      "active"
    );
    customers.put(customer1.getUuid(), customer1);
    var customer2 = new CustomerDto(
      UUID.randomUUID(),
      "Julia Smith",
      LocalDate.of(2010, Month.OCTOBER, 20),
      "locked"
    );
    customers.put(customer2.getUuid(), customer2);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<CustomerDto> getCustomers(
    @QueryParam("state")
    @Pattern(regexp = "active|locked|disabled")
    String state
  ) {
    return customers
      .values()
      .stream()
      .filter(c -> null == state || c.getState().equals(state))
      .toList();
  }

  // GET /customers/{id} -> 200 mit Customer

  @GET
  @Path("/{uuid}")
  @Produces(MediaType.APPLICATION_JSON)
  public CustomerDto findCustomerById(@PathParam("uuid") UUID uuid) {
    final var customer = customers.get(uuid);
    if (null == customer) {
      throw new NotFoundException();
    }
    return customer;
  }

  // POST /customers mit Kunde ohne UUID -> 201 mit Kunde + Location Header

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createCustomer(@Valid CustomerDto customer) {
    //assert null == customer.getUuid();
    customer.setUuid(UUID.randomUUID());
    if(null == customer.getState()) {
      customer.setState("active");
    }
    customers.put(customer.getUuid(), customer);
    final var location = UriBuilder
      .fromResource(CustomersResource.class)
      .path(CustomersResource.class, "findCustomerById")
      .build(customer.getUuid());
    /* Alternatively, add a parameter "@Context UriInfo info" and use it like this:
     *
     * info
     *   .getAbsolutePathBuilder()
     *   .path(customer.getUuid().toString())
     *   .build();
     */
    return Response
      .created(location)
      .entity(customer)
      .build();
  }

  @DELETE
  @Path("/{uuid}")
  public void deleteCustomer(@PathParam("uuid") UUID uuid) {
    final var deletedCustomer = customers.remove(uuid);
    if (null == deletedCustomer) {
      throw new NotFoundException();
    }
  }

}

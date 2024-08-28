package de.samples.schulung.quarkus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Path("/api/v1/customers")
@RequiredArgsConstructor
public class CustomersResource {

  private final CustomersService service;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<CustomerDto> getCustomers(
    @QueryParam("state")
    @Pattern(regexp = "active|locked|disabled")
    String state
  ) {
    return (
      null == state
        ? service.getCustomers()
        : service.findCustomersByState(state)
    ).toList();
  }

  // GET /customers/{id} -> 200 mit Customer

  @GET
  @Path("/{uuid}")
  @Produces(MediaType.APPLICATION_JSON)
  public CustomerDto findCustomerById(@PathParam("uuid") UUID uuid) {
    return service
      .findCustomerByUuid(uuid)
      .orElseThrow(NotFoundException::new);
  }

  // POST /customers mit Kunde ohne UUID -> 201 mit Kunde + Location Header

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createCustomer(@Valid CustomerDto customer) {
    //assert null == customer.getUuid();
    if(null == customer.getState()) {
      customer.setState("active");
    }
    service.createCustomer(customer);
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
    if (!service.deleteCustomer(uuid)) {
      throw new NotFoundException();
    }
  }

}

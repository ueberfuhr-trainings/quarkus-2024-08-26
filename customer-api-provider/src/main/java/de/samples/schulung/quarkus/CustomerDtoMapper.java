package de.samples.schulung.quarkus;

// TODO: MapStruct

import de.samples.schulung.quarkus.Customer.CustomerState;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class CustomerDtoMapper {

  public CustomerDto map(Customer customer) {
    if(null == customer) {
      return null;
    }
    var result = new CustomerDto();
    result.setName(customer.getName());
    result.setUuid(customer.getUuid());
    result.setBirthday(customer.getBirthday());
    result.setState(mapState(customer.getState()));
    return result;
  }

  public Customer map(CustomerDto dto) {
    if(null == dto) {
      return null;
    }
    return Customer
      .builder()
      .name(dto.getName())
      .uuid(dto.getUuid())
      .birthday(dto.getBirthday())
      .state(mapState(dto.getState()))
      .build();
  }

  public String mapState(CustomerState state) {
    if(null == state) {
      return null;
    }
    return switch (state) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

  public CustomerState mapState(String state) {
    if(null == state) {
      return null;
    }
    return switch (state) {
      case "active" -> CustomerState.ACTIVE;
      case "locked" -> CustomerState.LOCKED;
      case "disabled" -> CustomerState.DISABLED;
      default -> throw new ValidationException("Unknown state: " + state);
    };
  }

}

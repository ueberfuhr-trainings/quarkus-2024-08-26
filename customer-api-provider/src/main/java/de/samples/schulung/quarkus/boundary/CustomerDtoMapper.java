package de.samples.schulung.quarkus.boundary;

import de.samples.schulung.quarkus.domain.Customer;
import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import jakarta.validation.ValidationException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface CustomerDtoMapper {

  CustomerDto map(Customer customer);

  Customer map(CustomerDto dto);

  default String mapState(CustomerState state) {
    if (null == state) {
      return null;
    }
    return switch (state) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

  default CustomerState mapState(String state) {
    if (null == state) {
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

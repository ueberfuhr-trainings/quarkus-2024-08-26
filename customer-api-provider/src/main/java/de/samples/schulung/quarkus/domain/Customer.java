package de.samples.schulung.quarkus.domain;

import de.samples.schulung.quarkus.shared.Adult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Customer {

  public enum CustomerState {

    ACTIVE, LOCKED, DISABLED

  }

  // TODO: wenn neu -> Null, wenn update -> NotNull
  // @Null
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @NotNull
  @Adult
  private LocalDate birthday;
  @NotNull
  @Builder.Default
  private CustomerState state = CustomerState.ACTIVE;

}

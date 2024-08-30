package de.samples.schulung.quarkus.domain;

import de.samples.schulung.quarkus.ValidationGroups.Existing;
import de.samples.schulung.quarkus.ValidationGroups.New;
import de.samples.schulung.quarkus.shared.Adult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
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
  @Null(groups = New.class)
  @NotNull(groups = Existing.class)
  private UUID uuid;
  @Size(min = 3, max = 100, groups = {New.class, Existing.class, Default.class})
  @NotNull(groups = {New.class, Existing.class, Default.class})
  private String name;
  @NotNull(groups = {New.class, Existing.class, Default.class})
  @Adult(groups = {New.class, Existing.class, Default.class})
  private LocalDate birthday;
  @NotNull(groups = {New.class, Existing.class, Default.class})
  @Builder.Default
  private CustomerState state = CustomerState.ACTIVE;

}

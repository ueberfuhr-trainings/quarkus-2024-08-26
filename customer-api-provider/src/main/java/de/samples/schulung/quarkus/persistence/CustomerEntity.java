package de.samples.schulung.quarkus.persistence;

import de.samples.schulung.quarkus.domain.Customer.CustomerState;
import de.samples.schulung.quarkus.shared.Adult;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer")
@Table(name = "CUSTOMERS")
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @NotNull
  @Adult
  @Column(name = "BIRTH_DATE")
  private LocalDate birthday;
  @NotNull
  //@Enumerated(EnumType.STRING)
  private CustomerState state = CustomerState.ACTIVE;

}

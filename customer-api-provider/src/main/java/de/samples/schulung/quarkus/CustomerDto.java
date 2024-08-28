package de.samples.schulung.quarkus;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

  @Setter(onMethod_ = @JsonbTransient)
  @Null
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @JsonbProperty("birthdate")
  @NotNull
  private LocalDate birthday;
  @Pattern(regexp = "active|locked|disabled")
  private String state;

}

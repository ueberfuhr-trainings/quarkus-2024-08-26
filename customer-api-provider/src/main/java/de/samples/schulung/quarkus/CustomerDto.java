package de.samples.schulung.quarkus;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
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
  private UUID uuid;
  private String name;
  @JsonbProperty("birthdate")
  private LocalDate birthday;
  private String state;

}

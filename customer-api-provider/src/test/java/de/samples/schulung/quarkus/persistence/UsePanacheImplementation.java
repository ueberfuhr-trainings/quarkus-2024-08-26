package de.samples.schulung.quarkus.persistence;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class UsePanacheImplementation implements QuarkusTestProfile {

  @Override
  public Map<String, String> getConfigOverrides() {
    return Map.of(
      "persistence.sink.implementation", "panache"
    );
  }
}

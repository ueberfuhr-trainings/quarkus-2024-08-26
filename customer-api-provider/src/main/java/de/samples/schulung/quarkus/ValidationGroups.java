package de.samples.schulung.quarkus;

import jakarta.validation.groups.Default;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationGroups {

  // Validation Group
  public interface Incoming {}

  public final Class<?>[] ALWAYS = {
    Default.class,
    Incoming.class
  };

  // TODO does not work
  public static final Class<?>[] ONLY_INCOMING = {
    Incoming.class
  };

}

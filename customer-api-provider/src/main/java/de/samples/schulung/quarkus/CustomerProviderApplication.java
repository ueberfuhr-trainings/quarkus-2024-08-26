package de.samples.schulung.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class CustomerProviderApplication {

  public static void main(String[] args) {
    Quarkus.run(args);
  }

}

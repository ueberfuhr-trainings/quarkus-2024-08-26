package de.samples.schulung.quarkus.infrastructure;

import de.samples.schulung.quarkus.domain.CustomerCreatedEvent;
import io.quarkus.arc.log.LoggerName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CustomerEventLogger {

  @LoggerName("customers-service")
  Logger logger;

  void onCustomerCreated(@ObservesAsync CustomerCreatedEvent event) {
    logger.info("Created new customer " + event.customer().getUuid()); // Datenschutz! ;-)

  }

}

package de.samples.schulung.quarkus.infrastructure;

import de.samples.schulung.quarkus.domain.Customer;
import de.samples.schulung.quarkus.domain.CustomersService;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
class CustomersLoggingTests {

  /*
   * Requirement:
   *  - when a customer is created, a logging should occur (Logger with name "customers")
   * Hints:
   *  - we mock the JBoss logger here to verify the logging
   *  - this is error-prone, because
   *     - we overwrite the Quarkus-internal @DefaultBean (see io.quarkus.arc.runtime.LoggerProducer)
   *     - we verify the invocation of the special logger method (instead of verifying any logging)
   *  - it would be better instead to encapsulate Customer Events Logging within a custom bean
   *    and mock this bean during the test
   */

  @Inject
  CustomersService service;
  @LoggerName("customers-service")
  Logger log;

  @DisplayName("[INFRA] Customer created -> Logging")
  @Test
  void shouldLogWhenCustomerCreated() throws InterruptedException {
    reset(log); // already logged during startup (initialization)!

    // we invoke the logging asynchronously -> we join with a count down latch
    final var lock = new CountDownLatch(1);
    // count down when the info() method is invoked
    doAnswer(invocation -> {
      lock.countDown();
      return null;
    })
      .when(log).info(anyString());

    var customer = Customer.builder()
      .name("Tom")
      .birthday(LocalDate.of(2000, Month.FEBRUARY, 2))
      .build();
    service.createCustomer(customer);

    // wait until logging is done asynchronously
    assertThat(lock.await(1, TimeUnit.SECONDS))
      .describedAs("Asynchronous logging did not occur within 1 second")
      .isTrue(); // did it count back to 0?

    verify(log).info(anyString());

  }

  // just for this class, not for all tests!
  @ApplicationScoped
  static class LoggerMocksProducer {
    private final Map<String, Logger> loggers = new HashMap<>();

    private Optional<LoggerName> findLoggerName(InjectionPoint injectionPoint) {
      return injectionPoint.getQualifiers()
        .stream()
        .filter(q -> q.annotationType().equals(LoggerName.class))
        .findFirst()
        .map(LoggerName.class::cast);
    }

    private Logger createLogger(String name) {
      return Mockito.mock(Logger.class);
    }

    @Produces
    @Dependent
    @LoggerName("")
    Logger getMockedLogger(InjectionPoint injectionPoint) {
      return findLoggerName(injectionPoint)
        .map(LoggerName::value)
        .map(name -> loggers.computeIfAbsent(name, this::createLogger))
        .orElseThrow(() -> new IllegalStateException("Unable to derive the logger name at " + injectionPoint));
    }

    @PreDestroy
    void clear() {
      loggers.clear();
    }

  }

}

package de.samples.schulung.quarkus.utilities;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.test.junit.QuarkusTestProfile;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.jboss.logging.Logger;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProfileWithMockedLogger implements QuarkusTestProfile {

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

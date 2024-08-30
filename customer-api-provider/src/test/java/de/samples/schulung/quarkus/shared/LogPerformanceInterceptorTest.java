package de.samples.schulung.quarkus.shared;

import de.samples.schulung.quarkus.utilities.ProfileWithMockedLogger;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verifyNoInteractions;

@QuarkusTest
@TestProfile(ProfileWithMockedLogger.class)
public class LogPerformanceInterceptorTest {

  @Inject
  SampleBean sampleBean;
  @LoggerName("performance")
  Logger logger;

  @BeforeEach
  void setup() {
    Mockito.reset(logger);
  }

  @Test
  @DisplayName("[SHARED] No @LogPerformance -> no logging")
  void given_whenNoAnnotation_thenDoNotLog() {
    sampleBean.dontLog();
    verifyNoInteractions(logger);
  }

  @Test
  @DisplayName("[SHARED] @LogPerformance -> default level logging")
  void given_whenAnnotation_thenLogDefaultLevel() {
    sampleBean.logDefaultLevel();
    Mockito.verify(logger).logf(
      eq(Logger.Level.INFO),
      anyString(),
      any(Object[].class)
    );
  }

  @Test
  @DisplayName("[SHARED] @LogPerformance(DEBUG) -> debug logging")
  void given_whenAnnotationWithDebugLevel_thenLogDebugLevel() {
    sampleBean.logDebugLevel();
    Mockito.verify(logger).logf(
      eq(Logger.Level.DEBUG),
      anyString(),
      any(Object[].class)
    );
  }


  // Initialized for all tests, but does not disturb
  @ApplicationScoped
  static class SampleBean {

    void dontLog() {
    }

    @LogPerformance
    void logDefaultLevel() {
    }

    @LogPerformance(Logger.Level.DEBUG)
    void logDebugLevel() {
    }

  }

}

package de.samples.schulung.quarkus.shared;

import io.quarkus.arc.log.LoggerName;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

@Interceptor
@LogPerformance
@Priority(10)
public class LogPerformanceInterceptor {

  @LoggerName("performance")
  Logger logger;

  private Logger.Level findLevel(InvocationContext ic) {
    return AnnotationUtils
      .findAnnotation(ic.getMethod(), LogPerformance.class)
      .map(LogPerformance::value)
      .orElse(Logger.Level.INFO);
  }

  @AroundInvoke
  public Object logPerformance(InvocationContext invocationContext) throws Exception {
    final var methodName = invocationContext.getMethod().getName();
    final var level = findLevel(invocationContext);
    var ts1 = System.currentTimeMillis();
    try {
      return invocationContext.proceed(); // Weiterleitung an das Original
    } finally {
      var ts2 = System.currentTimeMillis();
      logger.logf(
        level,
        "Dauer der Methode '%s': %d ms",
        new Object[]{methodName, ts2 - ts1}
      );
    }
  }

}

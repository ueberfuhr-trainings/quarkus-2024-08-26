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

  @AroundInvoke
  public Object logPerformance(InvocationContext invocationContext) throws Exception {
    var ts1 = System.currentTimeMillis();
    try {
      return invocationContext.proceed(); // Weiterleitung an das Original
    } finally {
      var ts2 = System.currentTimeMillis();
      logger.info("Dauer der Methode '" + invocationContext.getMethod().getName() + "': " + (ts2 - ts1) + "ms");
    }
  }

}

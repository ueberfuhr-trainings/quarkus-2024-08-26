package de.samples.schulung.quarkus.shared;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import org.jboss.logging.Logger;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Documented
@Inherited
@InterceptorBinding
public @interface LogPerformance {

    @Nonbinding
    Logger.Level value() default Logger.Level.INFO;

}

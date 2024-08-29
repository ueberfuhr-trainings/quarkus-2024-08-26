package de.samples.schulung.quarkus.shared;

import jakarta.interceptor.InterceptorBinding;

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

    // TODO Level konfigurierbar?

}

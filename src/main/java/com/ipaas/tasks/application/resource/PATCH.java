package com.ipaas.tasks.application.resource;

import jakarta.ws.rs.HttpMethod;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Suporte para método HTTP PATCH no JAX-RS.
 */
@Target({METHOD})
@Retention(RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {
}

package com.amd.simplehttpserver.annotation;

import com.amd.simplehttpserver.handler.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestMethod {
  public String path() default "";
  public HttpMethod httpMethod() default HttpMethod.GET;
}

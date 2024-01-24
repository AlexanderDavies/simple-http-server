package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.annotation.RestController;
import com.amd.simplehttpserver.annotation.RestMethod;
import com.amd.simplehttpserver.handler.HttpRequest;

import java.lang.reflect.Method;

public class Route {

  private final Object controller;
  private final Method method;

  protected <T> Route(T controller, Method method) {
    if (!controller.getClass().isAnnotationPresent(RestController.class)) {
      throw new IllegalArgumentException("Route class must be annotated with @RestController");
    }

    if (!method.isAnnotationPresent(RestMethod.class)) {
      throw new IllegalArgumentException("Route class must be annotated with @RestMethod");
    }

    this.controller = controller;
    this.method = method;
  }

  public <T> Object execute(HttpRequest<T> request) throws Exception {
    return invoke(request);
  }

  private Object invoke(Object... args) throws Exception {
    // need to handle passing the body etc so you don't get illegal argument exception for too many parameters
    Object result =  method.invoke(controller);
    Class<?> returnType = method.getReturnType();
    return returnType.cast(result);
  }

}

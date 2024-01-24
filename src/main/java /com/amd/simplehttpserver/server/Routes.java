package com.amd.simplehttpserver.server;

import com.amd.simplehttpserver.annotation.RestController;
import com.amd.simplehttpserver.annotation.RestMethod;
import com.amd.simplehttpserver.handler.HttpMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Routes {
  Map<String, Route> routes = new HashMap<>();

  protected Routes() {
  }

  public void register(Object controller) {

    Class<?> clazz = controller.getClass();

    if (!clazz.isAnnotationPresent(RestController.class)) {
      throw new IllegalArgumentException("Route method must be annotated with @RestController");
    }

    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(RestMethod.class)) {

        HttpMethod httpMethod = method.getAnnotation(RestMethod.class).httpMethod();
        String resourcePath = controller.getClass().getAnnotation(RestController.class).path();
        String methodPath = method.getAnnotation(RestMethod.class).path();

        String path = httpMethod.toString() + resourcePath + methodPath;
        Route route = new Route(controller, method);

        routes.put(path, route);
      }
    }

  }

  @SuppressWarnings("unchecked")
  public Route getRoute(String path, HttpMethod httpMethod) throws IllegalArgumentException {
    Route route = routes.get(httpMethod + path);

    if (Objects.isNull(route)) {
      throw new IllegalArgumentException("Route not found: " + httpMethod + path);
    }

    return route;
  }

}

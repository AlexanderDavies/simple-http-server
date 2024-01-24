package com.amd.simplehttpserver;

import com.amd.simplehttpserver.annotation.RestController;
import com.amd.simplehttpserver.annotation.RestMethod;

@RestController(path="/test")
public class TestController {

@RestMethod(path = "/hello", httpMethod = com.amd.simplehttpserver.handler.HttpMethod.GET)
  public String hello() {
    return "Hello, World!";
  }

/*  @RestMethod(path = "/greet", httpMethod = com.amd.simplehttpserver.handler.HttpMethod.POST)
  public String greet(String name) {
    return "Hello, " + name + "!";
  }

  @RestMethod(path = "/echo", httpMethod = com.amd.simplehttpserver.handler.HttpMethod.PUT)
  public String echo(String message) {
    return "Echo: " + message;
  }*/
}

package org.acme.opentelemetry.lambda;

public class Request {
  private String id;

  public Request() {
  }

  public Request(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

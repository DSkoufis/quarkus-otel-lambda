package org.acme.opentelemetry.lambda;

import java.util.Map;

public class Request {
  private Map<String, String> val;

  public Request() {
  }

  public Request(Map<String, String> val) {
    this.val = val;
  }

  public Map<String, String> getVal() {
    return val;
  }

  public void setVal(Map<String, String> val) {
    this.val = val;
  }

  @Override
  public String toString() {
    return "Request{" +
        "val=" + val +
        '}';
  }
}

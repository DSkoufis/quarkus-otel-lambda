package org.acme.opentelemetry.lambda.http;

import java.util.Map;

public class GetResponse {
  public Map<String, String> args;
  public String method;

  @Override
  public String toString() {
    return "GetResponse{" +
        "args=" + args +
        ", method='" + method + '\'' +
        '}';
  }
}

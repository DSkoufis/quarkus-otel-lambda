package com.test;

public record LambdaResponse(boolean success) {
  static final LambdaResponse SUCCESS = new LambdaResponse(Boolean.TRUE);
}

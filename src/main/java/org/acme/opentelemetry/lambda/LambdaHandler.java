package org.acme.opentelemetry.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("otelHandler")
@ApplicationScoped
public class LambdaHandler implements RequestHandler<Request, Response> {

  @Inject
  LambdaService service;

  @Override
  @WithSpan
  public Response handleRequest(Request request, Context context) {
    service.sameSpan(request.getId());
    service.startDifferentSpan("test arg", "test 2");
    return new Response("ok");
  }
}

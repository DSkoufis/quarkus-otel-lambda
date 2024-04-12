package org.acme.opentelemetry.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import org.acme.opentelemetry.lambda.otel.AbstractHandler;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("otelHandler")
public class LambdaHandler extends AbstractHandler<Request, Response> {

  private final LambdaService service;

  @Inject
  public LambdaHandler(OpenTelemetry openTelemetry, LambdaService service) {
    super(openTelemetry);
    this.service = service;
  }

  @Override
  @WithSpan
  public Response doHandleRequest(Request request, Context context) {
    service.sameSpan(context.getAwsRequestId());

    service.startDifferentSpan(context.getAwsRequestId(), "test 2");

    return new Response("ok");
  }
}

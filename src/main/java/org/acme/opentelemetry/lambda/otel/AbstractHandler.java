package org.acme.opentelemetry.lambda.otel;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.awslambdacore.v1_0.AwsLambdaRequest;
import io.opentelemetry.instrumentation.awslambdacore.v1_0.internal.AwsLambdaFunctionInstrumenter;
import io.opentelemetry.instrumentation.awslambdacore.v1_0.internal.AwsLambdaFunctionInstrumenterFactory;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.semconv.ResourceAttributes;

public abstract class AbstractHandler<I, O> implements RequestHandler<I, O> {

  private final OpenTelemetry openTelemetry;
  private final AwsLambdaFunctionInstrumenter instrumenter;

  private static final AtomicBoolean isColdStart = new AtomicBoolean(true);

  public AbstractHandler(OpenTelemetry openTelemetry) {
    this.openTelemetry = openTelemetry;
    this.instrumenter = AwsLambdaFunctionInstrumenterFactory.createInstrumenter(openTelemetry);
  }

  protected abstract O doHandleRequest(I input, Context context);

  @Override
  public O handleRequest(I input, Context context) {
    AwsLambdaRequest request = AwsLambdaRequest.create(context, input, Collections.emptyMap());
    io.opentelemetry.context.Context parentOtelContext = instrumenter.extract(request);

    if (!instrumenter.shouldStart(parentOtelContext, request)) {
      return doHandleRequest(input, context);
    }

    io.opentelemetry.context.Context otelContext = instrumenter.start(parentOtelContext, request);

    Throwable error = null;
    O output = null;

    try (Scope ignored = otelContext.makeCurrent()) {
      additionalSpanAttributes(request);

      output = doHandleRequest(input, context);
      return output;

    } catch (Throwable t) {
      error = t;
      throw t;
    } finally {
      instrumenter.end(otelContext, request, output, error);

      if (openTelemetry instanceof OpenTelemetrySdk sdk) {
        CompletableResultCode completableResultCode = sdk.getSdkTracerProvider().forceFlush();
        completableResultCode.join(10, TimeUnit.SECONDS);
      }
    }
  }

  private void additionalSpanAttributes(AwsLambdaRequest request) {
    Span currentSpan = Span.current()
        .setAttribute(ResourceAttributes.CLOUD_RESOURCE_ID, request.getAwsContext().getInvokedFunctionArn());

    if (isColdStart.getAndSet(false)) {
      currentSpan.setAttribute(OTelConstants.IS_COLD_START, true);
    }
  }
}

package org.acme.opentelemetry.lambda;

import org.jboss.logging.Logger;

import io.opentelemetry.instrumentation.annotations.AddingSpanAttributes;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LambdaService {

  private static final Logger LOG = Logger.getLogger(LambdaService.class);

  @AddingSpanAttributes
  public void handleRequest(@SpanAttribute(value = "custom.arg") String requestId) {
    LOG.infof("Request ID: %s", requestId);
  }

  @WithSpan("addDelay-my-custom-span-test")
  public void addDelay() {
    LOG.infof("Here");
  }
}

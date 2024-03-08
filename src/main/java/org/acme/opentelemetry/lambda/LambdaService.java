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
  public void sameSpan(@SpanAttribute(value = "custom.arg") String requestId) {
    LOG.infof("Request ID: %s", requestId);
    LOG.info(System.getenv());
    LOG.info(System.getProperties());
  }

  @WithSpan
  public String startDifferentSpan(@SpanAttribute(value = "custom.arg") String requestId,
      @SpanAttribute(value = "test2") String test2) {
    LOG.info("Here");
    return "result";
  }
}

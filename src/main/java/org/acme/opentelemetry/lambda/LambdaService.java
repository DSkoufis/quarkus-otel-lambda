package org.acme.opentelemetry.lambda;

import org.acme.opentelemetry.lambda.http.GetResponse;
import org.acme.opentelemetry.lambda.http.MyRestService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.opentelemetry.instrumentation.annotations.AddingSpanAttributes;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LambdaService {

  private static final Logger LOG = Logger.getLogger(LambdaService.class);

  @Inject
  @RestClient
  MyRestService restService;

  @AddingSpanAttributes
  public void sameSpan(@SpanAttribute(value = "custom.arg") String requestId) {
    LOG.infof("Request ID: %s", requestId);

    LOG.debug(System.getenv());
    LOG.infov("Trace header: {0}", System.getProperty("com.amazonaws.xray.traceHeader"));
  }

  @WithSpan
  public void startDifferentSpan(@SpanAttribute(value = "custom.arg.1") String requestId,
      @SpanAttribute(value = "custom.arg.2") String test2) {
    GetResponse getResponse = restService.testGetRequest(requestId);
    LOG.infov("Get response: {0}", getResponse);
  }
}

package org.acme.opentelemetry.lambda;

import org.acme.opentelemetry.lambda.spans.SubsegmentAttribute;
import org.acme.opentelemetry.lambda.spans.WithCurrentSubsegment;
import org.acme.opentelemetry.lambda.spans.WithXraySubsegment;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LambdaService {

  private static final Logger LOG = Logger.getLogger(LambdaService.class);

  @WithCurrentSubsegment
  public void sameSpan(@SubsegmentAttribute(value = "custom.arg") String requestId) {
    LOG.infof("Request ID: %s", requestId);
  }

  @WithXraySubsegment
  public String startDifferentSpan(@SubsegmentAttribute(value = "custom.arg") String requestId,
      @SubsegmentAttribute(value = "test2") String test2) {
    LOG.info("Here");
    return "result";
  }
}

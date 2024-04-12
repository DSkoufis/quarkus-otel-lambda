package org.acme.opentelemetry.lambda.otel;

import io.opentelemetry.sdk.resources.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class LambdaResourceProducer {

  /**
   * Provides useful resource tags from the FAAS context, i.e lambda name, ARN, etc
   */
  @Produces
  @ApplicationScoped
  public Resource lambdaResource() {
    return LambdaResource.get();
  }

}

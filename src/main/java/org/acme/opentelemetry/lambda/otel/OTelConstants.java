package org.acme.opentelemetry.lambda.otel;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.semconv.ResourceAttributes;

class OTelConstants {
  /**
   * Custom environment variable which is used to set the {@link ResourceAttributes#CLOUD_ACCOUNT_ID} resource attribute
   */
  static final String ENV_ACCOUNT_ID = "ACCOUNT_ID";

  /**
   * Custom environment variable which is used to set the {@link ResourceAttributes#DEPLOYMENT_ENVIRONMENT} resource attribute
   */
  static final String ENV_ENVIRONMENT = "ENVIRONMENT";

  /**
   * Custom environment variable which is used to set the {@link ResourceAttributes#HOST_ARCH} resource attribute
   */
  static final String ENV_ARCHITECTURE = "ARCH";

  /**
   * Custom attribute which should appear only if the function was executed for the first time, aka "cold start"
   */
  static final AttributeKey<Boolean> IS_COLD_START = AttributeKey.booleanKey("aws.lambda.cold_start");
}

package org.acme.opentelemetry.lambda.otel;

import java.util.Map;
import java.util.stream.Stream;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.ResourceAttributes;

public final class LambdaResource {

  /**
   * Creates a {@link Resource} item which provides information about the AWS Lambda function.
   * <p>
   * Ref: <a href="https://opentelemetry.io/docs/specs/semconv/attributes-registry">OTel Attributes Registry</a>
   */
  public static Resource get() {
    return buildResource(System.getenv());
  }

  private static Resource buildResource(Map<String, String> env) {
    String functionName = env.getOrDefault("AWS_LAMBDA_FUNCTION_NAME", "");
    String functionVersion = env.getOrDefault("AWS_LAMBDA_FUNCTION_VERSION", "");

    if (!isLambda(functionName, functionVersion)) {
      return Resource.empty();
    }

    AttributesBuilder builder = Attributes.builder()
        .put(ResourceAttributes.CLOUD_PROVIDER, ResourceAttributes.CloudProviderValues.AWS)
        .put(ResourceAttributes.CLOUD_PLATFORM, ResourceAttributes.CloudPlatformValues.AWS_LAMBDA);

    if (!functionName.isEmpty()) {
      builder.put(ResourceAttributes.FAAS_NAME, functionName);
    }
    if (!functionVersion.isEmpty()) {
      builder.put(ResourceAttributes.FAAS_VERSION, functionVersion);
    }

    String region = env.getOrDefault("AWS_REGION", "");
    if (!region.isEmpty()) {
      builder.put(ResourceAttributes.CLOUD_REGION, region);
    }

    String logStreamName = env.getOrDefault("AWS_LAMBDA_LOG_STREAM_NAME", "");
    if (!logStreamName.isEmpty()) {
      builder.put(ResourceAttributes.FAAS_INSTANCE, logStreamName);
    }

    String logGroupName = env.getOrDefault("AWS_LAMBDA_LOG_GROUP_NAME", "");
    if (!logGroupName.isEmpty()) {
      builder.put(ResourceAttributes.AWS_LOG_GROUP_NAMES, logGroupName);
    }

    int maxMemory = Integer.parseInt(env.getOrDefault("AWS_LAMBDA_FUNCTION_MEMORY_SIZE", "0"));
    if (maxMemory != 0) {
      builder.put(ResourceAttributes.FAAS_MAX_MEMORY, maxMemory * 1_048_576);
    }

    String accountId = env.getOrDefault(OTelConstants.ENV_ACCOUNT_ID, ""); // optional custom env variable
    if (!accountId.isEmpty()) {
      builder.put(ResourceAttributes.CLOUD_ACCOUNT_ID, accountId);
    }

    String environment = env.getOrDefault(OTelConstants.ENV_ENVIRONMENT, ""); // optional custom env variable
    if (!environment.isEmpty()) {
      builder.put(ResourceAttributes.DEPLOYMENT_ENVIRONMENT, environment);
    }

    String arch = env.getOrDefault(OTelConstants.ENV_ARCHITECTURE, ""); // optional custom env variable
    if (!arch.isEmpty()) {
      builder.put(ResourceAttributes.HOST_ARCH, arch);
    }

    return Resource.create(builder.build(), ResourceAttributes.SCHEMA_URL);
  }

  private static boolean isLambda(String... envVariables) {
    return Stream.of(envVariables).anyMatch(v -> !v.isEmpty());
  }

  private LambdaResource() {
  }
}

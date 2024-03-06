resource "aws_lambda_function" "native_lambda_function" {
  function_name = local.lambda_name
  description   = local.lambda_desc
  role          = aws_iam_role.native_lambda_iam_role.arn

  filename         = var.lambda_file_path
  source_code_hash = filebase64sha256(var.lambda_file_path)

  runtime = "provided.al2"
#  runtime = "java17"
#  handler = "io.opentelemetry.instrumentation.awslambdacore.v1_0.TracingRequestStreamWrapper::handleRequest"
  handler = "blah.blah.blah"
#  handler = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"

  architectures = [var.lambda_architecture]

  memory_size = var.lambda_memory_size
  timeout     = var.lambda_timeout

  layers = [
#    "arn:aws:lambda:${var.aws_region}:901920570463:layer:aws-otel-java-wrapper-${var.lambda_architecture}-ver-1-32-0:1"
    "arn:aws:lambda:${var.aws_region}:901920570463:layer:aws-otel-java-agent-${var.lambda_architecture}-ver-1-32-0:1"
  ]

  tracing_config {
    mode = "Active"
  }

  environment {
    variables = {
#      DISABLE_SIGNAL_HANDLERS = true
      AWS_LAMBDA_EXEC_WRAPPER = "/opt/otel-stream-handler"
      QUARKUS_BANNER_ENABLED  = false
#      OTEL_INSTRUMENTATION_AWS_LAMBDA_HANDLER = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"
#      OTEL_INSTRUMENTATION_AWS_LAMBDA_FLUSH_TIMEOUT = 100
#      OPENTELEMETRY_COLLECTOR_CONFIG_FILE = "/var/task/collector.yaml"
    }
  }

  vpc_config {
    subnet_ids         = local.subnets
    security_group_ids = [local.security_groups]
  }

  depends_on = [
    aws_cloudwatch_log_group.lambda_logging
  ]
}

resource "aws_cloudwatch_log_group" "lambda_logging" {
  name              = "/aws/lambda/${local.lambda_name}"
  retention_in_days = var.lambda_logs_retention
  skip_destroy      = false
}

resource "aws_lambda_function" "lambda" {
  function_name    = local.lambda_name
  filename         = "../target/function.zip"
  role             = aws_iam_role.lambda_role.arn
  handler          = "not.used.in.provided.runtime"
  runtime          = "provided.al2"
  source_code_hash = filebase64sha256("../target/function.zip")
  architectures    = [var.lambda_arch]
  depends_on       = [aws_cloudwatch_log_group.lambda]
  timeout          = 300

  layers = [
    "arn:aws:lambda:${var.aws_region}:901920570463:layer:aws-otel-java-agent-${var.lambda_arch}-ver-1-32-0:1"
  ]

  tracing_config {
    mode = "Active"
  }

  environment {
    variables = {
      DISABLE_SIGNAL_HANDLERS = true
      QUARKUS_BANNER_ENABLED  = false
      QUARKUS_PROFILE         = "nonprod"
      ENVIRONMENT             = "nonprod"
      ARCH                    = var.lambda_arch
      ACCOUNT_ID              = local.account_id
    }
  }

  vpc_config {
    security_group_ids = local.security_group_ids
    subnet_ids         = local.subnets
  }
}

resource "aws_cloudwatch_log_group" "lambda" {
  name              = "/aws/lambda/${local.lambda_name}"
  retention_in_days = 1
  skip_destroy      = false
}

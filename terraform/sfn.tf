resource "aws_sfn_state_machine" "sfn_state_machine" {
  name     = local.sfn_name
  role_arn = aws_iam_role.sfn_iam_role.arn

  definition = templatefile("${path.module}/step_function.tftpl", {
    lambda_name = aws_lambda_function.native_lambda_function.arn
  })

  type = "STANDARD"

  logging_configuration {
    level           = "ALL"
    log_destination = "${aws_cloudwatch_log_group.sfn_logging.arn}:*"
  }

  tracing_configuration {
    enabled = "true"
  }
}

resource "aws_cloudwatch_log_group" "sfn_logging" {
  name              = "/aws/vendedlogs/states/${local.sfn_name}"
  retention_in_days = var.lambda_logs_retention
  skip_destroy      = false
}

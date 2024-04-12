resource "aws_sfn_state_machine" "sfn_state_machine" {
  name     = local.step_function_name
  role_arn = aws_iam_role.sfn_iam_role.arn

  definition = templatefile("${path.module}/step_function.tftpl", {
    lambda_name = aws_lambda_function.lambda.arn
  })

  type = "STANDARD"

  tracing_configuration {
    enabled = "true"
  }

  logging_configuration {
    level           = "ALL"
    log_destination = "${aws_cloudwatch_log_group.step_function.arn}:*"
  }
}

resource "aws_cloudwatch_log_group" "step_function" {
  name              = "/aws/vendedlogs/states/${local.step_function_name}"
  retention_in_days = 1
  skip_destroy      = false
}

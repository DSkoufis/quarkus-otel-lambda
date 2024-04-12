locals {
  # Complete with your own values

  #  lambda_name          = "otel-test"
  #  iam_lambda_role_name = "otel-test"
  #  step_function_name   = "otel-test"
  #  iam_sfn_role_name    = "otel-test"
  #
  #  account_id = ""
  #
  #  subnets            = ["subnet-1", "subnet-2", "subnet-3"]
  #  security_group_ids = ["sg-1"]
}

variable "aws_region" {}
variable "lambda_arch" {
  default = "arm64"
}

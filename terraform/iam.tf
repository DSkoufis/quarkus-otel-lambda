locals {
  lambda_role_name = lower(
    format("%s.%s.%s.%s.lambda", var.product_code_tag, var.product_name, var.aws_region, var.environment_tag)
  )
  sfn_role_name = lower(
    format("%s.%s.%s.%s.sfn", var.product_code_tag, var.product_name, var.aws_region, var.environment_tag)
  )
}

resource "aws_iam_role" "native_lambda_iam_role" {
  name               = local.lambda_role_name
  assume_role_policy = data.aws_iam_policy_document.lambda_assume_role.json
}

data "aws_iam_policy_document" "lambda_assume_role" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      identifiers = ["lambda.amazonaws.com"]
      type        = "Service"
    }
  }
}

resource "aws_iam_role_policy_attachment" "AWSLambdaBasicExecutionRole" {
  role       = aws_iam_role.native_lambda_iam_role.name
  policy_arn = data.aws_iam_policy.AWSLambdaBasicExecutionRole.arn
}

resource "aws_iam_role_policy_attachment" "AWSLambdaVPCAccessExecutionRole" {
  role       = aws_iam_role.native_lambda_iam_role.name
  policy_arn = data.aws_iam_policy.AWSLambdaVPCAccessExecutionRole.arn
}

data "aws_iam_policy" "AWSLambdaBasicExecutionRole" {
  arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

data "aws_iam_policy" "AWSLambdaVPCAccessExecutionRole" {
  arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
}

resource "aws_iam_role_policy" "xray_policy" {
  name = "${module.naming.aws_iam_role_policy}.xray"
  role = aws_iam_role.native_lambda_iam_role.id

  policy = data.aws_iam_policy_document.xray_policy_doc.json
}

data "aws_iam_policy_document" "xray_policy_doc" {
  statement {
    sid     = "0"
    effect  = "Allow"
    actions = [
      "xray:PutTraceSegments",
      "xray:PutTelemetryRecords"
    ]
    resources = [
      "*"
    ]
  }
}

resource "aws_iam_role" "sfn_iam_role" {
  name               = local.sfn_role_name
  assume_role_policy = data.aws_iam_policy_document.sfn_assume_role.json
}

data "aws_iam_policy_document" "sfn_assume_role" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      identifiers = ["states.amazonaws.com"]
      type        = "Service"
    }
  }
}

resource "aws_iam_role_policy" "step_function_policy" {
  name = "${module.naming.aws_iam_role_policy}.sfn"
  role = aws_iam_role.sfn_iam_role.id

  policy = data.aws_iam_policy_document.step_function_policy.json
}

data "aws_iam_policy_document" "step_function_policy" {
  statement {
    effect  = "Allow"
    actions = [
      "lambda:InvokeFunction"
    ]
    resources = [
      aws_lambda_function.native_lambda_function.arn
    ]
  }

  statement {
    effect  = "Allow"
    actions = [
      "logs:CreateLogDelivery",
      "logs:CreateLogStream",
      "logs:GetLogDelivery",
      "logs:UpdateLogDelivery",
      "logs:DeleteLogDelivery",
      "logs:ListLogDeliveries",
      "logs:PutLogEvents",
      "logs:PutResourcePolicy",
      "logs:DescribeResourcePolicies",
      "logs:DescribeLogGroups"
    ]
    resources = ["*"]
  }
}

resource "aws_iam_role_policy" "sfn_xray_policy" {
  name = "${module.naming.aws_iam_role_policy}.sfnxray"
  role = aws_iam_role.sfn_iam_role.id

  policy = data.aws_iam_policy_document.sfn_xray.json
}

data "aws_iam_policy_document" "sfn_xray" {
  statement {
    effect  = "Allow"
    actions = [
      "xray:PutTraceSegments",
      "xray:PutTelemetryRecords",
      "xray:GetSamplingRules",
      "xray:GetSamplingTargets"
    ]
    resources = ["*"]
  }
}

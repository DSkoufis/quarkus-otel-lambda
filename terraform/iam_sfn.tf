data "aws_iam_policy_document" "sfn_assume_policy" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      identifiers = ["states.amazonaws.com"]
      type        = "Service"
    }
  }
}

resource "aws_iam_role" "sfn_iam_role" {
  name               = local.iam_sfn_role_name
  assume_role_policy = data.aws_iam_policy_document.sfn_assume_policy.json
}

resource "aws_iam_role_policy" "sfn_xray_policy" {
  name = "${local.iam_sfn_role_name}.xray"
  role = aws_iam_role.sfn_iam_role.id

  policy = data.aws_iam_policy_document.xray_policy_doc.json
}

resource "aws_iam_role_policy" "step_function_policy" {
  name   = local.iam_sfn_role_name
  role   = aws_iam_role.sfn_iam_role.id
  policy = data.aws_iam_policy_document.step_function_policy.json
}

data "aws_iam_policy_document" "step_function_policy" {
  statement {
    effect  = "Allow"
    actions = [
      "lambda:InvokeFunction"
    ]
    resources = [
      aws_lambda_function.lambda.arn
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

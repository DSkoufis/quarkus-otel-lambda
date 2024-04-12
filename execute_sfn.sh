#!/bin/zsh

SFN_ARN=$1
PROFILE=$2
REGION=$3

aws stepfunctions start-execution \
  --state-machine-arn "$SFN_ARN" \
  --input "{\"key\":\"value\"}" \
  --profile "$PROFILE" --region "$REGION" --no-cli-pager

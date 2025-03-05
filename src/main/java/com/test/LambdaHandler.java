package com.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KafkaEvent;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named("lambdaHandler")
public class LambdaHandler implements RequestHandler<KafkaEvent, LambdaResponse> {

  @Inject
  StepFunctionService stepFunctionService;

  @Override
  public LambdaResponse handleRequest(KafkaEvent input, Context context) {
    stepFunctionService.forwardMessages(List.of());

    return LambdaResponse.SUCCESS;
  }
}

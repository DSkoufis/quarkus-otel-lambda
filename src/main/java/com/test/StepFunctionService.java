package com.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sfn.SfnClient;

@Slf4j
@ApplicationScoped
public class StepFunctionService {

  private static final int MAX_PAYLOAD = 255 * 1024;

  @ConfigProperty(name = "aws.stepfunction.arn")
  String sfnArn;
  @Inject
  ObjectMapper objectMapper;
  @Inject
  SfnClient sfnClient;

  @SneakyThrows
  public void forwardMessages(List<String> products) {
    String json = objectMapper.writeValueAsString(products);

    if (json.getBytes(StandardCharsets.UTF_8).length > MAX_PAYLOAD) {
      splitAndSubmit(products);
      return;
    }

    sfnClient.startExecution(b -> b.stateMachineArn(sfnArn).input(json));
  }

  private void splitAndSubmit(List<String> products) {
    int size = products.size();

    if (size == 1) {
      throw new IllegalStateException();
    }

    List<String> firstHalf = products.subList(0, size / 2);
    forwardMessages(firstHalf);

    List<String> secondHalf = products.subList(size / 2, size);
    forwardMessages(secondHalf);
  }
}

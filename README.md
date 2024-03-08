The Quarkus app makes use of the [`quarkus-amazon-lambda`](https://quarkus.io/guides/aws-lambda) extension.

The handler is: [`LambdaHandler`](src/main/java/org/acme/opentelemetry/lambda/LambdaHandler.java)

A terraform module is provided to build the lambda, although many variables are kept private as they are using private resources/modules. It should be
easy to reconfigure and deploy the example. All missing variables are set as tf `locals`

i.e:
```txt
- security_groups (lambda sg - vpc config)
- subnets (lambda subnets - vpc config)

Resource names, can be set to anything:
- lambda_desc (lambda description)
- lambda_name
- sfn_name
- sfn_role_name
- lambda_role_name
- sfn_policy_name
- sfn_xray_policy_name
- xray_policy_name
```

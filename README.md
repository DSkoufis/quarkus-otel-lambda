The Quarkus app makes use of the [`quarkus-amazon-lambda`](https://quarkus.io/guides/aws-lambda) extension and tests the OTel support.

The handler is: [`LambdaHandler`](src/main/java/org/acme/opentelemetry/lambda/LambdaHandler.java)

To build the app (in macOS):
```shell
mvn clean package -Dnative -DskipTests \
  -Dquarkus.native.container-build=true \
  -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17
```
q

package org.acme.opentelemetry.lambda.http;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "httpbin")
@Consumes(MediaType.APPLICATION_JSON)
public interface MyRestService {

  @GET
  GetResponse testGetRequest(@QueryParam("id") String requestId);
}

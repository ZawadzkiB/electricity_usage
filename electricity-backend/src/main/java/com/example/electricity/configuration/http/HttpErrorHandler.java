package com.example.electricity.configuration.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class HttpErrorHandler extends DefaultResponseErrorHandler {
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    log.error("Error: {} with status {}", response.getStatusText(), response.getStatusCode());
    if (response.getStatusCode().isError()) {
      throw new HttpFailedException();
    }
  }
}

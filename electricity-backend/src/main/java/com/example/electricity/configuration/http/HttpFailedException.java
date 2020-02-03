package com.example.electricity.configuration.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class HttpFailedException extends RuntimeException {

  public HttpFailedException() {
    super("error making call to another server");
  }
}

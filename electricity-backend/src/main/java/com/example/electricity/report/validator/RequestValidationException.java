package com.example.electricity.report.validator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequestValidationException extends RuntimeException {
  RequestValidationException(String message) {
    super(message);
  }
}

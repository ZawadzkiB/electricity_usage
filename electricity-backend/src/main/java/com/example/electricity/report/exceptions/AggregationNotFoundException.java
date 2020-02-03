package com.example.electricity.report.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AggregationNotFoundException extends RuntimeException {

  public AggregationNotFoundException() {
    super("Aggregation not found");
  }
}

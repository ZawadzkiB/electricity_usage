package com.example.electricity.report.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class Validator<T> {

  private T t;
  private List<ValidateError> errors = new ArrayList<>();

  Validator<T> set(T t) {
    this.t = t;
    return this;
  }

  public abstract void validate(T t, List<ValidateError> errors);

  public void validate() {
    validate(t, errors);
    if (errors.isEmpty()) {
      log.info("Validation successful");
    } else {
      log.info("Validation error: {}", errors);
      throw new RequestValidationException(errors.stream()
              .map(ValidateError::toString)
              .collect(Collectors.joining(", ")));
    }
  }

}

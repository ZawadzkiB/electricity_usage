package com.example.electricity.report.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ValidateError {
  private String field;
  private String message;
}

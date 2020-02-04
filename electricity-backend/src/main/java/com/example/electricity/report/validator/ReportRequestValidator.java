package com.example.electricity.report.validator;


import com.example.electricity.report.model.ReportRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

@Component()
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportRequestValidator extends Validator<ReportRequest> {

  @Override
  public void validate(ReportRequest reportRequest, List<ValidateError> errors) {
    if (Optional.ofNullable(reportRequest.getPrice()).isEmpty()) {
      errors.add(new ValidateError("price", "price can't be empty"));
    }
  }
}

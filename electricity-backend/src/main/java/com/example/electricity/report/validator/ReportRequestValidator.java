package com.example.electricity.report.validator;


import com.example.electricity.report.model.ReportRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

@Component()
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportRequestValidator extends Validator<ReportRequest> {

  @Override
  public void validate(ReportRequest reportRequest, List<ValidateError> errors) {
    if (reportRequest.getStartDate().isAfter(reportRequest.getEndDate())) {
      errors.add(new ValidateError("endDate", "startDate after endDate"));
    }
    if (reportRequest.getStartDate().isBefore(LocalDate.now().minusYears(2))) {
      errors.add(new ValidateError("startDate", "start date more than 2 years"));
    }
    if (reportRequest.getEndDate().isAfter(LocalDate.now())) {
      errors.add(new ValidateError("endDate", "end date can't be from future"));
    }
  }
}

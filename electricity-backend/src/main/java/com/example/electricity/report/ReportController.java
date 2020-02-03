package com.example.electricity.report;

import com.example.electricity.report.model.Report;
import com.example.electricity.report.model.ReportRequest;
import com.example.electricity.report.validator.RequestValidatorProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

  static final String DATE_FORMAT = "yyyy-MM-dd";
  private final ReportCreator reportCreator;
  private final RequestValidatorProvider requestValidatorProvider;

  public ReportController(ReportCreator reportCreator,
                          RequestValidatorProvider requestValidatorProvider) {
    this.reportCreator = reportCreator;
    this.requestValidatorProvider = requestValidatorProvider;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Report energyReport(@RequestParam(name = "price", defaultValue = "0") BigDecimal price) {

    ReportRequest reportRequest = new ReportRequest()
            .setPrice(Optional.ofNullable(price).orElse(BigDecimal.ZERO));

    log.info("Report requested data: {}", reportRequest);
    requestValidatorProvider.provideValidator(reportRequest).validate();
    return reportCreator.createReport(reportRequest);
  }

}

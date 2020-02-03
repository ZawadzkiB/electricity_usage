package com.example.electricity.report;

import com.example.electricity.report.aggregations.AggregationType;
import com.example.electricity.report.model.Report;
import com.example.electricity.report.model.ReportRequest;
import com.example.electricity.report.validator.RequestValidatorProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
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
  public Report energyReport(@RequestParam(name = "startDate", defaultValue = "") @DateTimeFormat(pattern = DATE_FORMAT) LocalDate startDate,
                             @RequestParam(name = "endDate", defaultValue = "") @DateTimeFormat(pattern = DATE_FORMAT) LocalDate endDate,
                             @RequestParam(name = "aggregation", defaultValue = "MONTHLY") AggregationType aggregationType,
                             @RequestParam(name = "price", defaultValue = "0") BigDecimal price) {

    ReportRequest reportRequest = new ReportRequest()
            .setStartDate(Optional.ofNullable(startDate).orElseGet(() -> LocalDate.now().minusYears(2)))
            .setEndDate(Optional.ofNullable(endDate).orElseGet(() -> LocalDate.now().minusDays(1)))
            .setAggregationType(aggregationType)
            .setPrice(Optional.ofNullable(price).orElse(BigDecimal.ZERO));

    log.info("Report requested data: {}", reportRequest);
    requestValidatorProvider.provideValidator(reportRequest).validate();
    return reportCreator.createReport(reportRequest);
  }

}

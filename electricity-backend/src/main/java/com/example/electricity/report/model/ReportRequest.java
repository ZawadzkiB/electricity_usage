package com.example.electricity.report.model;

import com.example.electricity.report.aggregations.AggregationType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ReportRequest {

  private LocalDate startDate;
  private LocalDate endDate;
  private AggregationType aggregationType;
  private BigDecimal price;
}

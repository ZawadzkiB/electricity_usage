package com.example.electricity.report.model;

import com.example.electricity.report.aggregations.AggregationType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Report {

  private String documentIdentification;
  private LocalDateTime documentDateTime;
  private String accountingPoint;
  private String measurementUnit;
  private Map<AggregationType, List<Consumption>> consumptionHistory;
}

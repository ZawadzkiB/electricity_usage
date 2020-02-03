package com.example.electricity.report.aggregations.functions;

import com.example.electricity.report.model.Consumption;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AggregateFunction {
  List<Consumption> apply(Map<LocalDateTime, List<Consumption>> groupedConsumptions);
}

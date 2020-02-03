package com.example.electricity.report.aggregations;

import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.model.Consumption;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class MonthlyAggregation implements Aggregates {

  @Override
  public List<Consumption> aggregate(List<Consumption> consumptions, AggregateFunction aggregateFunction) {
    Map<LocalDateTime, List<Consumption>> aggregatedMap = consumptions.stream()
            .collect(groupingBy(consumption -> consumption.getTimeStamp().toLocalDate()
                    .with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay()));

    return aggregateFunction.apply(aggregatedMap);
  }

  @Override
  public AggregationType type() {
    return AggregationType.MONTHLY;
  }

}

package com.example.electricity.report.aggregations.functions;

import com.example.electricity.report.model.Consumption;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component("average")
class AverageFunction implements AggregateFunction {

  @Override
  public List<Consumption> apply(Map<LocalDateTime, List<Consumption>> groupedConsumptions) {
    List<Consumption> aggregated = new ArrayList<>();
    groupedConsumptions.forEach((LocalDateTime group, List<Consumption> list) ->
            list.stream()
                    .mapToDouble(Consumption::getConsumption)
                    .average()
                    .ifPresent(average -> aggregated.add(new Consumption().setConsumption(average).setTimeStamp(group))));
    aggregated.sort(Comparator.comparing(Consumption::getTimeStamp));
    return aggregated;
  }

}

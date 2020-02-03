package com.example.electricity.report.aggregations;

import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.model.Consumption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class WeeklyAggregation implements Aggregates {

  @Value("${locale.language.tag}")
  private String localeLanguageTag;

  @Override
  public List<Consumption> aggregate(List<Consumption> consumptions, AggregateFunction aggregateFunction) {
    WeekFields weekFields = WeekFields.of(Locale.forLanguageTag(localeLanguageTag));
    Map<LocalDateTime, List<Consumption>> aggregatedMap = consumptions.stream()
            .collect(groupingBy(consumption ->
                            LocalDate.now().withYear(consumption.getTimeStamp().getYear())
                                    .with(weekFields.weekOfYear(), consumption.getTimeStamp().get(weekFields.weekOfYear()))
                                    .with(weekFields.dayOfWeek(), 1)
                                    .atStartOfDay()
                    )
            );

    return aggregateFunction.apply(aggregatedMap);
  }

  @Override
  public AggregationType type() {
    return AggregationType.WEEKLY;
  }
}

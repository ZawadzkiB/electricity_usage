package com.example.electricity.report.aggregations;

import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.model.Consumption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DailyAggregationTest {

  private static final String GROUP_NOT_FOUND = "Consumption group not found";
  private static final List<Consumption> consumptions = List.of(
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 23).atStartOfDay()).setConsumption(2.00),

          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().plusHours(1)).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().plusHours(2)).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().plusHours(3)).setConsumption(4.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().plusHours(4)).setConsumption(4.00),

          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 25).atStartOfDay().plusHours(1)).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 25).atStartOfDay().plusHours(1)).setConsumption(3.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 25).atStartOfDay().plusHours(2)).setConsumption(4.00)
  );
  @Qualifier("average")
  @Autowired
  AggregateFunction aggregateAverageFunction;
  @Qualifier("sum")
  @Autowired
  AggregateFunction aggregateSumFunction;
  @Autowired
  DailyAggregation dailyAggregation;

  @Test
  void dailyAggregationNumberOfGroups() {
    List<Consumption> dailyAggregated = dailyAggregation.aggregate(consumptions, aggregateAverageFunction);
    assertThat(dailyAggregated.size()).isEqualTo(3);
  }

  @Test
  void dailyAggregationConsumptionCreatedForSum() {
    List<Consumption> dailyAggregated = dailyAggregation.aggregate(consumptions, aggregateSumFunction);

    assertThat(dailyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 23).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 23).atStartOfDay()).setConsumption(2.00));

    assertThat(dailyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 24).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay()).setConsumption(12.00));

    assertThat(dailyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 25).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 25).atStartOfDay()).setConsumption(9.00));
  }

  @Test
  void dailyAggregationConsumptionCreatedForAverage() {
    List<Consumption> dailyAggregated = dailyAggregation.aggregate(consumptions, aggregateAverageFunction);

    assertThat(dailyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 23).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 23).atStartOfDay()).setConsumption(2.00));

    assertThat(dailyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 24).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay()).setConsumption(3.00));

    assertThat(dailyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 25).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 25).atStartOfDay()).setConsumption(3.00));
  }

  @Test
  void dailyAggregationConsumptionListIsEmpty() {
    List<Consumption> dailyAggregated = dailyAggregation.aggregate(new ArrayList<>(), aggregateAverageFunction);
    assertThat(dailyAggregated).isEmpty();
  }

}
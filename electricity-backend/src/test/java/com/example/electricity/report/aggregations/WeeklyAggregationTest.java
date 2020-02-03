package com.example.electricity.report.aggregations;

import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.model.Consumption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class WeeklyAggregationTest {

  private static final String GROUP_NOT_FOUND = "Consumption group not found";
  private static final List<Consumption> consumptions = List.of(
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 23).atStartOfDay()).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().minusHours(1)).setConsumption(2.00),

          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay()).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().plusHours(1)).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay().plusHours(2)).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 25).atStartOfDay()).setConsumption(2.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 26).atStartOfDay()).setConsumption(3.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 27).atStartOfDay()).setConsumption(4.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 28).atStartOfDay()).setConsumption(4.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 29).atStartOfDay()).setConsumption(4.00),
          new Consumption().setTimeStamp(LocalDate.of(2019, 6, 30).atStartOfDay()).setConsumption(4.00),

          new Consumption().setTimeStamp(LocalDate.of(2019, 7, 1).atStartOfDay()).setConsumption(5.00),

          new Consumption().setTimeStamp(LocalDate.of(2018, 6, 24).atStartOfDay()).setConsumption(3.00),

          new Consumption().setTimeStamp(LocalDate.of(2018, 6, 25).atStartOfDay()).setConsumption(4.00),
          new Consumption().setTimeStamp(LocalDate.of(2018, 6, 26).atStartOfDay()).setConsumption(5.00)
  );

  @Qualifier("average")
  @Autowired
  AggregateFunction aggregateAverageFunction;
  @Qualifier("sum")
  @Autowired
  AggregateFunction aggregateSumFunction;
  @Autowired
  WeeklyAggregation weeklyAggregation;

  @Test
  void weeklyAggregationNumberOfGroups() {
    List<Consumption> weeklyAggregated = weeklyAggregation.aggregate(consumptions, aggregateAverageFunction);
    assertThat(weeklyAggregated.size()).isEqualTo(5);
  }

  @Test
  void weeklyAggregationConsumptionCreatedForAverage() {
    List<Consumption> weeklyAggregated = weeklyAggregation.aggregate(consumptions, aggregateAverageFunction);

    assertThat(weeklyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 24).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay()).setConsumption(3.00));

    assertThat(weeklyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 17).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 17).atStartOfDay()).setConsumption(2.00));

    assertThat(weeklyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 7, 1).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 7, 1).atStartOfDay()).setConsumption(5.00));
  }

  @Test
  void weeklyAggregationConsumptionCreatedForSum() {
    List<Consumption> weeklyAggregated = weeklyAggregation.aggregate(consumptions, aggregateSumFunction);

    assertThat(weeklyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 24).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 24).atStartOfDay()).setConsumption(27.00));

    assertThat(weeklyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 6, 17).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 6, 17).atStartOfDay()).setConsumption(4.00));

    assertThat(weeklyAggregated.stream().filter(consumption -> consumption.getTimeStamp().isEqual(LocalDate.of(2019, 7, 1).atStartOfDay()))
            .findFirst().orElseThrow(() -> new AssertionError(GROUP_NOT_FOUND)))
            .isEqualTo(new Consumption().setTimeStamp(LocalDate.of(2019, 7, 1).atStartOfDay()).setConsumption(5.00));
  }

  @Test
  void weeklyAggregationConsumptionListIsEmpty() {
    List<Consumption> weeklyAggregated = weeklyAggregation.aggregate(new ArrayList<>(), aggregateAverageFunction);
    assertThat(weeklyAggregated).isEmpty();
  }
}
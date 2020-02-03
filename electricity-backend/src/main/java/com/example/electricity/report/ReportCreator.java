package com.example.electricity.report;

import com.example.electricity.provider.ElectricityUsageProvider;
import com.example.electricity.provider.model.ElectricityUsageData;
import com.example.electricity.report.aggregations.Aggregates;
import com.example.electricity.report.aggregations.AggregationType;
import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.model.Consumption;
import com.example.electricity.report.model.Report;
import com.example.electricity.report.model.ReportRequest;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReportCreator {

  private final ElectricityUsageProvider client;
  private final Set<Aggregates> aggregates;
  private final AggregateFunction aggregateFunction;

  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public ReportCreator(ElectricityUsageProvider client,
                       Set<Aggregates> aggregates,
                       @Qualifier("sum") AggregateFunction aggregateFunction) {
    this.client = client;
    this.aggregates = aggregates;
    this.aggregateFunction = aggregateFunction;
  }

  @VisibleForTesting
  void setDateFilters(LocalDateTime startDate, LocalDateTime endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Report createReport(ReportRequest reportRequest) {

    ElectricityUsageData data = Optional.ofNullable(client.getData()).orElseGet(ElectricityUsageData::new);

    EnumMap<AggregationType, List<Consumption>> consumptions = new EnumMap<>(AggregationType.class);
    aggregates.forEach(agr ->
            consumptions.put(agr.type(),
                    calculateCost(
                            agr.aggregate(
                                    data.getUsages().stream()
                                            .filter(it -> it.getTimestamp().isAfter(Optional.ofNullable(startDate).orElse(LocalDate.now().minusYears(2).atStartOfDay().minusHours(1))))
                                            .filter(it -> it.getTimestamp().isBefore(Optional.ofNullable(endDate).orElse(LocalDate.now().atStartOfDay().plusHours(25))))
                                            .map(it -> new Consumption().setConsumption(it.getUsage()).setTimeStamp(it.getTimestamp()))
                                            .collect(Collectors.toList()), aggregateFunction), reportRequest))
    );

    return energyReportToReport(data).setConsumptionHistory(consumptions);

  }

  private Report energyReportToReport(ElectricityUsageData data) {
    return new Report()
            .setAccountingPoint(data.getAccountingPoint())
            .setDocumentDateTime(data.getDocumentTimeGeneration())
            .setDocumentIdentification(data.getDocumentId())
            .setMeasurementUnit(data.getMeasurementUnit());
  }


  private List<Consumption> calculateCost(List<Consumption> consumptions, ReportRequest reportRequest) {
    return consumptions.stream()
            .map(consumption -> consumption.setCost(reportRequest.getPrice().multiply(BigDecimal.valueOf(consumption.getConsumption()))))
            .collect(Collectors.toList());
  }

}

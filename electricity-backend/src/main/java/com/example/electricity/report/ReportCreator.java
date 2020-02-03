package com.example.electricity.report;

import com.example.electricity.provider.ElectricityUsageProvider;
import com.example.electricity.provider.model.ElectricityUsage;
import com.example.electricity.provider.model.ElectricityUsageData;
import com.example.electricity.report.aggregations.Aggregates;
import com.example.electricity.report.aggregations.AggregationType;
import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.exceptions.AggregationNotFoundException;
import com.example.electricity.report.model.Consumption;
import com.example.electricity.report.model.Report;
import com.example.electricity.report.model.ReportRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReportCreator {

  private final ElectricityUsageProvider client;
  private final Set<Aggregates> aggregates;
  private final AggregateFunction aggregateFunction;

  public ReportCreator(ElectricityUsageProvider client,
                       Set<Aggregates> aggregates,
                       @Qualifier("sum") AggregateFunction aggregateFunction) {
    this.client = client;
    this.aggregates = aggregates;
    this.aggregateFunction = aggregateFunction;
  }

  public Report createReport(ReportRequest reportRequest) {

    ElectricityUsageData data = Optional.ofNullable(client.getData()).orElseGet(ElectricityUsageData::new);

    return energyReportToReport(data)
            .setAggregationType(reportRequest.getAggregationType())
            .setConsumptionHistory(
                    calculateCost(
                            getAggregation(reportRequest.getAggregationType())
                                    .aggregate(
                                            filterHourConsumptions(
                                                    data.getUsages(),
                                                    reportRequest),
                                            aggregateFunction),
                            reportRequest)
            );
  }

  private Report energyReportToReport(ElectricityUsageData data) {
    return new Report()
            .setAccountingPoint(data.getAccountingPoint())
            .setDocumentDateTime(data.getDocumentTimeGeneration())
            .setDocumentIdentification(data.getDocumentId())
            .setMeasurementUnit(data.getMeasurementUnit());
  }

  private List<Consumption> filterHourConsumptions(List<ElectricityUsage> usages, ReportRequest reportRequest) {
    return usages.stream()
            .filter(usage -> usage.getTimestamp().isAfter(reportRequest.getStartDate().atStartOfDay().minusHours(1)))
            .filter(usage -> usage.getTimestamp().isBefore(reportRequest.getEndDate().atStartOfDay().plusHours(24)))
            .map(usage -> new Consumption()
                    .setConsumption(usage.getUsage())
                    .setTimeStamp(usage.getTimestamp()))
            .collect(Collectors.toList());
  }

  private List<Consumption> calculateCost(List<Consumption> consumptions, ReportRequest reportRequest) {
    return consumptions.stream()
            .map(consumption -> consumption.setCost(reportRequest.getPrice().multiply(BigDecimal.valueOf(consumption.getConsumption()))))
            .collect(Collectors.toList());
  }


  private Aggregates getAggregation(AggregationType aggregationType) {
    return aggregates.stream().filter(it -> it.type().equals(aggregationType))
            .findFirst().orElseThrow(AggregationNotFoundException::new);
  }

}

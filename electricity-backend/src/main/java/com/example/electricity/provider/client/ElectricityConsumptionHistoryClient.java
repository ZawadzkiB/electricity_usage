package com.example.electricity.provider.client;

import com.example.electricity.configuration.http.HttpFailedException;
import com.example.electricity.provider.client.model.EnergyReport;
import com.example.electricity.provider.model.ElectricityUsage;
import com.example.electricity.provider.model.ElectricityUsageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ElectricityConsumptionHistoryClient {

  private final RestTemplate restTemplate;
  @Value("${client.electricity.history.url}")
  private String url;

  public ElectricityConsumptionHistoryClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  ResponseEntity<EnergyReport> getFullHistory() {

    LocalDate startDate = LocalDate.now(ZoneId.of("UTC+2")).minusYears(2);
    LocalDate endDate = LocalDate.now(ZoneId.of("UTC+2")).minusDays(1);
    log.info("Get energy consumptions for: start {}, end {}", startDate, endDate);
    try {
      return restTemplate.getForEntity(url, EnergyReport.class,
              startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
              endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    } catch (RestClientException exception) {
      log.error(exception.getMessage());
      throw new HttpFailedException();
    }
  }

  ElectricityUsageData energyReportToElectricityUsage(EnergyReport energyReport) {
    return new ElectricityUsageData()
            .setAccountingPoint(energyReport.getAccountTimeSeries().getAccountingPoint())
            .setDocumentId(energyReport.getDocumentIdentification())
            .setDocumentTimeGeneration(energyReport.getDocumentDateTime())
            .setMeasurementUnit(energyReport.getAccountTimeSeries().getMeasurementUnit())
            .setUsages(energyReport.getAccountTimeSeries().getConsumptionHistory().getHourConsumption()
                    .stream().map(hourConsumption -> new ElectricityUsage()
                            .setTimestamp(hourConsumption.getTs())
                            .setUsage(hourConsumption.getConsumption())).collect(Collectors.toList()));
  }

}

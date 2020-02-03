package com.example.electricity.provider.client;

import com.example.electricity.provider.client.model.EnergyReport;
import com.example.electricity.provider.model.ElectricityUsageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.electricity.provider.client.ElectricityUsageProvider.ELECTRIC_USAGE_CACHE_NAME;
import static javax.management.timer.Timer.ONE_HOUR;

@Slf4j
@Component
public class ElectricityConsumptionHistoryJob {

  private final ElectricityConsumptionHistoryClient client;

  public ElectricityConsumptionHistoryJob(ElectricityConsumptionHistoryClient client) {
    this.client = client;
  }

  @CachePut(cacheNames = ELECTRIC_USAGE_CACHE_NAME)
  public ElectricityUsageData updateCache() {
    log.info("Sending request for electricity usage data");
    ResponseEntity<EnergyReport> responseEntity = client.getFullHistory();
    log.info("Response {}", responseEntity.getStatusCode());
    EnergyReport energyReport = Optional.ofNullable(responseEntity.getBody()).orElseGet(EnergyReport::new);
    log.info("Cache update");
    return client.energyReportToElectricityUsage(energyReport);
  }

  @Scheduled(fixedDelay = ONE_HOUR, initialDelay = 10000)
  void updateCacheScheduled() {
    updateCache();
  }

}

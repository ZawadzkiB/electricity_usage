package com.example.electricity.provider.client;

import com.example.electricity.provider.client.model.EnergyReport;
import com.example.electricity.provider.model.ElectricityUsageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ElectricityUsageProvider implements com.example.electricity.provider.ElectricityUsageProvider {

  private ElectricityConsumptionHistoryClient client;

  public ElectricityUsageProvider(ElectricityConsumptionHistoryClient client) {
    this.client = client;
  }

  @Cacheable(cacheNames = "energyReport")
  @Override
  public ElectricityUsageData getData() {
    EnergyReport energyReport = Optional.ofNullable(client.getFullHistory().getBody()).orElseGet(EnergyReport::new);
    return client.energyReportToElectricityUsage(energyReport);
  }

}

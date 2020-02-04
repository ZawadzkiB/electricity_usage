package com.example.electricity.provider;

import com.example.electricity.provider.model.ElectricityUsageData;

public interface UsageProvider {
  ElectricityUsageData getData();
}

package com.example.electricity.provider.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ElectricityUsageData {
  private String documentId;
  private LocalDateTime documentTimeGeneration;
  private String measurementUnit;
  private String accountingPoint;
  private List<ElectricityUsage> usages = new ArrayList<>();
}

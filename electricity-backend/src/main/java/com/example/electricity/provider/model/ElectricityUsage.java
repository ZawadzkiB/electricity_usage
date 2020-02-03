package com.example.electricity.provider.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ElectricityUsage {
  private LocalDateTime timestamp;
  private Double usage;
}

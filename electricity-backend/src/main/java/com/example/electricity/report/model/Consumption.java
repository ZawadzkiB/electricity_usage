package com.example.electricity.report.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Consumption {

  private LocalDateTime timeStamp;
  private Double consumption;
  private BigDecimal cost = BigDecimal.ZERO;
}

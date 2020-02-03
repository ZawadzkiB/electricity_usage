package com.example.electricity.report.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ReportRequest {

  private BigDecimal price;
}

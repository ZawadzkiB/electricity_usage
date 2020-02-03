package com.example.electricity.provider.client.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountTimeSeries {
  @XmlElement(name = "AccountingPoint")
  private String accountingPoint;
  @XmlElement(name = "ConsumptionHistory")
  private ConsumptionHistory consumptionHistory = new ConsumptionHistory();
  @XmlElement(name = "MeasurementUnit")
  private String measurementUnit;
}

package com.example.electricity.provider.client.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsumptionHistory {
  @XmlElement(name = "HourConsumption")
  private List<HourConsumption> hourConsumption = new ArrayList<>();
}

package com.example.electricity.provider.client.model;

import com.example.electricity.configuration.xml.adapter.LocalDateTimeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@XmlRootElement(name = "EnergyReport")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnergyReport {
  @XmlElement(name = "DocumentIdentification")
  private String documentIdentification;
  @XmlElement(name = "DocumentDateTime")
  @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
  private LocalDateTime documentDateTime;
  @XmlElement(name = "AccountTimeSeries")
  private AccountTimeSeries accountTimeSeries = new AccountTimeSeries();

}

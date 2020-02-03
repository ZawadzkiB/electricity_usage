package com.example.electricity.provider.client.model;

import com.example.electricity.configuration.xml.adapter.LocalDateTimeAdapter;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@Accessors(chain = true)
public class HourConsumption {
  @XmlAttribute
  @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
  private LocalDateTime ts;
  @XmlValue
  private Double consumption;
}

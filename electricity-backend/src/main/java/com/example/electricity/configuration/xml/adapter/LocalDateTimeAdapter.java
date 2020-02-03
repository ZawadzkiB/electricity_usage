package com.example.electricity.configuration.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
  public LocalDateTime unmarshal(String v) throws Exception {
    return LocalDateTime.parse(v, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:sszzzz"));
  }

  public String marshal(LocalDateTime v) throws Exception {
    return v.toString();
  }
}

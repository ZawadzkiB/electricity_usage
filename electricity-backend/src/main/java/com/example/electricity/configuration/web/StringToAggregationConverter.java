package com.example.electricity.configuration.web;

import com.example.electricity.report.aggregations.AggregationType;
import org.springframework.core.convert.converter.Converter;

public class StringToAggregationConverter implements Converter<String, AggregationType> {
  @Override
  public AggregationType convert(String source) {
    return AggregationType.valueOf(source.toUpperCase());
  }
}

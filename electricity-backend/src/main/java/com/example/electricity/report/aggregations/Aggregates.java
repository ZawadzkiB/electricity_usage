package com.example.electricity.report.aggregations;

import com.example.electricity.report.aggregations.functions.AggregateFunction;
import com.example.electricity.report.model.Consumption;

import java.util.List;

public interface Aggregates {

  List<Consumption> aggregate(List<Consumption> consumptions, AggregateFunction aggregateFunction);

  AggregationType type();
}

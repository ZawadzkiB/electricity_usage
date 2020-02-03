package com.example.electricity.report;

import com.example.electricity.provider.ElectricityUsageProvider;
import com.example.electricity.provider.model.ElectricityUsage;
import com.example.electricity.provider.model.ElectricityUsageData;
import com.example.electricity.report.aggregations.AggregationType;
import com.example.electricity.report.model.Report;
import com.example.electricity.report.model.ReportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReportCreatorTest {

  @Autowired
  ReportCreator reportCreator;

  @MockBean
  ElectricityUsageProvider client;

  private ElectricityUsageData electricityUsage;
  private ReportRequest reportRequest;

  @BeforeEach
  void setUp() {
    electricityUsage = new ElectricityUsageData();
    electricityUsage.setDocumentTimeGeneration(LocalDate.of(2020, 1, 20).atStartOfDay());
    electricityUsage.setDocumentId("ID-123456");
    electricityUsage.setMeasurementUnit("kWh");
    electricityUsage.setAccountingPoint("54EA-5481353548-U");

    List<ElectricityUsage> usages = List.of(
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 1, 1).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 1, 5).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 1, 31).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 2, 1).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 2, 15).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 2, 28).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 3, 1).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 3, 15).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 3, 31).atStartOfDay()).setUsage(1.00),

            new ElectricityUsage().setTimestamp(LocalDate.of(2018, 4, 30).atStartOfDay()).setUsage(1.00),
            new ElectricityUsage().setTimestamp(LocalDate.of(2019, 3, 31).atStartOfDay()).setUsage(1.00)
    );
    electricityUsage.setUsages(usages);

    reportRequest = new ReportRequest()
            .setPrice(BigDecimal.valueOf(0.55));

    reportCreator.setDateFilters(
            LocalDate.of(2018, 1, 1).atStartOfDay(),
            LocalDate.of(2019, 3, 31).atStartOfDay());
  }

  @Test
  void checkBaseReportData() {
    Mockito.when(client.getData()).thenReturn(electricityUsage);
    Report report = reportCreator.createReport(reportRequest);
    assertThat(report).isEqualToIgnoringGivenFields(new Report()
            .setAccountingPoint("54EA-5481353548-U")
            .setDocumentDateTime(LocalDate.of(2020, 1, 20).atStartOfDay())
            .setDocumentIdentification("ID-123456")
            .setMeasurementUnit("kWh"), "consumptionHistory");
  }

  @Test
  void createReport() {
    Mockito.when(client.getData()).thenReturn(electricityUsage);
    Report report = reportCreator.createReport(reportRequest);
    assertThat(report.getConsumptionHistory().get(AggregationType.MONTHLY).size()).isEqualTo(4);
    assertThat(report.getConsumptionHistory().get(AggregationType.WEEKLY).size()).isEqualTo(7);
    assertThat(report.getConsumptionHistory().get(AggregationType.DAILY).size()).isEqualTo(9);
  }

  @Test
  void createReportFromNullEnergyReport() {
    Mockito.when(client.getData()).thenReturn(null);
    Report report = reportCreator.createReport(reportRequest);
    assertThat(report).isNotNull();
  }

  @Test
  void createReportFromEmptyObject() {
    Mockito.when(client.getData()).thenReturn(new ElectricityUsageData());
    Report report = reportCreator.createReport(reportRequest);
    assertThat(report).isNotNull();
  }

}
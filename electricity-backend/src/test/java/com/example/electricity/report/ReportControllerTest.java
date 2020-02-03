package com.example.electricity.report;

import com.example.electricity.report.aggregations.AggregationType;
import com.example.electricity.report.model.Consumption;
import com.example.electricity.report.model.Report;
import com.example.electricity.report.validator.RequestValidatorProvider;
import com.example.electricity.report.validator.Validator;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.electricity.provider.client.ElectricityUsageProvider.ELECTRIC_USAGE_CACHE_NAME;
import static com.example.electricity.report.ReportController.DATE_FORMAT;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReportControllerTest {

  private static WireMockServer wireMockServer;

  @MockBean
  RequestValidatorProvider requestValidationProvider;

  @Autowired
  CacheManager cacheManager;

  @LocalServerPort
  private int port;

  @BeforeAll
  static void setup() {
    wireMockServer = new WireMockServer(options().port(8089));
    wireMockServer.start();
  }

  static Stream<Arguments> calculationData() {
    return Stream.of(
            arguments(LocalDate.of(2020, 1, 1),
                    AggregationType.MONTHLY, BigDecimal.valueOf(1.5), 325.96, BigDecimal.valueOf(488.94), 14),
            arguments(LocalDate.of(2020, 1, 6),
                    AggregationType.WEEKLY, BigDecimal.valueOf(1.5), 121.59, BigDecimal.valueOf(182.385), 58),
            arguments(LocalDate.of(2020, 1, 2),
                    AggregationType.DAILY, BigDecimal.valueOf(1.5), 18.22, BigDecimal.valueOf(27.330), 400)

    );
  }

  @BeforeEach
  void setUp() {
    when(requestValidationProvider.provideValidator(any())).thenReturn(new ValidatorStub());
  }

  @AfterEach
  void tearDown() {
    wireMockServer.resetAll();
    Optional.ofNullable(cacheManager.getCache(ELECTRIC_USAGE_CACHE_NAME)).ifPresent(Cache::clear);
  }

  @Test
  void getReportShouldReturn200AndSecondRequestShouldBeCached() {
    wireMockServer.stubFor(get(urlPathMatching("/kwh/*"))
            .willReturn(aResponse().withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withStatus(200)
                    .withBodyFile("json/okResponse.xml")));

    Report report = given().port(port).log().all()
            .param("startDate", LocalDate.of(2019, 1, 11).format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
            .param("endDate", LocalDate.of(2020, 1, 12).format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
            .when()
            .get("/report")
            .then().log().all()
            .statusCode(200)
            .extract().as(Report.class);

    assertThat(report.getDocumentIdentification()).isEqualTo("1580568672");
    assertThat(report.getDocumentDateTime()).isEqualTo(LocalDateTime.parse("2020-02-01T16:51:12", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    assertThat(report.getAccountingPoint()).isEqualTo("54EA-5481353548-U");
    assertThat(report.getMeasurementUnit()).isEqualTo("KWH");
    assertThat(report.getConsumptionHistory()).isNotEmpty();

    wireMockServer.verify(1, getRequestedFor(urlPathMatching("/kwh/*")));
    given().port(port).log().all()
            .when()
            .get("/report")
            .then().log().all()
            .statusCode(200);
    wireMockServer.verify(1, getRequestedFor(urlPathMatching("/kwh/*")));
  }

  @ParameterizedTest
  @MethodSource("calculationData")
  void getReportCheckPriceCalculated(
          LocalDate date,
          AggregationType aggregationType,
          BigDecimal price,
          Double usage,
          BigDecimal cost,
          Integer size) {
    wireMockServer.stubFor(get(urlPathMatching("/kwh/*"))
            .willReturn(aResponse().withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withStatus(200)
                    .withBodyFile("json/okResponse.xml")));

    Report report = given().port(port).log().all()
            .param("price", price)
            .when()
            .get("/report")
            .then().log().all()
            .statusCode(200)
            .extract().as(Report.class);

    Consumption consumption = report.getConsumptionHistory().get(aggregationType).stream()
            .filter(it -> it.getTimeStamp().isEqual(date.atStartOfDay()))
            .findFirst().orElse(new Consumption());

    assertThat(consumption).isEqualToComparingOnlyGivenFields(new Consumption()
            .setTimeStamp(date.atStartOfDay())
            .setConsumption(usage), "timeStamp", "consumption");
    assertThat(consumption.getCost()).isEqualByComparingTo(cost);
    assertThat(report.getConsumptionHistory().get(aggregationType).size()).isEqualTo(size);
  }

  @Test
  void getReportShouldReturn424WhenHttpCallReturnError() {
    wireMockServer.stubFor(get(urlPathMatching("/kwh/*"))
            .willReturn(aResponse().withStatus(403)));

    given().port(port).log().all()
            .when()
            .get("/report")
            .then().log().all()
            .statusCode(424);

    wireMockServer.stubFor(get(urlPathMatching("/kwh/*"))
            .willReturn(aResponse().withStatus(200).withBody("invalid params")));

    given().port(port).log().all()
            .when()
            .get("/report")
            .then().log().all()
            .statusCode(424);
  }

  private class ValidatorStub extends Validator {
    @Override
    public void validate(Object o, List list) {
    }
  }
}
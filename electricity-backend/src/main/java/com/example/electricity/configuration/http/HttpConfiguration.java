package com.example.electricity.configuration.http;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class HttpConfiguration {

  private static OkHttpClient getUnsafeOkHttpClient() {
    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();
    return new OkHttpClient.Builder().connectionSpecs(Arrays.asList(spec, ConnectionSpec.CLEARTEXT))
            .hostnameVerifier((hostname, session) -> true).build();
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(getUnsafeOkHttpClient()));
    restTemplate.setErrorHandler(new HttpErrorHandler());

    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.ALL_VALUE));

    restTemplate.setInterceptors(interceptors);

    return restTemplate;
  }
}

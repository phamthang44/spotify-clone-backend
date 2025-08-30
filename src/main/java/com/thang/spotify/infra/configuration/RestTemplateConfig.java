package com.thang.spotify.infra.configuration;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(10000))
                .setResponseTimeout(Timeout.ofMilliseconds(10000))
                .build();

        org.apache.hc.client5.http.impl.classic.CloseableHttpClient httpClient =
                org.apache.hc.client5.http.impl.classic.HttpClients.custom()
                        .setDefaultRequestConfig(config)
                        .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(factory);
    }

}

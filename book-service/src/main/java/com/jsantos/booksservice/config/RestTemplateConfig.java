package com.jsantos.booksservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private Environment environment;

    //private static final String URL_USER_SERVICE = "http://localhost:8082/user-service";
    private static final String URI_COVERS_SERVICE = "https://covers.openlibrary.org/b/isbn";

    @Bean
    @LoadBalanced
    public RestTemplate restTemplateUser(RestTemplateBuilder builder) {
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler(environment.getProperty("url.user.service"));
        RestTemplate restTemplate = builder
                .uriTemplateHandler(uriTemplateHandler)
                .build();

        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateCovers(RestTemplateBuilder builder, List<HttpMessageConverter<?>> messageConverters) {
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler(URI_COVERS_SERVICE);
        RestTemplate restTemplate = builder
                .uriTemplateHandler(uriTemplateHandler)
                .build();
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

}

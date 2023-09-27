package com.jsantos.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private Environment environment;

    //private static final String URL_BOOK_SERVICE = "http://localhost:8082/book-service";


    @Bean
    @LoadBalanced
    public RestTemplate restTemplateBook(RestTemplateBuilder builder, List<HttpMessageConverter<?>> messageConverters) {
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler(environment.getProperty("url.book.service"));
        RestTemplate restTemplate = builder
                .uriTemplateHandler(uriTemplateHandler)
                .build();
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

}

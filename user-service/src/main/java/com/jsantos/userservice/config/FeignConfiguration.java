package com.jsantos.userservice.config;

import com.jsantos.userservice.model.exception.feign.FeignErrorDecoder;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    //@Bean
    /*public FeignErrorDecoder getFeignErrorDecoder() {
        return new FeignErrorDecoder();
    }*/

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

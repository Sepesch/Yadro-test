package com.yadro.yadro.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@Configuration
public class MainConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().stream()
            .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
            .findFirst()
            .ifPresent(converter -> {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonConverter.setSupportedMediaTypes(
                    Arrays.asList(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_PLAIN,
                        MediaType.TEXT_HTML
                    )
                );
            });
        return restTemplate;
    }
}
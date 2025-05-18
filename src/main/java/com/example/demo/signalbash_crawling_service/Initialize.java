package com.example.demo.signalbash_crawling_service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;;

@Configuration
public class Initialize {
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean(name = "computeTimeScript")
    public String getComputeTimeScript() throws IOException {
        return StreamUtils.copyToString(new ClassPathResource("static/computeTime.js").getInputStream(), Charset.defaultCharset());
    }
}

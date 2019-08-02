package com.zadorovskyi.cars.rent.service.web.config;

import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PropertiesBeanConfiguration {

    private static final String PROPERTIES_FILE_PATH = "C:\\PETRi\\MY\\cars-rent-service\\config\\src\\main\\java\\config\\config.yml";

    @Bean
    public static ApplicationConfiguration createProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            File ymlFile = new File(PROPERTIES_FILE_PATH);
            return mapper.readValue(ymlFile, ApplicationConfiguration.class);
        } catch (Exception e) {
            log.error("Error ocurred during yaml propetries parsing. Exception={}", e.getMessage());
            throw e;
        }
    }
}

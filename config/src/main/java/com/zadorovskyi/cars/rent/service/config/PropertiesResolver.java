package com.zadorovskyi.cars.rent.service.config;

import java.io.File;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertiesResolver<T> {

    public static final String APPLICATION_YAML =
        "C:\\PETRi\\MY\\cars-rent-service\\config\\src\\main\\java\\com\\zadorovskyi\\cars\\rent\\service\\config\\application.yaml";

    public T resolve(Class<T> type) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            File ymlFile = new File(APPLICATION_YAML);
            JsonNode node = mapper.readTree(ymlFile).get(resolveFieldName(type.getSimpleName()));
            return mapper.treeToValue(node, type);
        } catch (Exception e) {
            log.error("Error occurred during yaml properties parsing. Exception={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String resolveFieldName(String className) {
        return (className.substring(0, 1).toLowerCase() + className.substring(1));
    }
}

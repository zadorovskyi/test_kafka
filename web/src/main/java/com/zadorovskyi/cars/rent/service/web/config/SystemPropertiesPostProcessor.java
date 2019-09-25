package com.zadorovskyi.cars.rent.service.web.config;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import com.zadorovskyi.cars.rent.service.config.PropertiesResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class SystemPropertiesPostProcessor implements EnvironmentPostProcessor {

    @Override public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PropertiesResolver<SystemProperties> propertiesResolver = new PropertiesResolver<>();
        SystemProperties systemProperties = propertiesResolver.resolve(SystemProperties.class);
        loadProperties(systemProperties.getProperties());
    }

    private void loadProperties(Properties props) {
        props.forEach((key, value) -> System.setProperty((String) key, (String) value));
    }
}




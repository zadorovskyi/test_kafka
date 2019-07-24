package com.zadorovskyi.cars.rent.service.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.zadorovskyi.cars.rent.service.kafka.config.KafkaClientConfiguration;
import com.zadorovskyi.cars.rent.service.kafka.interceptor.PolymorphicAvroAnnotationIntrospector;

@Configuration
public class KafkaBeansConfiguration {

    @Autowired
    private KafkaClientConfiguration configuration;

    @Bean
    public ObjectMapper buildAvroMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).registerModules(
                new Jdk8Module(), new JavaTimeModule(), new ParameterNamesModule())
            .setAnnotationIntrospector(
                new AnnotationIntrospectorPair(new PolymorphicAvroAnnotationIntrospector(), new JacksonAnnotationIntrospector()));
    }

    @Bean
    public Producer buildProducer() {
        return new KafkaProducer<String, Object>(configuration.createProperties());
    }

}

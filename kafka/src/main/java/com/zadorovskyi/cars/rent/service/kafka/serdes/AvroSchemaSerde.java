package com.zadorovskyi.cars.rent.service.kafka.serdes;

import java.util.Map;

import org.apache.kafka.common.serialization.Serde;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.zadorovskyi.cars.rent.service.kafka.interceptor.PolymorphicAvroAnnotationIntrospector;

public class AvroSchemaSerde<T> implements Serde<T> {

    public static AvroMapper MAPPER;

    static {
        MAPPER = (AvroMapper) (new AvroMapper()).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).registerModules(
                new Jdk8Module(), new JavaTimeModule(), new ParameterNamesModule())
            .setAnnotationIntrospector(
                new AnnotationIntrospectorPair(new PolymorphicAvroAnnotationIntrospector(), new JacksonAnnotationIntrospector()));
    }

    protected final AvroSchemaSerializer<T> serializer;

    protected final FaultTolerantDeserializer<T> deserializer;

    public AvroSchemaSerde() {
        this(MAPPER);

    }

    protected AvroSchemaSerde(AvroMapper mapper) {
        this.serializer = new AvroSchemaSerializer<>(mapper);
        this.deserializer = new FaultTolerantDeserializer<>(mapper);
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        this.serializer.configure(configs, isKey);
        this.deserializer.configure(configs, isKey);
    }

    public void close() {
        this.serializer.close();
        this.deserializer.close();
    }

    public AvroSchemaSerializer<T> serializer() {
        return this.serializer;
    }

    public AvroSchemaDeserializer<T> deserializer() {
        return this.deserializer;
    }
}

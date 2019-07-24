package com.zadorovskyi.cars.rent.service.kafka.serdes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;

import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDe;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvroSchemaDeserializer<T> extends AbstractKafkaAvroSerDe implements Deserializer<T> {

    protected final AvroMapper mapper;

    public AvroSchemaDeserializer(AvroMapper mapper) {
        if (mapper == null) {
            throw new NullPointerException("mapper");
        } else {
            this.mapper = mapper;
        }
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        this.configureClientProperties(new KafkaAvroDeserializerConfig(configs));
    }

    public T deserialize(String topic, byte[] data) {
        if (data != null && data.length != 0) {
            ByteBuffer dataBuffer = ByteBuffer.wrap(data);
            byte magic = dataBuffer.get();
            if (magic != 0) {
                throw new SerializationException("Unknown magic byte: " + magic + " (expected: " + 0 + ")");
            } else {
                int schemaId = dataBuffer.getInt();

                Schema schema;
                try {
                    schema = this.schemaRegistry.getById(schemaId);

                } catch (RestClientException | IOException var9) {
                    throw new SerializationException("Failed to retrieve schema for id: " + schemaId, var9);
                }

                try {
                    ObjectReader reader = this.mapper.readerFor(Object.class).with(new AvroSchema(schema));
                    return (T) reader
                        .readValue(dataBuffer.array(), dataBuffer.arrayOffset() + dataBuffer.position(), dataBuffer.remaining());
                } catch (Exception var8) {
                    throw new SerializationException("Failed to deserialize avro message", var8);
                }
            }
        } else {
            return null;
        }
    }

    public void close() {
    }
}

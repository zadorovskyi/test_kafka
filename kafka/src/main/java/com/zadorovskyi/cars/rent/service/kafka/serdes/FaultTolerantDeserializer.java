package com.zadorovskyi.cars.rent.service.kafka.serdes;

import java.util.Map;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FaultTolerantDeserializer<T> extends AvroSchemaDeserializer<T> {

    public FaultTolerantDeserializer(AvroMapper avroMapper) {
        super(avroMapper);
    }

    /**
     * @param topic the topic name
     * @param data  serialized bytes;
     * @return deserialized typed data; may be null
     * In case Kafka Producer changes package of a message,  deserialization return HashMap instead of the message.
     * Check 'if (result instanceof Map)' avoid an exception during casting map to object and prevent stop message processing.
     */
    @Override
    public T deserialize(String topic, byte[] data) {
        T result = super.deserialize(topic, data);
        if (result instanceof Map) {
            log.error("Error during deserialization record. topic: {} data: {}. " +
                "Probable cause - the record was moved to another package", topic, data);
            return null;
        }
        return result;
    }

}

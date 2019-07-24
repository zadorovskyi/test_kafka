package com.zadorovskyi.cars.rent.service.kafka.serdes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDe;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvroSchemaSerializer<T> extends AbstractKafkaAvroSerDe implements Serializer<T> {

    private AvroMapper mapper;

    private boolean isKey;

    private final LoadingCache<Type, Schema> schemaCache;

    public AvroSchemaSerializer() {
        this(AvroSchemaSerde.MAPPER);
    }

    @Autowired
    public AvroSchemaSerializer(@NonNull AvroMapper mapper) {
        this.schemaCache = buildCache();
        Objects.requireNonNull(mapper, "mapper is null");
        this.mapper = mapper;
    }

    @Override public byte[] serialize(String topic, Object data) {
        Schema schema;
        int schemaId;
        if (data == null) {
            return null;
        } else {
            try {
                schema = this.getSchema(data);
            } catch (Exception e) {
                log.debug("Failed to build schema on topic {} for record: {}", new Object[] { topic, data.toString(), e });
                throw new SerializationException("Error building Avro schema for record", e);
            }

            try {
                String subject = this.getSubjectName(topic, isKey);
                schemaId = this.schemaRegistry.register(subject, schema);
            } catch (Exception e) {
                log.debug("Error registering Avro schema on topic {}", topic, e);
                throw new SerializationException("Error registering Avro schema", e);
            }
            return serialize(topic, data, schema, schemaId);
        }
    }

    private byte[] serialize(String topic, Object data, Schema schema, int schemaId) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(0);
            out.write(ByteBuffer.allocate(4).putInt(schemaId).array());
            ObjectWriter writer = this.mapper.writerFor(Object.class).with(new AvroSchema(schema));
            writer.writeValue(out, data);
            byte[] bytes = out.toByteArray();
            out.close();
            return bytes;
        } catch (RuntimeException | IOException e) {
            log.debug("Error serializing Avro message to topic {}", topic, e);
            throw new SerializationException("Error serializing Avro message", e);
        }
    }

    protected Schema getSchema(Object object) {
        if (object == null) {
            throw new NullPointerException("datum");
        } else {
            return object instanceof GenericContainer ? generateSchema(object) : this.getSchema((Type) object.getClass());
        }
    }

    private Schema getSchema(Type datumType) {
        return (Schema) this.schemaCache.getUnchecked(datumType);
    }

    private Schema generateSchema(Object object) {
        AvroSchemaGenerator gen = new AvroSchemaGenerator();
        try {
            mapper.acceptJsonFormatVisitor(object.getClass(), gen);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
        AvroSchema schemaWrapper = gen.getGeneratedSchema();

        return schemaWrapper.getAvroSchema();

    }

    @Override public void configure(Map<String, ?> configs, boolean isKey) {
        this.configureClientProperties(new KafkaAvroSerializerConfig(configs));
        this.isKey = isKey;
    }

    @Override public void close() {
        schemaCache.cleanUp();
    }

    private LoadingCache<Type, Schema> buildCache() {
        return CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Type, Schema>() {

            public Schema load(Type o) throws Exception {
                if (o instanceof Class && ((Class) o).getTypeParameters().length > 0) {
                    log.warn("Trying to serialize object of generic type {} with no type bindings", ((Class) o).getName());
                }

                return AvroSchemaSerializer.this.mapper.schemaFor(AvroSchemaSerializer.this.mapper.constructType(o)).getAvroSchema();
            }
        });
    }
}

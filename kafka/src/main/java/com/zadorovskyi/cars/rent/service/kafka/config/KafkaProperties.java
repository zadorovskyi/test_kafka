package com.zadorovskyi.cars.rent.service.kafka.config;

import java.util.Properties;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaProperties {

    @Valid
    @NotNull
    private String kafkaBrokerUrl;

    @Valid
    @NotNull
    private String schemaRegistryUrl;

    @Valid
    @NotNull
    private String clientId;

    @Valid
    private String topic;

    @Valid
    @NotNull
    private String groupId;

    @Valid
    @NotNull
    private String applicationId;

    @Valid
    @NotNull
    private Class<? extends Serde> valueSerde;

    @NotNull
    private String specificAvroReader = "true";

    @Valid
    @NotNull
    private Class<? extends Serializer> producerValueSerializer;

    @Valid
    @Min(0L)
    private int numStandByReplicas = 0;

    @Valid
    @NotNull
    private Offset offset = Offset.EARLIEST;

    @Valid
    @NotNull
    private boolean enableAutoCommit = true;

    @Valid
    @NotNull
    private boolean enableTopicAutoCreate = true;

    @Valid
    @NotNull
    private int requestTimeoutMs = 100;

    @Valid
    @Min(1L)
    private int replicationFactor = 3;

    @Valid
    private String securityProtocol;

    @Valid
    private String sslKeystoreLocation;

    @Valid
    private String sslKeystorePassword;

    @Valid
    private String sslTruststoreLocation;

    @Valid
    private String sslTruststorePassword;

    @Valid
    private String sslKeyPassword;

    public Properties createProperties() {
        Properties props = new Properties();
        props.put("group.id", this.groupId);
        props.put("client.id", this.clientId);
        props.put("schema.registry.url", this.schemaRegistryUrl);
        props.put("bootstrap.servers", this.kafkaBrokerUrl);
        props.put("num.standby.replicas", this.numStandByReplicas);
        props.put("replication.factor", this.replicationFactor);
        props.put("default.key.serde", Serdes.String().getClass().getName());
        props.put("default.value.serde", this.valueSerde.getName());
        props.put("specific.avro.reader", this.specificAvroReader);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", this.producerValueSerializer.getName());
        props.put("auto.offset.reset", this.offset.value);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("enable.auto.commit", this.enableAutoCommit);
        props.put("request.timeout.ms", this.requestTimeoutMs);
        props.put("application.id", this.applicationId);
        props.put("auto.create.topics.enable", this.enableTopicAutoCreate);
        this.putValueIfPresent("security.protocol", this.getSecurityProtocol(), props);
        this.putValueIfPresent("ssl.truststore.location", this.getSslTruststoreLocation(), props);
        this.putValueIfPresent("ssl.truststore.password", this.getSslTruststorePassword(), props);
        this.putValueIfPresent("ssl.keystore.location", this.getSslKeystoreLocation(), props);
        this.putValueIfPresent("ssl.keystore.password", this.getSslKeystorePassword(), props);
        this.putValueIfPresent("ssl.key.password", this.getSslKeyPassword(), props);
        return props;
    }

    private void putValueIfPresent(String key, String value, Properties properties) {
        if (value != null) {
            properties.setProperty(key, value);
        }

    }

    enum Offset {
        EARLIEST("earliest"),
        LATEST("latest"),
        NONE("none");

        private final String value;

        private Offset(String value) {
            this.value = value;
        }
    }
}

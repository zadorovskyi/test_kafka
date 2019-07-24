package com.zadorovskyi.cars.rent.service.config;

import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.config.TinkConfig;
import com.zadorovskyi.cars.rent.service.kafka.config.KafkaClientConfiguration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BeanConfiguration {

    @Autowired
    private ApplicationConfiguration configuration;

    @Bean
    public KafkaClientConfiguration getKafkaClientConfig() {
        return configuration.getKafkaClient();
    }

    @Qualifier("kafka.topic")
    @Bean
    public String getKafkaTopic() {
        return configuration.getKafkaClient().getTopic();
    }

    @Bean
    public Aead createTintAead() {
        try {
            TinkConfig.register();
            KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM);
            return AeadFactory.getPrimitive(keysetHandle);
        } catch (GeneralSecurityException e) {
            log.error("Text encrypt decrypt error. Error occurred during encryptor creation={}", e);
            throw new RuntimeException(e);
        }
    }
}

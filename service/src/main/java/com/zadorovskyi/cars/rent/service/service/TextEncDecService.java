package com.zadorovskyi.cars.rent.service.service;

import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.crypto.tink.Aead;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class TextEncDecService {

    private final Aead tintAeadEncDec;

    @Autowired
    public TextEncDecService(Aead tintAeadEncDec) {
        this.tintAeadEncDec = tintAeadEncDec;
    }

    public byte[] encrypt(byte[] encriptingValue, String secret) {
        try {

            return tintAeadEncDec.encrypt(encriptingValue, secret.getBytes());
        } catch (GeneralSecurityException ex) {
            log.error("Text encrypt decrypt service error. Text encryption failed. Exception={}", ex);
            throw new RuntimeException(ex);
        }
    }

    public byte[] decrypt(byte[] cipherValue, String secret) {
        try {

            return tintAeadEncDec.decrypt(cipherValue, secret.getBytes());
        } catch (GeneralSecurityException ex) {
            log.error("Text encrypt decrypt service error. Text encryption failed. Exception={}", ex);
            throw new RuntimeException(ex);

        }
    }

}

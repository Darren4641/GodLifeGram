package com.godlife.godlifegram.common.config;

import lombok.val;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.Security;

@Configuration
public class NotificationConfig {
    @Value("${vapid.key.subject}")
    private String subject;
    @Value("${vapid.key.public}")
    private String publicKey;
    @Value("${vapid.key.private}")
    private String privateKey;

    @Bean
    public PushService PushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        return new PushService(publicKey, privateKey, subject);
    }
}

package com.redpony.authservice.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("security")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SecurityProperties {

    private Resource keyStore;
    private String keyStorePassword;
    private String keyPairAlias;
    private String keyPairPassword;
    }
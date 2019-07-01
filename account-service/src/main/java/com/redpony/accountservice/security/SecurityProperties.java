package com.redpony.accountservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties("security")
public class SecurityProperties {
    private Resource publicKey;
}

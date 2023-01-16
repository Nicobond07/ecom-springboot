package com.ecom.ecom.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties("ecom.security")
@Getter
@Setter
public class EcomSecurity {
    private Map<String, EndPointSecurity> endpoints;
}

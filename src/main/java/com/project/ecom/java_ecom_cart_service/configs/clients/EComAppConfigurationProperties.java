package com.project.ecom.java_ecom_cart_service.configs.clients;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ecom.app")
public class EComAppConfigurationProperties {
    private String url;
}

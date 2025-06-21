package com.project.ecom.java_ecom_cart_service;

import com.project.ecom.java_ecom_cart_service.configs.clients.EComAppConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableCaching
@EnableMongoRepositories(basePackages = "com.project.ecom.java_ecom_cart_service.repositories.mongo")
@EnableMongoAuditing
@EnableJpaRepositories(basePackages = "com.project.ecom.java_ecom_cart_service.repositories.jpa")
@EnableJpaAuditing
@EnableConfigurationProperties(value = EComAppConfigurationProperties.class)
@SpringBootApplication
public class JavaEcomCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEcomCartServiceApplication.class, args);
    }

}

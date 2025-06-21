package com.project.ecom.java_ecom_cart_service.configs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/*
Default JdkSerializer was not able to serialize the below fields in the CartDocument:
- id
- createdAt
- updatedAt

Hence, using Jackson2JsonSerializer

Resolving Error:
Could not write JSON: Java 8 date/time type `java.time.LocalDateTime` not supported by default:
add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (or disable `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`)
(through reference chain: com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument["createdAt"])

Cause: GenericJackson2JsonSerializer does not know how to serialize/deserialize LocalDateTime.

Solution:
Register JavaTimeModule with Jackson ObjectMapper used by Redis.
 */

@Configuration
public class RedisConfig {
    @Bean(name = "customRedisConfig")
    public RedisCacheConfiguration redisConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // support for LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // readable ISO8601 format

        /* Issue during deserialization: Getting error while reading data from cache

        Error:
        class java.util.LinkedHashMap cannot be cast to class com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument (java.util.LinkedHashMap is in module java.base of loader 'bootstrap'; com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument is in unnamed module of loader org.springframework.boot.devtools.restart.classloader.RestartClassLoader @7df4d094)

        Possible Resolution:
        Activating type info so that the deserializer knows which class to use

         */
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1L))  // TTL - 1 Day
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(objectMapper)
                        )
                );
    }
}

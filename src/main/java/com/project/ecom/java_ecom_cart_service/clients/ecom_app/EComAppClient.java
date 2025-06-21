package com.project.ecom.java_ecom_cart_service.clients.ecom_app;

import com.project.ecom.java_ecom_cart_service.dtos.CheckoutRequestDto;
import com.project.ecom.java_ecom_cart_service.dtos.ProductDto;
import com.project.ecom.java_ecom_cart_service.exceptions.ExternalServiceException;
import com.project.ecom.java_ecom_cart_service.models.mongo.CartItemDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Component
public class EComAppClient {
    private final RestTemplate restTemplate;
    @Value("${ecom.app.url}")
    private String ECOM_APP_URL;

    @Autowired
    public EComAppClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(3L))  // max timeout to establish a connection with ecom_app
                .readTimeout(Duration.ofSeconds(5L))  // max timeout to get the response back
                .build();
    }

    public ProductDto getBasicProductDetails(Long productId) {
        String url = ECOM_APP_URL + "/api/product/basic-info/{id}";
        try {
            ResponseEntity<ProductDto> responseEntity = this.restTemplate.getForEntity(url, ProductDto.class, productId);
            System.out.println(":: DEBUG :: LOG :: EComAppClient :: productDetails :: " + responseEntity.getBody().getProductId());
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalServiceException(
                    "ECom App responded with an error.",
                    e.getStatusCode(),
                    e.getResponseBodyAsString(),
                    e
            );
        }
    }

    public Long cartCheckout(List<CartItemDocument> cartItems, Long deliveryAddressId, String accessToken) {
        String url = ECOM_APP_URL + "/api/cart/checkout";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.AUTHORIZATION, accessToken);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        CheckoutRequestDto checkoutRequest = new CheckoutRequestDto();
        checkoutRequest.setCartItems(cartItems);
        checkoutRequest.setDeliveryAddressId(deliveryAddressId);
        HttpEntity<CheckoutRequestDto> requestEntity = new HttpEntity<>(checkoutRequest, requestHeaders);

        try {
            ResponseEntity<Long> responseEntity = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, Long.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ExternalServiceException(
                    "ECom App responded with an error",
                    e.getStatusCode(),
                    e.getResponseBodyAsString(),
                    e
            );
        }
    }
}

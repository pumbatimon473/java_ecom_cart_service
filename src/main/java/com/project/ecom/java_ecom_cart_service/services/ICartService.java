package com.project.ecom.java_ecom_cart_service.services;

import com.project.ecom.java_ecom_cart_service.dtos.ProductDto;
import com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument;

public interface ICartService {
    CartDocument addProductToCart(Long customerId, ProductDto productDto, Integer quantity);

    CartDocument removeCartItem(Long customerId, Long productId);

    CartDocument updateCartItem(Long customerId, Long productId, Integer incrementVal);

    CartDocument getCart(Long customerId);  // returns ACTIVE cart

    Long checkoutCart(Long customerId, Long deliveryAddressId, String accessToken);
}

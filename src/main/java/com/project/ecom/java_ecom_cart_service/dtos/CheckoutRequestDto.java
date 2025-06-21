package com.project.ecom.java_ecom_cart_service.dtos;

import com.project.ecom.java_ecom_cart_service.models.mongo.CartItemDocument;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequestDto {
    private List<CartItemDocument> cartItems;
    private Long deliveryAddressId;
}

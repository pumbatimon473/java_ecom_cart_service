package com.project.ecom.java_ecom_cart_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartCheckoutRequestDto {
    @NotNull
    private Long deliveryAddressId;
}

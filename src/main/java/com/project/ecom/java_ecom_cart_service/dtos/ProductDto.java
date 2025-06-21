package com.project.ecom.java_ecom_cart_service.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long productId;
    private String name;
    private String primaryImageUrl;
    private BigDecimal price;
}

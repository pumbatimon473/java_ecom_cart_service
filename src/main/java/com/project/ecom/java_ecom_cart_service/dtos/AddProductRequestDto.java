package com.project.ecom.java_ecom_cart_service.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductRequestDto {
    @NotNull
    @Valid
    private ProductDto product;
    @NotNull @Min(1)
    private Integer quantity;
}

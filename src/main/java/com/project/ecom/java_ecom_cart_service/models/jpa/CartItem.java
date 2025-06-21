package com.project.ecom.java_ecom_cart_service.models.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class CartItem extends BaseModel {
    private Long productId;
    private String productName;
    private String primaryImageUrl;
    private Integer quantity;
    private BigDecimal price;  // price per unit

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}

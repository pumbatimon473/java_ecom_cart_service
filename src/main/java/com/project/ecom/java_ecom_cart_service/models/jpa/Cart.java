package com.project.ecom.java_ecom_cart_service.models.jpa;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.ecom.java_ecom_cart_service.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Cart extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonManagedReference
    private CustomerSession session;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    @Enumerated(EnumType.STRING)
    private CartStatus status;
}

package com.project.ecom.java_ecom_cart_service.models.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.ecom.java_ecom_cart_service.enums.UserSessionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class CustomerSession extends BaseModel {
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private UserSessionStatus status;

    @OneToMany(mappedBy = "session")
    @JsonBackReference
    private List<Cart> carts;  // A customer session can have many carts but only one of them will be active

    public Boolean isActive() {
        return this.status == UserSessionStatus.ACTIVE;
    }
}

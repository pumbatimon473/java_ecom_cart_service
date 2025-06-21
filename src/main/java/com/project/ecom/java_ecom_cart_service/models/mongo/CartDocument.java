package com.project.ecom.java_ecom_cart_service.models.mongo;

import com.project.ecom.java_ecom_cart_service.enums.CartStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Document(collection = "cart")
public class CartDocument extends MongoBaseModel implements Serializable {
    @Field(name = "customer_id")
    private Long customerId;

    @Field(name = "status")
    private CartStatus status;

    @Field
    private List<CartItemDocument> items;
}

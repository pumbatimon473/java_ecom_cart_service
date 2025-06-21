package com.project.ecom.java_ecom_cart_service.models.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/*
Another error after enabling type info:

Could not read JSON:Cannot construct instance of `com.project.ecom.java_ecom_cart_service.models.mongo.CartItemDocument` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
 at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 333] (through reference chain: com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument["items"]->java.util.ArrayList[0])


Resolution:
- Disable @Builder for CartItemDocument.
    - As a default CTOR is required for JSON deserialization by Redis
 */


@Getter
@Setter
//@Builder  // Disabled builder as a default CTOR is required for deserialization by Redis
public class CartItemDocument implements Serializable {
    @Field
    private String id = UUID.randomUUID().toString();

    @Field(name = "product_id")
    private Long productId;

    @Field(name = "product_name")
    private String productName;

    @Field(name = "primary_image_url")
    private String primaryImageUrl;

    @Field
    private Integer quantity;

    @Field(name = "price_at_addition")
    private BigDecimal priceAtAddition;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItemDocument that = (CartItemDocument) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}

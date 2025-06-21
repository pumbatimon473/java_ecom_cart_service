package com.project.ecom.java_ecom_cart_service.repositories.mongo;

import com.project.ecom.java_ecom_cart_service.enums.CartStatus;
import com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ICartDocumentRepository extends MongoRepository<CartDocument, String> {
    Optional<CartDocument> findByCustomerIdAndStatus(Long customerId, CartStatus status);
}

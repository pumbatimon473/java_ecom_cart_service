package com.project.ecom.java_ecom_cart_service.services;

import com.project.ecom.java_ecom_cart_service.clients.ecom_app.EComAppClient;
import com.project.ecom.java_ecom_cart_service.dtos.ProductDto;
import com.project.ecom.java_ecom_cart_service.enums.CartStatus;
import com.project.ecom.java_ecom_cart_service.exceptions.InvalidProductDetailsException;
import com.project.ecom.java_ecom_cart_service.exceptions.NoActiveCartException;
import com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument;
import com.project.ecom.java_ecom_cart_service.models.mongo.CartItemDocument;
import com.project.ecom.java_ecom_cart_service.repositories.mongo.ICartDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService implements ICartService {
    private final ICartDocumentRepository cartRepo;
    private final EComAppClient eComAppClient;

    @Autowired
    public CartService(ICartDocumentRepository cartRepo, EComAppClient eComAppClient) {
        this.cartRepo = cartRepo;
        this.eComAppClient = eComAppClient;
    }

    @CacheEvict(value = "activeCart", key = "#customerId")
    @Override
    public CartDocument addProductToCart(Long customerId, ProductDto productDto, Integer quantity) {
        CartDocument cart = this.cartRepo.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElse(null);
        if (cart == null) {  // create a new cart
            cart = new CartDocument();
            cart.setCustomerId(customerId);
            cart.setStatus(CartStatus.ACTIVE);
        }

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        // check if there already exists an item in the cart with the same product id
        Optional<CartItemDocument> cartItemOptional = cart.getItems().stream().filter(item -> Objects.equals(item.getProductId(), productDto.getProductId()))
                .findFirst();
        if (cartItemOptional.isPresent()) {  // update the product's quantity
            CartItemDocument cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {  // add the product to the cart
            validateProductDetails(productDto);
            // NOTE: product price will be locked after the first addition, i.e., adding the same product again will use the price when it was first added
            CartItemDocument cartItem = new CartItemDocument();
            cartItem.setId(UUID.randomUUID().toString());
            cartItem.setProductId(productDto.getProductId());
            cartItem.setProductName(productDto.getName());
            cartItem.setPrimaryImageUrl(productDto.getPrimaryImageUrl());
            cartItem.setPriceAtAddition(productDto.getPrice());
            cartItem.setQuantity(quantity);

            cart.getItems().add(cartItem);
        }
        return this.cartRepo.save(cart);
    }

    private void validateProductDetails(ProductDto productDto) {
        ProductDto productDetails = this.eComAppClient.getBasicProductDetails(productDto.getProductId());
        Boolean isValid = Boolean.TRUE;
        if (!productDetails.getName().equals(productDto.getName()))
            isValid = Boolean.FALSE;
        if (!productDetails.getPrice().equals(productDto.getPrice()))
            isValid = Boolean.FALSE;
        if (!isValid)
            throw new InvalidProductDetailsException("Provided product details do not match server record");
    }

    @CacheEvict(value = "activeCart", key = "#customerId")
    @Override
    public CartDocument removeCartItem(Long customerId, Long productId) {
        CartDocument cart = this.cartRepo.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveCartException(customerId));
        // filter the items by productId
        Optional<CartItemDocument> cartItemOptional = cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProductId(), productId)).findFirst();
        if (cartItemOptional.isEmpty())
            return cart;  // No change

        // remove the cart item if a match is found
        cartItemOptional.ifPresent(item -> cart.getItems().remove(item));
        return this.cartRepo.save(cart);
    }

    @CacheEvict(value = "activeCart", key = "#customerId")
    @Override
    public CartDocument updateCartItem(Long customerId, Long productId, Integer incrementVal) {
        CartDocument cart = this.cartRepo.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveCartException(customerId));
        // filter the cart item having the given productId
        Optional<CartItemDocument> cartItemOptional = cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProductId(), productId)).findFirst();
        if (cartItemOptional.isEmpty())
            throw new IllegalArgumentException(String.format("No product with id %d exists in the cart", productId));

        CartItemDocument cartItem = cartItemOptional.get();
        if (cartItem.getQuantity() + incrementVal <= 0) {  // remove the cartItem
            cart.getItems().remove(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + incrementVal);
        }

        return this.cartRepo.save(cart);
    }

    @Cacheable(value = "activeCart", key = "#customerId")
    @Override
    public CartDocument getCart(Long customerId) {
        return this.cartRepo.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveCartException(customerId));
    }

    @CacheEvict(value = "activeCart", key = "#customerId")
    @Override
    public Long checkoutCart(Long customerId, Long deliveryAddressId, String accessToken) {
        CartDocument cart = this.cartRepo.findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveCartException(customerId));

        Long orderId = this.eComAppClient.cartCheckout(cart.getItems(), deliveryAddressId, accessToken);
        // update the cart status
        cart.setStatus(CartStatus.CHECKED_OUT);
        this.cartRepo.save(cart);

        return orderId;
    }
}

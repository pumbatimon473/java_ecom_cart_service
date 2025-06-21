package com.project.ecom.java_ecom_cart_service.controllers;

import com.project.ecom.java_ecom_cart_service.dtos.AddProductRequestDto;
import com.project.ecom.java_ecom_cart_service.dtos.CartCheckoutRequestDto;
import com.project.ecom.java_ecom_cart_service.models.mongo.CartDocument;
import com.project.ecom.java_ecom_cart_service.services.ICartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final ICartService cartService;

    @Autowired
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/products")
    public ResponseEntity<CartDocument> addProduct(@Valid @RequestBody AddProductRequestDto requestDto, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = jwt.getClaim("user_id");
        CartDocument cart = this.cartService.addProductToCart(customerId, requestDto.getProduct(), requestDto.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @GetMapping
    public ResponseEntity<CartDocument> getCart(@AuthenticationPrincipal Jwt jwt) {
        Long customerId = jwt.getClaim("user_id");
        CartDocument cart = this.cartService.getCart(customerId);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<CartDocument> updateCartItem(@PathVariable(name = "id") Long productId, @RequestParam(name = "increment_val") Integer incrementVal, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = jwt.getClaim("user_id");
        CartDocument cart = this.cartService.updateCartItem(customerId, productId, incrementVal);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<CartDocument> removeCartItem(@PathVariable(name = "id") Long productId, @AuthenticationPrincipal Jwt jwt) {
        Long customerId = jwt.getClaim("user_id");
        CartDocument cart = this.cartService.removeCartItem(customerId, productId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@Valid @RequestBody CartCheckoutRequestDto requestDto, @AuthenticationPrincipal Jwt jwt, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken) {
        Long customerId = jwt.getClaim("user_id");
        Long orderId = this.cartService.checkoutCart(customerId, requestDto.getDeliveryAddressId(), accessToken);
        return ResponseEntity.ok("Order Id: " + orderId);
    }
}

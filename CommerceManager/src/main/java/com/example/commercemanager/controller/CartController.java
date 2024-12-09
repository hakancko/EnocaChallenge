package com.example.commercemanager.controller;

import com.example.commercemanager.dto.CartDTO;
import com.example.commercemanager.entity.Cart;
import com.example.commercemanager.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long customerId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.addProductToCart(customerId, productId, quantity));
    }

    @PostMapping("/{customerId}/remove")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Long customerId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.removeProductFromCart(customerId, productId, quantity));
    }

    @PostMapping("/{customerId}/empty")
    public ResponseEntity<Void> emptyCart(@PathVariable Long customerId) {
        cartService.emptyCart(customerId);
        return ResponseEntity.ok().build();
    }
}

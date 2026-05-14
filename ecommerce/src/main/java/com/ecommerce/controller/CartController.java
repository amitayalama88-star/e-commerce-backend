package com.ecommerce.controller;

import com.ecommerce.dto.CartResponse;
import com.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String username, @RequestParam Long itemId, @RequestParam(defaultValue = "1") int quantity) {
        cartService.addToCart(username, itemId, quantity);
        return "Item added to cart! Go see your cart now.";
    }

    @GetMapping
    public CartResponse getCart(@RequestParam String username) {
        return cartService.getCart(username);
    }

    @DeleteMapping("/clear")
    public String clearCart(@RequestParam String username) {
        cartService.clearCart(username);
        return "Your cart is empty now.";
    }
}

package com.ecommerce.controller;

import com.ecommerce.dto.CheckoutResponse;
import com.ecommerce.dto.OrderHistoryResponse;
import com.ecommerce.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public Object checkout(@RequestParam String username) {
        try {
            return orderService.checkoutFromCart(username);
        } catch (RuntimeException e) {
            return "Oops! " + e.getMessage();
        }
    }

    @GetMapping("/history")
    public List<OrderHistoryResponse> getOrderHistory(@RequestParam String username) {
        return orderService.getOrderHistory(username);
    }
}

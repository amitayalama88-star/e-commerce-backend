package com.ecommerce.service;

import com.ecommerce.dto.CheckoutResponse;
import com.ecommerce.dto.OrderHistoryResponse;
import com.ecommerce.model.*;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CheckoutResponse checkoutFromCart(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("You didn't put anything in your cart!");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        List<CheckoutResponse.OrderItemDetails> responseItems = new ArrayList<>();
        double subtotal = 0.0;

        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            
            if (item.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("We don't have enough of " + item.getName());
            }

            item.setQuantity(item.getQuantity() - cartItem.getQuantity());
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItems.add(orderItem);

            double itemSubtotal = item.getPrice() * cartItem.getQuantity();
            subtotal += itemSubtotal;

            responseItems.add(CheckoutResponse.OrderItemDetails.builder()
                    .itemName(item.getName())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(item.getPrice())
                    .subtotal(itemSubtotal)
                    .build());
        }

        double tax = subtotal * 0.10; 
        double total = subtotal + tax;

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setItems(orderItems);
        orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return CheckoutResponse.builder()
                .orderId(order.getId())
                .items(responseItems)
                .subtotal(subtotal)
                .tax(tax)
                .total(total)
                .build();
    }

    public List<OrderHistoryResponse> getOrderHistory(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> 
            OrderHistoryResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream().map(orderItem -> 
                    OrderHistoryResponse.OrderHistoryItemDetails.builder()
                        .itemName(orderItem.getItem().getName())
                        .quantity(orderItem.getQuantity())
                        .unitPrice(orderItem.getPrice())
                        .build()
                ).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());
    }
}

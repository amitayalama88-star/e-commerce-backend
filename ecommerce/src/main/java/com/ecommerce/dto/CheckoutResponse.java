package com.ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CheckoutResponse {
    private Long orderId;
    private List<OrderItemDetails> items;
    private Double subtotal;
    private Double tax; // Let's add 10% tax for realism
    private Double total;

    @Data
    @Builder
    public static class OrderItemDetails {
        private String itemName;
        private Integer quantity;
        private Double unitPrice;
        private Double subtotal;
    }
}

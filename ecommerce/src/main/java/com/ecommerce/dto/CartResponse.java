package com.ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private List<CartItemDetails> items;
    private Double subtotal;

    @Data
    @Builder
    public static class CartItemDetails {
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private Double unitPrice;
        private Double itemSubtotal;
    }
}

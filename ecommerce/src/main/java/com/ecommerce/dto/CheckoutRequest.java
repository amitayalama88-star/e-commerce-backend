package com.ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long itemId;
        private Integer quantity;
    }
}

package com.ecommerce.service;

import com.ecommerce.dto.CartResponse;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Item;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ItemRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CartService(CartItemRepository cartItemRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public void addToCart(String username, Long itemId, int quantity) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Login first!"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        CartItem cartItem = cartItemRepository.findByUserAndItem_Id(user, itemId)
                .orElse(new CartItem(null, user, item, 0));
        
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
    }

    public CartResponse getCart(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Login first!"));
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        List<CartResponse.CartItemDetails> details = cartItems.stream().map(ci -> 
            CartResponse.CartItemDetails.builder()
                .itemId(ci.getItem().getId())
                .itemName(ci.getItem().getName())
                .quantity(ci.getQuantity())
                .unitPrice(ci.getItem().getPrice())
                .itemSubtotal(ci.getItem().getPrice() * ci.getQuantity())
                .build()
        ).collect(Collectors.toList());

        double subtotal = details.stream().mapToDouble(CartResponse.CartItemDetails::getItemSubtotal).sum();

        return CartResponse.builder()
                .items(details)
                .subtotal(subtotal)
                .build();
    }

    @Transactional
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        cartItemRepository.deleteByUser(user);
    }
}

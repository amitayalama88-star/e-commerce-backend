package com.ecommerce.repository;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndItem_Id(User user, Long itemId);
    void deleteByUser(User user);
}

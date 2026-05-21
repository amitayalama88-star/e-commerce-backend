package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    private Double totalAmount;

    @ManyToOne
//    @ManyToOne & @OneToMany:Purpose: These manage the relationships between tables.
//    Use Case: * @ManyToOne: Used in CartItem to link a cart entry to a specific User and Item.
//    @OneToMany: Used in the Order class to link one order to a list of multiple OrderItem entries.
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
//    When you use the @JoinColumn(name = "user_id") annotation, you are explicitly telling Spring Data JPA how to link two database tables together.
    private List<OrderItem> items;
}

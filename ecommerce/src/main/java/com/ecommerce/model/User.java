package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Purpose: Marks a Java class as a table in your database.
//Spring knows it needs to create (or map to) a corresponding table in the database.
@Table(name = "users")
//@Table(name = "..."):Purpose: Explicitly names the table in your database.
// Use Case: You used this to map your Java class CartItem to a specific table name like "cart items".
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
//    @Id:Purpose: Defines the Primary Key of the database table.
//    Use Case: Every object needs a unique identifier so the database can distinguish between, for example, "Item-1" and "Item-2".
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)   //you are telling Spring Boot and Hibernate to enforce a strict rule:
    // this field can never be left empty (null) in the database.
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}

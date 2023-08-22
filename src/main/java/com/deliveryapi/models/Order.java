package com.deliveryapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @ManyToMany
    @JoinTable(name = "order_products")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address shippingAddress;

    private String status;

}

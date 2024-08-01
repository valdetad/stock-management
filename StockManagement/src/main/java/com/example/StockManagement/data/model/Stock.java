package com.example.StockManagement.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="stock")

public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
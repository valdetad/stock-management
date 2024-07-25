package com.example.StockManagement.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)

    private Double price;
    private String description;
    private String barcode;
}

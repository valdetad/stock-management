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
}

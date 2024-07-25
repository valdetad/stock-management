package com.example.StockManagement.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="market")

public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

}
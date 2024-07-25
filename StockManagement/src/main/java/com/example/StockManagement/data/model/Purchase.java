package com.example.StockManagement.data.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name="purchase")

public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

}

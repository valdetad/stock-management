package com.example.StockManagement.data.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    @ManyToMany
    @JoinTable(
            name = "purchase_product",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
}

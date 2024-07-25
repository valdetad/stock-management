package com.example.StockManagement.data.dtos;

import com.example.StockManagement.data.model.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public enum PurchaseStatusCreateDto {
    private Long id;
    private PurchaseStatus status;
    private Date purchaseDate;
    private Long marketId;
    private Set<Long> productIds;
}

package com.example.StockManagement.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StockCreateDto {
    private Integer quantity;
    private Long marketId;
    private Long productId;

}

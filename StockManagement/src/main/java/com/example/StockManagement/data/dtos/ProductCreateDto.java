package com.example.StockManagement.data.dtos;

import com.example.StockManagement.data.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductCreateDto {

    private String name;
    private Category category;
    private Double price;
    private String description;
    private String barcode;

}

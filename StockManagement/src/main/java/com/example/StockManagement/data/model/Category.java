package com.example.StockManagement.data.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = Category.CategoryDeserializer.class)
public enum Category {
    VEGETABLE,
    FRUIT,
    BANANA,
    POTATO,
    CLOTHING,
    APPLE,
    GROCERIES,
    ORANGE,
    ELECTRONICS,
    TOMATO;

    public static class CategoryDeserializer extends JsonDeserializer<Category> {
        @Override
        public Category deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            String value = p.getText().toUpperCase();
            return Category.valueOf(value);
        }
    }
}

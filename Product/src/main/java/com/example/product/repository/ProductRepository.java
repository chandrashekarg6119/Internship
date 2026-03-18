package com.example.product.repository;

import com.example.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByProductPriceGreaterThan(double price);

    List<Product> findByProductColor(String color);

    long countByProductMaterial(String material);

    List<Product> findByProductPriceBetweenAndProductMaterial(double min, double max, String material);
}
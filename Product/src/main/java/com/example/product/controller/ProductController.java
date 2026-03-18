package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    // 1️⃣ Get all products
    @GetMapping
    public List<Product> getAll() {
        return repo.findAll();
    }

    // 2️⃣ Products with price > 500
    @GetMapping("/price/gt500")
    public List<Product> priceGreater() {
        return repo.findByProductPriceGreaterThan(500);
    }

    // 3️⃣ Find by color
    @GetMapping("/color/{color}")
    public List<Product> byColor(@PathVariable String color) {
        return repo.findByProductColor(color);
    }

    // 4️⃣ Count by material
    @GetMapping("/count/material/{mat}")
    public long countMaterial(@PathVariable String mat) {
        return repo.countByProductMaterial(mat);
    }

    // 5️⃣ Sort by price ascending
    @GetMapping("/sort/asc")
    public List<Product> sortAsc() {
        return repo.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getProductPrice))
                .toList();
    }

    // 6️⃣ Sort by price descending
    @GetMapping("/sort/desc")
    public List<Product> sortDesc() {
        return repo.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getProductPrice).reversed())
                .toList();
    }

    // 7️⃣ Maximum price product
    @GetMapping("/max-price")
    public Product maxPrice() {
        return repo.findAll()
                .stream()
                .max(Comparator.comparing(Product::getProductPrice))
                .orElse(null);
    }

    // 8️⃣ Minimum price product
    @GetMapping("/min-price")
    public Product minPrice() {
        return repo.findAll()
                .stream()
                .min(Comparator.comparing(Product::getProductPrice))
                .orElse(null);
    }

    // 9️⃣ Average product price
    @GetMapping("/avg-price")
    public double avgPrice() {
        return repo.findAll()
                .stream()
                .mapToDouble(Product::getProductPrice)
                .average()
                .orElse(0);
    }

    // 🔟 Price between 400 and 800 AND material = Rubber
    @GetMapping("/filter")
    public List<Product> filter() {
        return repo.findByProductPriceBetweenAndProductMaterial(400, 800, "Rubber");
    }
}
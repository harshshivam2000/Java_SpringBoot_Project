package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Integer id(int id);
}


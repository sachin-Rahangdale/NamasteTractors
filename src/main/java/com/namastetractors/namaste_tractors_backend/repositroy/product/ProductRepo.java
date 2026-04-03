package com.namastetractors.namaste_tractors_backend.repositroy.product;

import com.namastetractors.namaste_tractors_backend.entity.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long > {

}

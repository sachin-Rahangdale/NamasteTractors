package com.namastetractors.namaste_tractors_backend.entity.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.namastetractors.namaste_tractors_backend.entity.article.Article;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
}

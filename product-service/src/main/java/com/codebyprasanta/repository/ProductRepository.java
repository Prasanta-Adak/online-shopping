package com.codebyprasanta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.codebyprasanta.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}

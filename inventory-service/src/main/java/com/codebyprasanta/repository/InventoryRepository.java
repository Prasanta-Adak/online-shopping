package com.codebyprasanta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codebyprasanta.entities.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

	Optional<Inventory> findBySkuCode(String skuCode);

}
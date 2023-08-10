package com.codebyprasanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codebyprasanta.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}

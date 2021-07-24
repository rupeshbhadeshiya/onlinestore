package com.learning.ddd.onlinestore.cart.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.ddd.onlinestore.cart.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findByConsumerId(String consumerId);

}

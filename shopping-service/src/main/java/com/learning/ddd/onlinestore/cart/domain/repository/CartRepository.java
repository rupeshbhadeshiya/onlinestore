package com.learning.ddd.onlinestore.cart.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.ddd.onlinestore.cart.domain.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findByConsumerId(String consumerId);

}

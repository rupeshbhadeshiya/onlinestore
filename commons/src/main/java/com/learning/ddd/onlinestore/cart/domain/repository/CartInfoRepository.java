package com.learning.ddd.onlinestore.cart.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.ddd.onlinestore.cart.application.dto.CartInfo;

@Repository
public interface CartInfoRepository extends JpaRepository<CartInfo, Integer> {

	List<CartInfo> findByConsumerId(String consumerId);

}

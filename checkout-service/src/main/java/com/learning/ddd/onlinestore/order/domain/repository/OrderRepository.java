package com.learning.ddd.onlinestore.order.domain.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.ddd.onlinestore.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByConsumerId(String consumerId);

}

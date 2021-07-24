package com.learning.ddd.onlinestore.order.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.ddd.onlinestore.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByConsumerId(String consumerId);
	
//	@Query("FROM Order o JOIN OrderItem i "
//			+ "WHERE o.orderId = i.orderId"
//			+ " AND o.consumerId = :consumerId"
//			+ " AND "
//			+ " ( "
//			+ "   ( (i.category LIKE :searchText) OR (i.subCategory LIKE :searchText)"
//			+ "     (i.name LIKE :searchText) OR (i.subCategory LIKE :searchText) )"
//			+ "   OR "
//			+ "   (o.creationDate = :orderPlacedDate)"
//			+ " ) " )
//	Object[] findBySearchCriteria(String consumerId, String searchText, Date orderPlacedDate);

	void deleteByConsumerIdAndOrderNumber(String consumerId, String orderNumber);

}

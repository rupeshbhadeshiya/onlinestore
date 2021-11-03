package com.learning.ddd.onlinestore.order.domain.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByConsumerId(String consumerId);

	Order findByOrderNumber(String orderNumber);

	@Query(	  
			" SELECT order "
			+ "	FROM Order order "
			+ " WHERE order.orderId in "
			+ "  ( "
			+ " 	SELECT o.orderId "
			+ " 	FROM Order o, Address a, OrderItem i, OrderTransaction t "
			+ " 	WHERE o.orderNumber LIKE :orderNumber "
			+ " 		OR t.transactionNumber LIKE :transactionNumber "
			+ " 		OR o.creationDate LIKE :purchaseDate "
			+ " 		OR o.paymentMethod LIKE :paymentMethod "			
			+ " 		OR ( "
			+ " 			i.category LIKE :itemDetails "
			+ " 			OR i.subCategory LIKE :itemDetails "
			+ " 			OR i.name LIKE :itemDetails "
			+ " 		) "
			+ " 		OR ( "
			+ " 			a.line1 LIKE :addressDetails "
			+ " 			OR a.line2 LIKE :addressDetails "
			+ " 			OR a.city LIKE :addressDetails "
			+ " 			OR a.state LIKE :addressDetails "
			+ " 			OR a.postalCode LIKE :addressDetails "
			+ " 			OR a.country LIKE :addressDetails "
			+ " 		) "
//			+ " 		OR t.merchantInfo.name LIKE :merchantName "
			+ "  ) "
			)
	List<Order> findByExample(
			@Param("orderNumber") String orderNumber, 
			@Param("transactionNumber") String transactionNumber,
			@Param("purchaseDate") Date purchaseDate,
			@Param("paymentMethod") PaymentMethod paymentMethod,
			@Param("itemDetails") String itemDetails,
			@Param("addressDetails") String addressDetails);
//			,
//			@Param("merchantName") String merchantName);

	void deleteByConsumerIdAndOrderNumber(String consumerId, String orderNumber);
	
}

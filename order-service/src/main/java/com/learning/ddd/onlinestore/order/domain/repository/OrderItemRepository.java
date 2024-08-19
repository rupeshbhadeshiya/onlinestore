package com.learning.ddd.onlinestore.order.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learning.ddd.onlinestore.order.domain.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Modifying
	@Query("delete from OrderItem item where "
			+ "item.itemId=:itemId "
			+ " OR item.product.category=:category"
			+ " OR item.product.subCategory=:subCategory"
			+ " OR item.product.name=:name"
			+ " OR item.product.price=:price"
			+ " OR item.product.quantity=:quantity")
	void deleteItems(@Param("itemId") Integer itemId, 
			@Param("category") String category, @Param("subCategory") String subCategory,
			@Param("name") String name, @Param("price") Double price, @Param("quantity") Integer quantity);

}

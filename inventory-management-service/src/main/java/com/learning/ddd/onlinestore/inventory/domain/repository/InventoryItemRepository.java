package com.learning.ddd.onlinestore.inventory.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

//@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

	@Modifying
	@Query("delete from InventoryItem item where "
			+ "item.itemId=:itemId "
			+ " OR item.category=:category"
			+ " OR item.subCategory=:subCategory"
			+ " OR item.name=:name"
			+ " OR item.price=:price"
			+ " OR item.quantity=:quantity")
	void deleteItems(@Param("itemId") Integer itemId, 
			@Param("category") String category, @Param("subCategory") String subCategory,
			@Param("name") String name, @Param("price") Double price, @Param("quantity") Integer quantity);

	
	@Query("SELECT SUM(quantity) from InventoryItem")
	Integer calculateAllItemsQuantitiesTotal();

}

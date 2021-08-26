package com.learning.ddd.onlinestore.inventory.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

//@Repository
public interface InventoryItemJpaRepository extends JpaRepository<InventoryItem, Integer> {

	@Query(	  "	SELECT COUNT(*) "
			+ " FROM InventoryItem item "
			+ "	WHERE item.category=:category"
			+ " 	AND item.subCategory=:subCategory"
			+ " 	AND item.name=:name"
			+ " 	AND item.price=:price"
			+ " 	AND item.quantity=:quantity")
	Integer countByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);


	@Query(	  "	SELECT item "
			+ " FROM InventoryItem item "
			+ "	WHERE item.category LIKE :category"
			+ " 	OR item.subCategory LIKE :subCategory"
			+ " 	OR item.name LIKE :name"
			+ " 	OR item.price=:price"
			+ " 	OR item.quantity=:quantity")
	List<InventoryItem> searchItemsByExample(@Param("category") String category, 
			@Param("subCategory") String subCategory, 
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);



	@Query("SELECT SUM(quantity) from InventoryItem")
	Integer calculateAllItemsQuantitiesTotal();
	
	
	@Modifying
	@Query("DELETE FROM InventoryItem item "
			+ "WHERE item.itemId=:itemId "
			+ " OR item.category=:category"
			+ " OR item.subCategory=:subCategory"
			+ " OR item.name=:name"
			+ " OR item.price=:price"
			+ " OR item.quantity=:quantity")
	void deleteItems(@Param("itemId") Integer itemId, 
			@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);


}

package com.learning.ddd.onlinestore.inventory.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.Product;

//@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Integer> {

	@Query(	  "	SELECT inventoryItem "
			+ " FROM InventoryItem inventoryItem "
			+ "	WHERE inventoryItem.product.productId=:productId")
	InventoryItem findByProductId(@Param("productId") Integer productId);
	
	@Query(	  "	SELECT inventoryItem.product "
			+ " FROM InventoryItem inventoryItem "
			+ "	WHERE inventoryItem.available=:available")
	List<Product> findProductsBasedOnAvailability(boolean available);
	
	@Query(	  "	SELECT inventoryItem.product "
			+ " FROM InventoryItem inventoryItem "
			+ "	WHERE inventoryItem.product.category=:category"
			+ " 	AND inventoryItem.product.subCategory=:subCategory"
			+ " 	AND inventoryItem.product.name=:name")
	Product searchProductsByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name);
	
	
	@Modifying
	@Query("UPDATE InventoryItem inventoryItem "
			+ "SET inventoryItem.available=:available "
			+ "WHERE inventoryItem.product.productId=:productId")
	void updateProductForAvailability(int productId, boolean available);
	
	
	@Modifying
	@Query("UPDATE InventoryItem inventoryItem "
			+ "SET inventoryItem.available=:available "
			+ "WHERE inventoryItem.product.productId IN (:productIdList)")
	void updateListOfProductsForAvailability(Integer[] productIdList, boolean available);
	
	
	@Query(	  "SELECT SUM(inventoryItem.product.quantity) "
			+ "FROM InventoryItem inventoryItem ")	
	Integer calculateAllProductsQuantitiesTotal();
	
	
	@Query(	  "	SELECT COUNT(*) "
			+ " FROM InventoryItem inventoryItem "
			+ "	WHERE inventoryItem.product.category=:category"
			+ " 	AND inventoryItem.product.subCategory=:subCategory"
			+ " 	AND inventoryItem.product.name=:name"
			+ " 	AND inventoryItem.product.price=:price"
			+ " 	AND inventoryItem.product.quantity=:quantity")
	Integer countByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);
	
	
	@Query(	  "	SELECT inventoryItem.product "
			+ " FROM InventoryItem inventoryItem "
			+ "	WHERE inventoryItem.product.category LIKE :category"
			+ " 	OR inventoryItem.product.subCategory LIKE :subCategory"
			+ " 	OR inventoryItem.product.name LIKE :name"
			+ " 	OR inventoryItem.product.price=:price"
			+ " 	OR inventoryItem.product.quantity=:quantity")
	List<Product> searchProductsByExample(@Param("category") String category, 
			@Param("subCategory") String subCategory, 
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);

	@Modifying
	@Query(	  "DELETE "
			+ "FROM InventoryItem inventoryItem "		
			+ "WHERE inventoryItem.product.productId=:productId")
	void deleteByProductId(Integer productId);

}

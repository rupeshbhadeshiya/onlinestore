package com.learning.ddd.onlinestore.productcatalog.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.ddd.onlinestore.productcatalog.domain.Product;

public interface ProductCatalogRepository extends JpaRepository<Product, Integer> {

	@Query(	  "	SELECT COUNT(*) "
			+ " FROM Product product "
			+ "	WHERE product.category=:category"
			+ " 	AND product.subCategory=:subCategory"
			+ " 	AND product.name=:name"
			+ " 	AND product.price=:price"
			+ " 	AND product.quantity=:quantity")
	Integer countByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);
	
	@Query(	  "	SELECT product "
			+ " FROM Product product "
			+ "	WHERE product.category=:category"
			+ " 	AND product.subCategory=:subCategory"
			+ " 	AND product.name=:name")
	Product searchByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name);


	@Query(	  "	SELECT product "
			+ " FROM Product product "
			+ "	WHERE product.category LIKE :category"
			+ " 	OR product.subCategory LIKE :subCategory"
			+ " 	OR product.name LIKE :name"
			+ " 	OR product.price=:price"
			+ " 	OR product.quantity=:quantity")
	List<Product> searchProductsByExample(@Param("category") String category, 
			@Param("subCategory") String subCategory, 
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);



	@Query("SELECT SUM(quantity) from Product")
	Integer calculateAllProductsQuantitiesTotal();
	
	
	@Modifying
	@Query("DELETE FROM Product product "
			+ "WHERE product.productId=:productId "
			+ " OR product.category=:category"
			+ " OR product.subCategory=:subCategory"
			+ " OR product.name=:name"
			+ " OR product.price=:price"
			+ " OR product.quantity=:quantity")
	void deleteProducts(@Param("productId") Integer productId, 
			@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);


}

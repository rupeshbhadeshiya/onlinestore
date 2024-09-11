package com.learning.ddd.onlinestore.productcatalog.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learning.ddd.onlinestore.product.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.ProductCatalogItem;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalogItem, Integer> {

	@Query(	  "	SELECT productCatalog.product "
			+ " FROM ProductCatalogItem productCatalog")
	List<Product> findAllProducts();
	
	@Query(	  "	SELECT productCatalog.product "
			+ " FROM ProductCatalogItem productCatalog "
			+ "	WHERE productCatalog.product.productId=:productId")
	Product findByProductId(int productId);
	
	@Query(	  "	SELECT COUNT(*) "
			+ " FROM ProductCatalogItem productCatalog "
			+ "	WHERE productCatalog.product.category=:category"
			+ " 	AND productCatalog.product.subCategory=:subCategory"
			+ " 	AND productCatalog.product.name=:name"
			+ " 	AND productCatalog.product.price=:price"
			+ " 	AND productCatalog.product.quantity=:quantity")
	Integer countByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);
	
	@Query(	  "	SELECT productCatalog.product "
			+ " FROM ProductCatalogItem productCatalog "
			+ "	WHERE productCatalog.product.category=:category"
			+ " 	AND productCatalog.product.subCategory=:subCategory"
			+ " 	AND productCatalog.product.name=:name")
	Product searchProductsByUniqueFields(@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name);


	@Query(	  "	SELECT productCatalog.product "
			+ " FROM ProductCatalogItem productCatalog "
			+ "	WHERE productCatalog.product.category LIKE :category"
			+ " 	OR productCatalog.product.subCategory LIKE :subCategory"
			+ " 	OR productCatalog.product.name LIKE :name"
			+ " 	OR productCatalog.product.price=:price"
			+ " 	OR productCatalog.product.quantity=:quantity")
	List<Product> searchProductsByExample(@Param("category") String category, 
			@Param("subCategory") String subCategory, 
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);


	@Query(	  "SELECT SUM(productCatalog.product.quantity) "
			+ "FROM ProductCatalogItem productCatalog ")
	Integer calculateAllProductsQuantitiesTotal();
	
	
	@Modifying
	@Query(	  "DELETE "
			+ "FROM ProductCatalogItem productCatalog "	
			+ "WHERE productCatalog.product.productId=:productId "
			+ " OR productCatalog.product.category=:category"
			+ " OR productCatalog.product.subCategory=:subCategory"
			+ " OR productCatalog.product.name=:name"
			+ " OR productCatalog.product.price=:price"
			+ " OR productCatalog.product.quantity=:quantity")
	void deleteProducts(@Param("productId") Integer productId, 
			@Param("category") String category, 
			@Param("subCategory") String subCategory,
			@Param("name") String name, 
			@Param("price") Double price, 
			@Param("quantity") Integer quantity);

	@Modifying
	@Query(	  "DELETE "
			+ "FROM ProductCatalogItem productCatalog "	
			+ "WHERE productCatalog.product.productId=:productId")
	void deleteByProductId(Integer productId);


}

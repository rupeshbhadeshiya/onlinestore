package com.learning.ddd.onlinestore.productcatalog.domain.service;

import java.util.List;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.inventory.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.productcatalog.domain.ProductCatalogItem;
import com.learning.ddd.onlinestore.productcatalog.domain.repository.ProductCatalogRepository;

@Service
public class ProductCatalogService {

	@Autowired
	private ProductCatalogRepository productCatalogRepository;
	
	//@Autowired
	//private ProductCatalogEventsProducer productCatalogEventsProducer;
	
	
	public ProductCatalogService() {
	}

	public List<Product> getAllProducts() {
		return productCatalogRepository.findAllProducts();
	}

	@Transactional
	public Product addProduct(Product product) throws JMSException, ProductAlreadyExistsException {
		
		// imp:
		// searching Product for uniqueness have to be only by matching unique fields Category/SubCategory/name
		// if you try to search by productId then it will anyway be different for even similar product added repeatedly
		// ...
		// so, if you wish to allow adding same products again then here search by productId
		// and, if you wish to not allow adding same products again then here search by unique fields Category/SubCategory/name
		// ...
		if (searchProduct(product) != null) { 
			throw new ProductAlreadyExistsException(product);
		}
		
		// reaching here means the Product doesn't already exist in local data store

		ProductCatalogItem productCatalogItem = new ProductCatalogItem(product);
		
		final ProductCatalogItem persistedItem = productCatalogRepository.save(productCatalogItem);

//		// no events needed to be published from product-catalog as of now (19.Aug.2024)
//		ProductAddedToInventoryEvent event = new ProductAddedToInventoryEvent(persistedItem.getProduct());
//		productCatalogEventsProducer.publishDomainEvent(event);
		
		return persistedItem.getProduct();
	}
	
	public Product getProduct(final int productId) {
		
		return productCatalogRepository.findByProductId(productId);
	}

	public Integer getProductQuantitiesTotal() {
		
		Integer allProductsQuantitiesTotal = productCatalogRepository.calculateAllProductsQuantitiesTotal();
		return allProductsQuantitiesTotal != null ? allProductsQuantitiesTotal : 0;
	}
	
	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public Product searchProduct(Product product) {
		
		return productCatalogRepository.searchProductsByUniqueFields(
			product.getCategory(), product.getSubCategory(), product.getName()
		);
	}

	public List<Product> searchProductsByExample(Product exampleProduct) {
		
		return productCatalogRepository.searchProductsByExample(exampleProduct.getCategory(),
			exampleProduct.getSubCategory(), exampleProduct.getName(),
			exampleProduct.getPrice(), exampleProduct.getQuantity()
		);
	}

	@Transactional
	public void removeProduct(Integer productId) {
		productCatalogRepository.deleteByProductId(productId);
	}

}

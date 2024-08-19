package com.learning.ddd.onlinestore.inventory.domain;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.commons.util.CommonUtil;
import com.learning.ddd.onlinestore.inventory.domain.event.ProductAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ProductRemovedFromInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ProductsAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.pubsub.InventoryEventsProducer;
import com.learning.ddd.onlinestore.inventory.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.inventory.domain.repository.InventoryRepository;

//What an Inventory can have and should do?
//1: Inventory contains lot of items, basically lot of Products
//2: Mart team can add batches of already supported items
//3: Mart team can add new set of items (if management decided to add new products)
//4: Mart team can remove existing items (say some problem found in some items or remove due to some problem)
//5: Mart team/Management may want to review Inventory (how many items in stock for each supported product?)
//6: Mart team/Management may want to search some/many specific item/product)
//			get a specific item by itemId
//			search item(s) by any combination of parameters of Item
@Component
public class Inventory {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private InventoryEventsProducer inventoryEventsProducer;
	
	//@Autowired
	//private DomainEventPublisher domainEventPublisher;

	
	public Inventory() {
		
	}
	
	
	public List<Product> getAvailableProducts() {
		boolean available = true;
		return inventoryRepository.findProductsBasedOnAvailability(available);
	}

	@Transactional
	public Product addProduct(Product product) throws ProductAlreadyExistsException, JMSException {
		
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

		InventoryItem inventoryItem = new InventoryItem(product);
		
		// let's assign some unique id to Product which will be later used 
		// to share this Product as unique product among all microservices 
		inventoryItem.getProduct().setProductId(CommonUtil.generateUUID());

		// also, let's mark that this product is now available for shopping
		inventoryItem.setAvailable(true); 
		
		final InventoryItem persistedItem = inventoryRepository.save(inventoryItem);
		
		ProductAddedToInventoryEvent event = new ProductAddedToInventoryEvent(persistedItem.getProduct());
		inventoryEventsProducer.publishDomainEvent(event);
		
		return persistedItem.getProduct();
	}
	
	public Product getProduct(final int productId) {
		
		InventoryItem itemFromDB = inventoryRepository.findByProductId(productId);
		//System.out.println("-------------> getItem(): itemFromDB = " + itemFromDB);
		
		return ((itemFromDB != null) ? itemFromDB.getProduct() : null);
	}

	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public Product searchProduct(Product product) {
		
		return inventoryRepository.searchProductsByUniqueFields(
			product.getCategory(), product.getSubCategory(), product.getName()
		);
	}
	
	// update one Product as now not available for shopping
	@Transactional
	public void updateProductAsNotAvailableForShopping(int productId) throws JMSException {
		
		// update Product as not available
		inventoryRepository.updateProductForAvailability(productId, false);
		
		// publish domain event so other services like Product-Catalog also update on their side
		ProductRemovedFromInventoryEvent event = new ProductRemovedFromInventoryEvent(
			inventoryRepository.findByProductId(productId).getProduct()
		);
		inventoryEventsProducer.publishDomainEvent(event);		
	}
	
	// update one Product as now available for shopping
	@Transactional
	public void updateProductAsAvailableForShopping(int productId) throws JMSException {
		
		// update Product as available
		inventoryRepository.updateProductForAvailability(productId, true);
		
		// publish domain event so other services like Product-Catalog also update on their side
		ProductAddedToInventoryEvent event = new ProductAddedToInventoryEvent(
			inventoryRepository.findByProductId(productId).getProduct()
		);
		inventoryEventsProducer.publishDomainEvent(event);
	}
	
	// update many Products as now available for shopping
	// this use case happens only when either a Cart is emptied or an Order is cancelled
	@Transactional
	public void updateListOfProductsAsAvailableForShopping(List<Product> products) throws JMSException {
		
		// update Product as available
		
		List<Integer> productIdList = new ArrayList<Integer>();
		
		for (Product product : products) {
			productIdList.add(product.getProductId());
		}
		
		inventoryRepository.updateListOfProductsForAvailability(
			productIdList.toArray(new Integer[0]), 
			true
		);
		
		// publish domain event so other services like Product-Catalog also update on their side
		ProductsAddedToInventoryEvent event = new ProductsAddedToInventoryEvent(products);
		inventoryEventsProducer.publishDomainEvent(event);
	}
	
	@Transactional
	public Product updateProduct(Product product) {
		
		InventoryItem item = getInventoryItemByProductId(product.getProductId());
		item.setProduct(product);
		
		return inventoryRepository.save(item).getProduct();
	}
	
	@Transactional
	public void removeProduct(Integer productId) throws JMSException {
		
		Product productToBeRemoved = inventoryRepository.findByProductId(productId).getProduct();
		
		// remove the Product
		inventoryRepository.deleteByProductId(productId);
		
		// publish domain event so other services like Product-Catalog also update on their side
		ProductRemovedFromInventoryEvent event = 
				new ProductRemovedFromInventoryEvent(productToBeRemoved);
		inventoryEventsProducer.publishDomainEvent(event);		
	}
	
	
	public InventoryItem getInventoryItemByProductId(int productId) {
		
		InventoryItem itemFromDB = inventoryRepository.findByProductId(productId);
		//System.out.println("-------------> getItemByProductId(): itemFromDB = " + itemFromDB);
		
		return itemFromDB;
	}
	
	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public List<Product> searchProductsByExample(Product exampleProduct) {
		
		return inventoryRepository.searchProductsByExample(exampleProduct.getCategory(),
			exampleProduct.getSubCategory(), exampleProduct.getName(),
			exampleProduct.getPrice(), exampleProduct.getQuantity()
		);
	}
		
//		return itemRepository.findAll(Example.of(exampleItem));
		
//		return getItems().stream()
//				.filter(item ->
//							item.getItemId()==exampleItem.getItemId() 
//							|| item.getCategory().equals(exampleItem.getCategory())
//							|| item.getSubCategory().equals(exampleItem.getSubCategory())
//							|| item.getName().equals(exampleItem.getName())
//							|| item.getPrice().equals(exampleItem.getPrice())
//							|| item.getQuantity()==exampleItem.getQuantity()
//						)
//				.collect(Collectors.toList());

	
	public int getProductQuantitiesTotal() {
		
		Integer allProductsQuantitiesTotal = inventoryRepository.calculateAllProductsQuantitiesTotal();
		return allProductsQuantitiesTotal != null ? allProductsQuantitiesTotal : 0;
	}


}

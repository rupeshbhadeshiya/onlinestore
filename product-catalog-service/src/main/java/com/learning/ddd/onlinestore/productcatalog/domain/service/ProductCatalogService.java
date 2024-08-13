package com.learning.ddd.onlinestore.productcatalog.domain.service;

import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.productcatalog.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.productcatalog.domain.repository.ProductCatalogRepository;

@Service
public class ProductCatalogService {

	@Autowired
	private ProductCatalogRepository productcatalogRepository;
	
	//@Autowired
	//private CartEventsProducer cartEventsProducer;
	
	//@Autowired
	//private DomainEventPublisher domainEventPublisher;
	
	
	public ProductCatalogService() {
	}
	
	@Transactional
	public Product addProduct(Product product) throws ProductAlreadyExistsException, JMSException {
		
		if (searchProduct(product) != null) {
			throw new ProductAlreadyExistsException(product);
		}
		
		final Product persistedProduct = productcatalogRepository.save(product);
		
		
		//ItemAddedToInventoryEventData eventData = new ItemAddedToInventoryEventData(persistedProduct);
		//DomainEvent itemAddedToInventoryEvent = new DomainEvent(DomainEventName.ITEM_ADDED_TO_INVENTORY, eventData);
		// inventoryEventsProducer.publishDomainEvent(itemAddedToInventoryEvent);
		
		return persistedProduct;
	}
	
//	@Transactional
//	public List<InventoryItem> addItems(List<InventoryItem> itemsToBeAdded) throws ItemsAlreadyExistsException {
//		
//		List<InventoryItem> items = new ArrayList<InventoryItem>();
//		List<InventoryItem> alreadyExistingItems = new ArrayList<InventoryItem>();
//		
//		for (InventoryItem item : itemsToBeAdded) {
//			Integer countByUniqueFields = productcatalogRepository.countByUniqueFields(
//					item.getCategory(), item.getSubCategory(), 
//					item.getName(), item.getPrice(), item.getQuantity());
//			if (countByUniqueFields > 0) {	
//				// item exists in Inventory
//				alreadyExistingItems.add(item);
//			} else { //if (countByUniqueFields == 0)
//				// new item
//				items.add(item);
//			}
//		}
//		
//		if (!alreadyExistingItems.isEmpty() ) {
//			throw new ItemsAlreadyExistsException(alreadyExistingItems);
//		}
//		
//		if (!items.isEmpty() ) {
//			items = productcatalogRepository.saveAll(items);
//			domainEventPublisher.publishEvent(new ItemsAddedToInventoryEvent(items));
//		}
//		
//		return items;
//	}

	public List<Product> getProducts() {
		
		return productcatalogRepository.findAll();
		
	}
	
	public Product getProduct(final int productId) {
		
		Optional<Product> productFromDB = productcatalogRepository.findById(productId);
		//System.out.println("-------------> itemFromDB = " + itemFromDB);
		
		return ((productFromDB != null) && productFromDB.isPresent()) ? productFromDB.get() : null;
	}
	
	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public Product searchProduct(Product product) {
		
		return productcatalogRepository.searchByUniqueFields(
			product.getCategory(), product.getSubCategory(), product.getName()
		);
	}
	
	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public List<Product> searchProducts(Product exampleProduct) {
		
		return productcatalogRepository.searchProductsByExample(exampleProduct.getCategory(),
			exampleProduct.getSubCategory(), exampleProduct.getName(),
			exampleProduct.getPrice(), exampleProduct.getQuantity()
		);
		
//		return productcatalogRepository.findAll(Example.of(exampleItem));
		
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
		
	}
	
	@Transactional
	public Product updateProduct(Product productToUpdate) {
		
		return productcatalogRepository.save(productToUpdate);
	}
	
	@Transactional
	public void removeProduct(Integer productId) throws CloneNotSupportedException, JMSException {
		
		//Product copyOfProductToBeRemoved = getProduct(productId).clone();
		
		productcatalogRepository.deleteById(productId);
		
		//ItemRemovedFromInventoryEventData eventData = new ItemRemovedFromInventoryEventData(copyOfProductToBeRemoved);
		//DomainEvent itemRemovedFromInventoryEvent = new DomainEvent(DomainEventName.ITEM_REMOVED_FROM_INVENTORY, eventData);
		// inventoryEventsProducer.publishDomainEvent(itemRemovedFromInventoryEvent);
	}

	@Transactional
	public void removeProduct(Product product) throws CloneNotSupportedException, JMSException {
		
		//Product copyOfItemToBeRemoved = product.clone();
		
		productcatalogRepository.delete(product);
		
		//ItemRemovedFromInventoryEventData eventData = new ItemRemovedFromInventoryEventData(copyOfItemToBeRemoved);
		//DomainEvent itemRemovedFromInventoryEvent = new DomainEvent(DomainEventName.ITEM_REMOVED_FROM_INVENTORY, eventData);
		// inventoryEventsProducer.publishDomainEvent(itemRemovedFromInventoryEvent);
	}

	@Transactional
	public void removeProducts(Product exampleProduct) {
		
		productcatalogRepository.deleteProducts(
				exampleProduct.getProductId(), exampleProduct.getCategory(), exampleProduct.getSubCategory(),
				exampleProduct.getName(), exampleProduct.getPrice(), exampleProduct.getQuantity());
		
//		List<Item> items = getItems();
//		boolean anyItemRemoved = 
//				items.removeIf(item ->
//					item.getItemId()==exampleItem.getItemId() 
//					|| item.getCategory().equals(exampleItem.getCategory())
//					|| item.getSubCategory().equals(exampleItem.getSubCategory())
//					|| item.getName().equals(exampleItem.getName())
//					|| item.getPrice().equals(exampleItem.getPrice())
//					|| item.getQuantity()==exampleItem.getQuantity()
//				);
//		productcatalogRepository.saveAll(items);		
//		domainEventPublisher.publishEvent(new ItemsRemovedFromInventoryEvent(exampleItem));
//		return anyItemRemoved;
	}

	public int getItemsQuantitiesTotal() {
		
		Integer allItemsQuantitiesTotal = productcatalogRepository.calculateAllProductsQuantitiesTotal();
		return allItemsQuantitiesTotal != null ? allItemsQuantitiesTotal : 0;
	}

}

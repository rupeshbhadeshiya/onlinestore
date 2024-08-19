package com.learning.ddd.onlinestore.productcatalog.domain.event.pubsub;

import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.inventory.domain.event.ProductAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ProductRemovedFromInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ProductsAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.exception.ProductAlreadyExistsException;
import com.learning.ddd.onlinestore.productcatalog.domain.service.ProductCatalogService;

//@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class InventoryEventsConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [product-catalog-service] " + InventoryEventsConsumer.class.getSimpleName();
	
	@Value("${onlinestore.inventory.events.topic.name:InventoryEventsTopic}")
	private String topicName;
	
	@Autowired
	private ProductCatalogService productCatalogService;
	
//	@Autowired
//	private ProductCatalogRepository productcatalogRepository;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[product-catalog-service] " + InventoryEventsConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, JMSException, ProductAlreadyExistsException {

		// Process "One Product added to Inventory" event
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCT_ADDED_TO_INVENTORY)) {
			
			ProductAddedToInventoryEvent productAddedToInventoryEvent = (ProductAddedToInventoryEvent) domainEvent;
			
			Product product = productAddedToInventoryEvent.getProduct();
			
			// ... persist the Product to ProductCatalogItem table ...
			productCatalogService.addProduct(product);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Persisted Product(s) to local data store - "
				+ "product(s) = " + product
				+ ", event is due to one or more Products have been added to the Inventory");
		
			
			
		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCTS_ADDED_TO_INVENTORY)) {
			
			// Process "Many Products added to Inventory" event (which happens only in Cart emptying or Order canceling)
			
			ProductsAddedToInventoryEvent productsAddedToInventoryEvent = (ProductsAddedToInventoryEvent) domainEvent;
			
			List<Product> products = productsAddedToInventoryEvent.getProducts();

			for (Product product : products) {
				
				// add Product one by one to the local data store
				
				productCatalogService.addProduct(product);
				
				System.out.println(
					SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
					+ ", Persisted Product(s) to local data store - "
					+ "product(s) = " + product
					+ ", event is due to one or more Products have been added to the Inventory"
				);			
			}
			
			
		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCT_REMOVED_FROM_INVENTORY)) {

			// Process One "Product removed from Inventory" event
			
			ProductRemovedFromInventoryEvent productRemovedFromInventoryEvent = (ProductRemovedFromInventoryEvent) domainEvent;
			
			Product product = productRemovedFromInventoryEvent.getProduct();
			
			// ... delete the Product from ProductCatalogItem table ...
			productCatalogService.removeProduct(product.getProductId());
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Removed Product(s) from local data store - "
				+ "product(s) = " + product
				+ ", event is due to one or more Products have been removed from the Inventory");
			
		}
		
	}
	
//	@Override
//	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException {
//
//		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.ITEM_ADDED_TO_INVENTORY)) {
//			
//			ItemAddedToInventoryEventData eventData = (ItemAddedToInventoryEventData) domainEvent.getDomainEventData();
//			
//			Product product = ItemConversionUtil.fromInventoryItemToProduct(eventData.getItem());
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + OnlinestoreDomainEventName.ITEM_ADDED_TO_INVENTORY.name()
//				+ ", Going to persist Product to local data store - "
//				+ "product = " + product
//				+ ", event is due to a Product has been added to the Inventory");
//
//			// save the Product (InventoryItem) to the ProductCatalog table
//			productcatalogRepository.save(product);
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + OnlinestoreDomainEventName.ITEM_ADDED_TO_INVENTORY.name()
//				+ ", Persisted Product to local data store - "
//				+ "product = " + product
//				+ ", event is due to a Product has been added to the Inventory");
//		
//			
//		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.ITEM_REMOVED_FROM_INVENTORY)) {
//
//			ItemRemovedFromInventoryEventData eventData = (ItemRemovedFromInventoryEventData) domainEvent.getDomainEventData();
//			
//			Product product = ItemConversionUtil.fromInventoryItemToProduct(eventData.getItem());
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + OnlinestoreDomainEventName.ITEM_REMOVED_FROM_INVENTORY.name()
//				+ ", Going to remove Product from local data store - "
//				+ "product = " + product
//				+ ", event is due to a Product has been removed from the Inventory");
//			
//			// remove the Product (InventoryItem) from the ProductCatalog table
//			productcatalogRepository.delete(product);
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + OnlinestoreDomainEventName.ITEM_REMOVED_FROM_INVENTORY.name()
//				+ ", Removed Product from local data store - "
//				+ "product = " + product
//				+ ", event is due to a Product has been removed from the Inventory");
//			
//		}
//		
//	}
		
	
}


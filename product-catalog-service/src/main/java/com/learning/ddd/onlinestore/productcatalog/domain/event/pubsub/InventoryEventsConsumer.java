package com.learning.ddd.onlinestore.productcatalog.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemAddedToInventoryEventData;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemRemovedFromInventoryEventData;
import com.learning.ddd.onlinestore.productcatalog.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.repository.ProductCatalogRepository;

//@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class InventoryEventsConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [product-catalog-service] " + InventoryEventsConsumer.class.getSimpleName();
	
	@Value("${onlinestore.inventory.events.topic.name:InventoryEventsTopic}")
	private String topicName;
	
	@Autowired
	private ProductCatalogRepository productcatalogRepository;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[product-catalog-service] " + InventoryEventsConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(DomainEvent domainEvent) throws CloneNotSupportedException {

		if (domainEvent.getEventName().equals(DomainEventName.ITEM_ADDED_TO_INVENTORY)) {
			
			ItemAddedToInventoryEventData eventData = (ItemAddedToInventoryEventData) domainEvent.getEventData();
			
			Product product = ItemConversionUtil.fromInventoryItemToProduct(eventData.getItem());
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_ADDED_TO_INVENTORY.name()
				+ ", Going to persist Product to local data store - "
				+ "product = " + product
				+ ", event is due to a Product has been added to the Inventory");
			
			productcatalogRepository.save(product);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_ADDED_TO_INVENTORY.name()
				+ ", Persisted Product to local data store - "
				+ "product = " + product
				+ ", event is due to a Product has been added to the Inventory");
		
			
		} else if (domainEvent.getEventName().equals(DomainEventName.ITEM_REMOVED_FROM_INVENTORY)) {

			ItemRemovedFromInventoryEventData eventData = (ItemRemovedFromInventoryEventData) domainEvent.getEventData();
			
			Product product = ItemConversionUtil.fromInventoryItemToProduct(eventData.getItem());
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_REMOVED_FROM_INVENTORY.name()
				+ ", Going to remove Product from local data store - "
				+ "product = " + product
				+ ", event is due to a Product has been removed from the Inventory");
			
			productcatalogRepository.delete(product);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_REMOVED_FROM_INVENTORY.name()
				+ ", Removed Product from local data store - "
				+ "product = " + product
				+ ", event is due to a Product has been removed from the Inventory");
			
		}
		
	}
		
	
}


package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ProductAddedToCartEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ProductRemovedFromCartEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.Product;

@Component
public class CartEventsConsumer extends DomainEventsConsumer {
	
	public static final String SERVICE_COMPONENT = "==== [inventory-service] " + CartEventsConsumer.class.getSimpleName();
	
	@Autowired
	private Inventory inventory;
	
	@Value("${onlinestore.cart.events.topic.name:CartEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[inventory-service] " + CartEventsConsumer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, JMSException {

		// FIXME Inefficient code - figure out if there is any way
		//			by which Inventory and Cart can use same Item class?
		//			+ can Order also use same Item class?
		//
		// FIXME Why we need 3 different classes carrying same data?
		//			-> InventoryItem, CartItem
		
		
		// [ Cases for which items needed to be REMOVED from Inventory ] //
		//	1. Items shopped to a Cart
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCT_ADDED_TO_CART)) {
			
			ProductAddedToCartEvent event = (ProductAddedToCartEvent) domainEvent;
			
			Product product = event.getProduct();
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Going to update Product in Inventory as now not available for shopping"
				+ ", product = " + product
			);
			
			inventory.updateProductAsNotAvailableForShopping(product.getProductId());
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Updated Product in Inventory as now not available for shopping"
				+ ", product = " + product
			);
			
		
		// [ Cases for which items needed to be ADDED back to Inventory ] //
		//	1. A Item is removed from a Cart
		//	2. A Cart is emptied
		
		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCT_REMOVED_FROM_CART)) {

			ProductRemovedFromCartEvent event = (ProductRemovedFromCartEvent) domainEvent;
			
			Product product = event.getProduct();
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Going to update Product in Inventory as now available for shopping"
				+ ", product = " + product
			);
			
			inventory.updateProductAsAvailableForShopping(product.getProductId());
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Updated Product in Inventory as now available for shopping"
				+ ", product = " + product
			);
			
		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER)) {
			
			CartEmptiedEvent event = (CartEmptiedEvent) domainEvent;
			
			List<Product> products = event.getProducts();
			
			for (Product product : products) {
				
				System.out.println(
					SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
					+ ", Going to update Product in Inventory as now available for shopping"
					+ ", product = " + product
				);
			
				inventory.updateProductAsAvailableForShopping(product.getProductId());
				
				System.out.println(
					SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
					+ ", Updated Product in Inventory as now available for shopping"
					+ ", product = " + product
				);
				
			}
			
		}
		
	}

}

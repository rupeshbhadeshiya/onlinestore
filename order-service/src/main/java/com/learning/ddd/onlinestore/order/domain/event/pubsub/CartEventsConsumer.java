package com.learning.ddd.onlinestore.order.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemAddedToCartEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemRemovedFromCartEventData;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;

@Component
public class CartEventsConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [order-service] " + CartEventsConsumer.class.getSimpleName();
	
	@Autowired
	private CartRepository cartRepository;
	
	
	@Value("${onlinestore.cart.events.topic.name:CartEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[order-service] " + CartEventsConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(DomainEvent domainEvent) throws CloneNotSupportedException {

		if (domainEvent.getEventName().equals(DomainEventName.ITEM_ADDED_TO_CART)) {
			
			ItemAddedToCartEventData eventData = (ItemAddedToCartEventData) domainEvent.getEventData();
			
			Cart cart = eventData.getCart();
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_ADDED_TO_CART.name()
				+ ", Going to Create/Update Cart to local data store - "
				+ "cart = " + cart
				+ ", event is due to an item has been added to a Cart = " + eventData.getItem());
			
			cartRepository.save(cart);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_ADDED_TO_CART.name()
				+ ", Created/Updated Cart to local data store - "
				+ "cart = " + cart
				+ ", event is due to an item has been added to a Cart = " + eventData.getItem());
		}
		
		else if (domainEvent.getEventName().equals(DomainEventName.ITEM_REMOVED_FROM_CART)) {

			ItemRemovedFromCartEventData eventData = (ItemRemovedFromCartEventData) domainEvent.getEventData();
			
			Cart cart = eventData.getCart();
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_REMOVED_FROM_CART.name()
				+ ", Going to Create/Update Cart to local data store - "
				+ "cart = " + cart
				+ ", event is due to an item has been removed from a Cart = " + eventData.getItem());
			
			cartRepository.save(cart);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_REMOVED_FROM_CART.name()
				+ ", Created/Updated Cart to local data store - "
				+ "cart = " + cart
				+ ", event is due to an item has been removed from a Cart = " + eventData.getItem());
		}
		
		else if (domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION) || 
				domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED_BY_CONSUMER)) {
			
			CartEmptiedEventData cartEmptiedEventData = (CartEmptiedEventData) domainEvent.getEventData();
			
			Cart cart = cartEmptiedEventData.getCart();
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION.name()
				+ ", Going to delete Cart from local data store "
				+ ", event is due to a Cart is emptied by Consumer or due to Order creation "
				+ ", cart = " + cart);
			
			cartRepository.delete(cart);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + DomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION.name()
				+ ", Deleted a Cart from local data store "
				+ ", event is due to a Cart is emptied by Consumer or due to Order creation "
				+ ", cart = " + cart);
			
		} // else-if
		
	}

}

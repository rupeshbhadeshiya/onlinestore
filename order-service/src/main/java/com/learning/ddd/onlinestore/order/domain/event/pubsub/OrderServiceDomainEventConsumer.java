package com.learning.ddd.onlinestore.order.domain.event.pubsub;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.event.ItemsAddedToCartEvent.ItemsAddedToCartEventData;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;
import com.learning.ddd.onlinestore.order.domain.service.OrderService;

@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class OrderServiceDomainEventConsumer implements DomainEventConsumer {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderService orderService;
	
	@Override
	public void consumeEvent(DomainEvent domainEvent) {

		System.out.println("==== OrderServiceDomainEventConsumer "
			+ "- processing event - " + domainEvent);
		
		if (domainEvent.getEventName().equals(DomainEventName.ITEMS_ADDED_TO_CART)) {
			
			
			ItemsAddedToCartEventData cartItemsAddedEventData = (ItemsAddedToCartEventData) domainEvent.getEventData();
			
			Cart cart = cartItemsAddedEventData.getCart();
			
			//cartRepository.save(cart);
			
			System.out.println("==== OrderServiceDomainEventConsumer - "
					+ "Event: " + DomainEventName.ITEMS_ADDED_TO_CART.name()
					+ ", Created/Updated Cart to local data store - "
					+ "cart = " + cart
					+ ", items added = " + cartItemsAddedEventData.getItems());
		}
		
	}
	
	
}

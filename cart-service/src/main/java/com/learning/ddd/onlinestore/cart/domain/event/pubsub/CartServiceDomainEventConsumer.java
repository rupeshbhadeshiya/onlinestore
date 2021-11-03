package com.learning.ddd.onlinestore.cart.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEvent;

@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class CartServiceDomainEventConsumer implements DomainEventConsumer {

	@Autowired
	private CartService cartService;
	
	@Override
	public void consumeEvent(DomainEvent domainEvent) {

		System.out.println("==== CartServiceDomainEventConsumer "
			+ "- processing event - " + domainEvent);
		
		// [ Cases for which items needed to be REMOVED from Cart ] //
		//	1. Items shopped to a Cart
		
		if (domainEvent.getEventName().equals(DomainEventName.ORDER_CREATED)) {
			OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) domainEvent;
			Order order = orderCreatedEvent.getOrder();
			Cart cart = orderCreatedEvent.getCart();
			try {
				
				// be mindful to call internal empty call so CartEmptiedEvent is not 
				// published, because then inventory would claim items which 
				// it should not do when an Order is created! 
				cartService.emptyCartWithoutPublishingEvent(cart.getCartId());
				
				System.out.println("==== CartServiceDomainEventConsumer - "
						+ "Due to Order Created - EMPTIED Cart - "
						+ "cart=" + cart + ", order=" + order);
			} catch (Exception e) {
				System.err.println("==== CartServiceDomainEventConsumer - "
						+ "Exception while empting Cart! "
						+ "cart=" + cart + ", order=" + order);
				e.printStackTrace();
			}
		}
		
		// [ Cases for which items needed to be ADDED back to Cart ] //
		//	1. ?
		
		// else if 
			
	}
	
	
}

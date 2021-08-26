package com.learning.ddd.onlinestore.webapp.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEvent;
import com.learning.ddd.onlinestore.utils.SessionLikeInMemoryStore;

@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class WebAppDomainEventConsumer implements DomainEventConsumer {

	@Autowired
	private SessionLikeInMemoryStore session;
	
	@Override
	public void consumeEvent(DomainEvent domainEvent) {

		System.out.println("==== WebAppDomainEventConsumer "
			+ "- processing event - " + domainEvent);
		
		// when a cart is successfully checked out as an Order, 
		// so remove references of the cartId so it does not point to a Cart which 
		// no more exists
		if (domainEvent.getEventName().equals(DomainEventName.ORDER_CREATED)) {
			OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) domainEvent;
			Order order = orderCreatedEvent.getOrder();
			Cart cart = orderCreatedEvent.getCart();
			
			if (session.getAttribute("cartId").equals(cart.getCartId())) {
				session.removeAttribute("cartId");
				System.out.println("==== WebAppDomainEventConsumer - "
					+ "Due to Order Created - REMOVED cartId=" 
						+ cart.getCartId());
			}
			
		}
		
		
		// if a Consumer emptied cart (say don't want to shop cart items)
		// so remove references of the cartId so it does not point to a Cart which 
		// no more exists
		else if (domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED)) {
			
			CartEmptiedEvent cartEmptiedEvent = (CartEmptiedEvent) domainEvent;
			
			if (session.getAttribute("cartId").equals(cartEmptiedEvent.getCartId())) {
				session.removeAttribute("cartId");
				System.out.println("==== WebAppDomainEventConsumer - "
					+ "Due to Cart Emptied - REMOVED cartId=" 
						+ cartEmptiedEvent.getCartId());
			}
			
		}
		
		// [ Cases for which items needed to be ADDED back to Cart ] //
		//	1. ?
		
		// else if 
			
	}
	
	
}


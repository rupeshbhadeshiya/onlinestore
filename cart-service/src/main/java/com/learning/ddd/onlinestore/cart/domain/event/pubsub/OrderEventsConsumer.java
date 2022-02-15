package com.learning.ddd.onlinestore.cart.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEventData;

@Component
public class OrderEventsConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [cart-service] " + OrderEventsConsumer.class.getSimpleName();
	
	@Autowired
	private CartService cartService;
	
	
	@Value("${onlinestore.order.events.topic.name:OrderEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[cart-service] " + OrderEventsConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	@Override
	protected void consumeDomainEvent(DomainEvent domainEvent) throws CloneNotSupportedException {

		// [ Cases for which items needed to be REMOVED from Cart ] //
		//	1. Items shopped to a Cart
		
		if (domainEvent.getEventName().equals(DomainEventName.ORDER_CREATED)) {
			
			Cart cart = ((OrderCreatedEventData) domainEvent.getEventData()).getCart();
			try {
				
				// be mindful to call internal empty call so CartEmptiedEvent is not 
				// published, because then inventory would claim items which 
				// it should not do when an Order is created! 
				
				//cartService.emptyCartWithoutPublishingEvent(cart.getCartId());
				cartService.emptyCart(cart.getCartId(), DomainEventName.ORDER_CREATED);
				
				System.out.println(
					SERVICE_COMPONENT + " - Event: " + DomainEventName.ORDER_CREATED.name()
					+ ", Emptied Cart as Order successfully Created "
					//+ ", order = " + order
					+ ", cart = " + cart);
				
			} catch (Exception e) {

				System.err.println(
					SERVICE_COMPONENT + " - Event: " + DomainEventName.ORDER_CREATED.name()
					+ ", Error while emptying Cart as Order successfully Created "
					//+ ", order = " + order
					+ ", cart = " + cart);
				
				e.printStackTrace();
			}
		}
		
		// [ Cases for which items needed to be ADDED back to Cart ] //
		//	1. ?
		
		// else if 
		
	}

}

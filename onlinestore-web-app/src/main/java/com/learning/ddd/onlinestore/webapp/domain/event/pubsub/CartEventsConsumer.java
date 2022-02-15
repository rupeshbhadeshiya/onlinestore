package com.learning.ddd.onlinestore.webapp.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEventData;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.utils.SessionLikeInMemoryStore;

//@Primary // choose this one from all implementations of DomainEventProcessor
@Component
public class CartEventsConsumer extends DomainEventsConsumer {

	public static final String SERVICE_COMPONENT = "==== [onlinestore-web-app] " + CartEventsConsumer.class.getSimpleName();
	
	@Autowired
	private SessionLikeInMemoryStore session;

	
	@Value("${onlinestore.cart.events.topic.name:CartEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[onlinestore-web-app] " + CartEventsConsumer.class.getSimpleName();
	}
	
	
	@Override
	protected String getTopicName() {
		return topicName;
	}
	
	
	@Override
	protected void consumeDomainEvent(DomainEvent domainEvent) throws CloneNotSupportedException, JMSException {
		
		System.out.println(
			SERVICE_COMPONENT + " - Event: " + DomainEventName.ITEM_ADDED_TO_CART.name()
			+ "- processing event - " + domainEvent
		);
		
		// when a cart is successfully checked out as an Order, 
		// so remove references of the cartId so it does not point to a Cart which 
		// no more exists
//		if (domainEvent.getEventName().equals(DomainEventName.ORDER_CREATED)) {
//			OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) domainEvent;
//			Order order = orderCreatedEvent.getOrder();
//			Cart cart = orderCreatedEvent.getCart();
//			
//			if (session.getAttribute("cartId").equals(cart.getCartId())) {
//				session.removeAttribute("cartId");
//				System.out.println("==== WebAppDomainEventConsumer - "
//					+ "Due to Order Created - REMOVED cartId=" 
//						+ cart.getCartId());
//			}
//			
//		}
		
		
		// if a Consumer emptied cart (say don't want to shop cart items)
		// so remove references of the cartId so it does not point to a Cart which 
		// no more exists
		//else
		
		if (
			domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED_BY_CONSUMER)
			|| domainEvent.getEventName().equals(DomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION) ) {
			
			CartEmptiedEventData cartEmptiedEventData = (CartEmptiedEventData) domainEvent.getEventData();
			
			if (session.getAttribute("cartId").equals(cartEmptiedEventData.getCart().getCartId())) {
				
				session.removeAttribute("cartId");
				
				System.out.println(SERVICE_COMPONENT + " - Due to Cart Emptied by Consumer or due to Order creation -> "
					+ "Removed cartId from Session = " + cartEmptiedEventData.getCart().getCartId() );
			}
			
		}
		
		// [ Cases for which items needed to be ADDED back to Cart ] //
		//	1. ?
		
		// else if 
			
	}
	
	
}


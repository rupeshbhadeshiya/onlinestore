package com.learning.ddd.onlinestore.webapp.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
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
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, JMSException {
		
		// if a Consumer emptied cart (say don't want to shop cart items)
		// so remove references of the cartId so it does not point to a Cart which 
		// no more exists
		//else
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER)
			|| domainEvent.getEventName().equals(OnlinestoreDomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION) ) {
			
			CartEmptiedEvent event = (CartEmptiedEvent) domainEvent;
			
			Integer cartId = (Integer) event.getCartId();
			
			if ( (session.getAttribute("cartId") != null) && session.getAttribute("cartId").equals(cartId) ) {
				
				session.removeAttribute("cartId");
				
				System.out.println(
					SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName() 
					+ ", CartId = " + cartId
					+ ", Since Cart is delete, removed the cartId from in-memory Session"
				);
			}
			
		}
		
		// [ Cases for which items needed to be ADDED back to Cart ] //
		//	1. ?
		
		// else if 
			
	}
	
	
}


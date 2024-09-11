package com.learning.ddd.onlinestore.cart.domain.event.pubsub;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsConsumer;
import com.learning.ddd.onlinestore.order.domain.event.OrderConfirmedEvent;

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
	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException, CartNotFoundException, JMSException {

		// [ Cases for which items needed to be REMOVED from Cart ] //
		//	1. Items shopped to a Cart
		
		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.ORDER_CONFIRMED)) {
			
			OrderConfirmedEvent orderConfirmedEvent = (OrderConfirmedEvent) domainEvent;
			
			cartService.emptyCart(
				orderConfirmedEvent.getCartInfo().getCartId(), 
				domainEvent.getEventName()
			);
			
			System.out.println(
				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
				+ ", Emptied the Cart as the Order has been confirmed "
				+ ", OrderInfo = " + orderConfirmedEvent.getOrderInfo()
				+ ", CartInfo = " + orderConfirmedEvent.getCartInfo());
				
		}
		
		// [ Cases for which items needed to be ADDED back to Cart ] //
		//	1. ?
		
		// else if 
		
	}

}

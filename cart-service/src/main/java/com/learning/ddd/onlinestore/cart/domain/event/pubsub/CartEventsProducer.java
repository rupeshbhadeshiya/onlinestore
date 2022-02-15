package com.learning.ddd.onlinestore.cart.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;

@Component
public class CartEventsProducer extends DomainEventsPublisher {
	
	//public static final String SERVICE_COMPONENT = "==== [cart-service] " + CartEventsProducer.class.getSimpleName();
	
	@Value("${onlinestore.cart.events.topic.name:CartEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[cart-service] " + CartEventsProducer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}

}

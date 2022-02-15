package com.learning.ddd.onlinestore.order.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;

@Component
public class OrderEventsProducer extends DomainEventsPublisher {
	
	//public static final String SERVICE_COMPONENT = "==== [order-service] " + OrderEventsProducer.class.getSimpleName();
	
	@Value("${onlinestore.order.events.topic.name:OrderEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[order-service] " + OrderEventsProducer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}

}

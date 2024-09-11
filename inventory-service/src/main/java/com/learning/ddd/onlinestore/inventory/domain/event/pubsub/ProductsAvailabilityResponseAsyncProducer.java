package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;

@Component
public class ProductsAvailabilityResponseAsyncProducer extends DomainEventsPublisher {
	
	//public static final String SERVICE_COMPONENT = "==== [inventory-service] " + OrderEventsProducer.class.getSimpleName();
	
	@Value("${onlinestore.order.events.topic.name:OrderInventoryResponseTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[inventory-service] " + ProductsAvailabilityResponseAsyncProducer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}

}

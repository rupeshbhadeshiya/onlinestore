package com.learning.ddd.onlinestore.inventory.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;

@Component
public class InventoryEventsProducer extends DomainEventsPublisher {
	
	//public static final String SERVICE_COMPONENT = "==== [inventory-service] " + InventoryEventsProducer.class.getSimpleName();
	
	@Value("${onlinestore.inventory.events.topic.name:InventoryEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[inventory-service] " + InventoryEventsProducer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}

}

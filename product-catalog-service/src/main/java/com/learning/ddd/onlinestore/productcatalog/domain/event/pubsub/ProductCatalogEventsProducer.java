package com.learning.ddd.onlinestore.productcatalog.domain.event.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;

@Component
public class ProductCatalogEventsProducer extends DomainEventsPublisher {
	
	//public static final String SERVICE_COMPONENT = "==== [product-catalog-service] " + InventoryEventsProducer.class.getSimpleName();
	
	@Value("${onlinestore.productcatalog.events.topic.name:ProductCatalogEventsTopic}")
	private String topicName;
	
	
	@Override
	protected String getCallingServiceName() {
		return "[product-catalog-service] " + ProductCatalogEventsProducer.class.getSimpleName();
	}
	
	@Override
	protected String getTopicName() {
		return topicName;
	}

}

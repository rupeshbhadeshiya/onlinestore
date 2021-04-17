package com.learning.ddd.onlinestore.commons.domain.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyDomainEventsPublisher implements DomainEventsPublisher {

	@Autowired
	private DomainEventService domainEventService;
	
	@Override
	public void publishEvent(DomainEvent domainEvent) {
		
		domainEventService.publishEvent(domainEvent);
		
		System.out.println("DummyDomainEventsPublisher: published event - " + domainEvent);
	}
	
	public void setDomainEventService(DomainEventService domainEventService) {
		this.domainEventService = domainEventService;
	}

}

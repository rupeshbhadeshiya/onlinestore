package com.learning.ddd.onlinestore.commons.domain.event;

public class DummyDomainEventsPublisher implements DomainEventsPublisher {

	private DomainEventService domainEventService;
	
	@Override
	public void publishEvent(DomainEvent domainEvent) {
		
		domainEventService.publishEvent(domainEvent);
		
		System.out.println("DummyDomainEventsPublisher: published event - " + domainEvent);
	}
	
	@Override
	public void setDomainEventService(DomainEventService domainEventService) {
		this.domainEventService = domainEventService;
	}

}

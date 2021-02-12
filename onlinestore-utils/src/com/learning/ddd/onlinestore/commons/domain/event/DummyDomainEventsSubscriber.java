package com.learning.ddd.onlinestore.commons.domain.event;

public class DummyDomainEventsSubscriber implements DomainEventSubscriber {

	@Override
	public void processEvent(DomainEvent domainEvent) {
		
		System.out.println("DummyDomainEventsSubscriber: processEvent():  event - " + domainEvent);
	}

}

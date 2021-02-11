package com.learning.ddd.onlinestore.commons.domain.event;

public class SampleEventsSubscriber implements DomainEventSubscriber {

	@Override
	public void processEvent(DomainEvent domainEvent) {
		System.out.println("SampleEventsSubscriber: processEvent():  event - " + domainEvent);
	}

}

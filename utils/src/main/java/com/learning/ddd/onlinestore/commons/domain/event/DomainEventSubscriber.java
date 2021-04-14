package com.learning.ddd.onlinestore.commons.domain.event;

public interface DomainEventSubscriber {

	void processEvent(DomainEvent domainEvent);
	
}

package com.learning.ddd.onlinestore.commons.domain.event;

public interface DomainEventsPublisher {

	public void setDomainEventService(DomainEventService domainEventService);
	
	void publishEvent(DomainEvent domainEvent);

}

package com.learning.ddd.onlinestore.commons.domain.event;

public interface DomainEventsPublisher {

	void publishEvent(DomainEvent domainEvent);

}

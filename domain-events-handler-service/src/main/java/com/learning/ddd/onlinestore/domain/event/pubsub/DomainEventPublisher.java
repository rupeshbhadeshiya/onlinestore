package com.learning.ddd.onlinestore.domain.event.pubsub;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.exception.DomainEventPublishingFailedException;

public interface DomainEventPublisher {

	void publishEvent(DomainEvent domainEvent) 
			throws DomainEventPublishingFailedException;

}

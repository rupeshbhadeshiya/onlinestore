package com.learning.ddd.onlinestore.domain.event.pubsub;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;

public interface DomainEventConsumer {

	public void consumeEvent(DomainEvent domainEvent);

}

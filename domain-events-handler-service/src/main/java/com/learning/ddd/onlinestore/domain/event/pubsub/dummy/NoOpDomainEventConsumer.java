package com.learning.ddd.onlinestore.domain.event.pubsub.dummy;

import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;

@Component
public class NoOpDomainEventConsumer implements DomainEventConsumer {

	@Override
	public void consumeEvent(DomainEvent domainEvent) {
		System.out.println("==== NoOpDomainEventProcessor "
				+ "- just logging received event - " 
				+ domainEvent);
	}

}

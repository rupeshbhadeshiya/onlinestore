package com.learning.ddd.onlinestore.commons.domain.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DomainEventService {

	// in-memory storage of Domain Events
	private List<DomainEvent> domainEventStore = new ArrayList<>();
	
	@Autowired
	private List<DomainEventSubscriber> domainEventSubscribers;
	
	public void publishEvent(DomainEvent domainEvent) {
		domainEventStore.add(domainEvent);
		sendEventToSubscribers(domainEvent);
	}

	private void sendEventToSubscribers(DomainEvent domainEvent) {
		for (DomainEventSubscriber domainEventSubscriber : domainEventSubscribers) {
			domainEventSubscriber.processEvent(domainEvent);
		}
	}
	
	public void setDomainEventSubscribers(List<DomainEventSubscriber> domainEventSubscribers) {
		this.domainEventSubscribers = domainEventSubscribers;
	}

}

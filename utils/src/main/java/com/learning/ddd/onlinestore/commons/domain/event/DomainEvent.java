package com.learning.ddd.onlinestore.commons.domain.event;

public interface DomainEvent {

	public DomainEventName getEventName();
	
	public Object getEventData();
	
}

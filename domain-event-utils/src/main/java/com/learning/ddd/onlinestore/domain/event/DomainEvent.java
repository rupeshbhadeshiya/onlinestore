package com.learning.ddd.onlinestore.domain.event;

import java.io.Serializable;

public interface DomainEvent extends Serializable {

	public DomainEventName getEventName();
	
	public Object getEventData();
	
}

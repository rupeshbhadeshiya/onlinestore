package com.learning.ddd.onlinestore.domain.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DomainEvent implements Serializable {

	private static final long serialVersionUID = 9008892552343079068L;

	private DomainEventName eventName;
	//private Object eventData;
	private Map<DomainEventName, Object> eventAndDataMapping = new HashMap<>();
	
	public DomainEvent(DomainEventName eventName, Object eventData) {
		super();
		this.eventName = eventName;
		//this.eventData = eventData;
		this.eventAndDataMapping.put(eventName, eventData);
	}

	public DomainEventName getEventName() {
		return eventName;
	}
	
	public Object getEventData() {
		//return eventData;
		return this.eventAndDataMapping.get(eventName);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + ": " + eventAndDataMapping;
	}
	
}

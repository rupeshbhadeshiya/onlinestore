package com.learning.ddd.onlinestore.domain.event;

import java.io.Serializable;

public class OnlinestoreDomainEvent implements Serializable {

	private static final long serialVersionUID = 9008892552343079068L;

	private OnlinestoreDomainEventName eventName;
	private Object eventData;
	
	public OnlinestoreDomainEvent(OnlinestoreDomainEventName eventName, Object eventData) {
		super();
		this.eventName = eventName;
		this.eventData = eventData;
	}

	public OnlinestoreDomainEventName getEventName() {
		return eventName;
	}
	
	public Object getEventData() {
		return eventData;
	}
	
	@Override
	public String toString() {
		return (this.getClass().getName() + ": eventName = " + this.eventName
			+ ", eventData = " + eventData);
	}
	
}

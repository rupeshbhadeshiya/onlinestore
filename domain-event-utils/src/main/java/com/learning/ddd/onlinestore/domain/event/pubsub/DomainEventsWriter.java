package com.learning.ddd.onlinestore.domain.event.pubsub;

import javax.jms.JMSException;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;

public interface DomainEventsWriter {

	public abstract void connect(String topicName, String callingServiceName);
	public abstract void write(OnlinestoreDomainEvent domainEvent) throws JMSException;
	public abstract void disconnect();
	
}

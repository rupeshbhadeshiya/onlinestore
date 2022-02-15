package com.learning.ddd.onlinestore.domain.event.pubsub;

import javax.jms.JMSException;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;

public interface DomainEventsWriter {

	public abstract void connect(String topicName, String callingServiceName);
	public abstract void write(DomainEvent domainEvent) throws JMSException;
	public abstract void disconnect();
	
}

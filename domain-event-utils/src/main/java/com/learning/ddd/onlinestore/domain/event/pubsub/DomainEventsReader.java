package com.learning.ddd.onlinestore.domain.event.pubsub;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public interface DomainEventsReader {

	public abstract void connect(String topicName, String callingServiceName);
	public abstract ObjectMessage read() throws JMSException;
	public abstract void disconnect();
	
}

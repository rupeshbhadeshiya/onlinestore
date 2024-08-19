package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import javax.jms.JMSException;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsWriter;

@ActiveProfiles("test") // Use this bean while running JUnit test
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // V.IMP; each Consumer Bean using it must've exclusive copy!
														// otherwise it results into multithreading issues...
public class SysoutDomainEventsWriter implements DomainEventsWriter {

	private static final String THIS_CLASS_NAME = SysoutDomainEventsWriter.class.getSimpleName();
	
	@Override
	public void connect(String topicName, String callingServiceName) {
		
		System.out.println(THIS_CLASS_NAME + ": connect() - started");
		
		System.out.println(THIS_CLASS_NAME + ": connect() - completed");
	}
	
	@Override
	public void write(OnlinestoreDomainEvent domainEvent) throws JMSException {

		System.out.println(THIS_CLASS_NAME + ": write() - started");
		
		System.out.println(THIS_CLASS_NAME + ": write() - "
			+ "domainEvent is written to sysout, domainEvent=" + domainEvent);

	}
	
	
	@Override
	public void disconnect() {
	
		System.out.println(THIS_CLASS_NAME + ": disconnect() - started");
		
		System.out.println(THIS_CLASS_NAME + ": disconnect() - completed");
	} 
	

}

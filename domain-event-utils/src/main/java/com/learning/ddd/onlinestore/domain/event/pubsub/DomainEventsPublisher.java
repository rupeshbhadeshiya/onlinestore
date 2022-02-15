package com.learning.ddd.onlinestore.domain.event.pubsub;

import javax.jms.JMSException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;

//@Component (no need for this bean to be a @Component, as extending bean will be anyway declaring itself a @Component)
public abstract class DomainEventsPublisher implements ServletContextListener {
	
	
	@Autowired
	private DomainEventsWriter domainEventsWriter; // must keep under @Component otherwise bean binding won't happen
	
	// note, unlike Consumer, here we don't need a daemon worker thread...
	
	// ServletContextListener implementation - handle application start-time activity
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println(getCallingServiceName() + ": contextInitialized() - started");
		
		// domainEventsWritingWorker.start();
		domainEventsWriter.connect(getTopicName(), getCallingServiceName());
		
		System.out.println(getCallingServiceName() + ": contextInitialized() - completed");
	}
	
	
	// ServletContextListener implementation - handle application stop-time activity
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		System.out.println(getCallingServiceName() + ": contextDestroyed() - started");
		
		//domainEventsWritingWorker.stopWork();
		domainEventsWriter.disconnect();
		
		System.out.println(getCallingServiceName() + ": contextDestroyed() - completed");
	}
	
	
	public void publishDomainEvent(DomainEvent domainEvent) throws JMSException {
		
		System.out.println(getCallingServiceName() + ": publishDomainEvent() - started");
		
		//domainEventsWritingWorker.stopWork();
		domainEventsWriter.write(domainEvent);
		
		System.out.println(getCallingServiceName() + ": publishDomainEvent() - completed");
	}
	
	
	/* Publisher to provide name of Topic where DomainEvent will be written */
	protected abstract String getTopicName();
	
	/* Publisher to provide name (e.g. cart-service: CartEventsProducer, or, cart-service: OnlinestoreEventsProducer */
	protected abstract String getCallingServiceName();
	
	
}

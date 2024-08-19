package com.learning.ddd.onlinestore.domain.event.pubsub.dummy;

import javax.jms.JMSException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsWriter;

@Profile("test")
@Configuration
public class JUnitTestsSpecificSpringBeanConfigurations {

	public JUnitTestsSpecificSpringBeanConfigurations() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~> JUnitTestsSpecificSpringBeanConfigurations() <~~~~~~~~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	@Bean
	public DomainEventsWriter domainEventsWriter() {
		
		return new DomainEventsWriter() {
			
			@Override
			public void write(OnlinestoreDomainEvent domainEvent) throws JMSException {
				System.out.println("~~~~~~~~> Dummy DomainEventsWriter(): write(): domainEvent = " + domainEvent + " <~~~~~~~~");
			}
			
			@Override
			public void connect(String topicName, String callingServiceName) {
				System.out.println("~~~~~~~~> Dummy DomainEventsWriter(): connect(): " +
					"callingServiceName = " + callingServiceName + ", topicName = " + topicName + " <~~~~~~~~");
			}
			
			@Override
			public void disconnect() {
				System.out.println("~~~~~~~~> Dummy DomainEventsWriter(): disconnect(): <~~~~~~~~");
			}
			
		};
	}
	
}

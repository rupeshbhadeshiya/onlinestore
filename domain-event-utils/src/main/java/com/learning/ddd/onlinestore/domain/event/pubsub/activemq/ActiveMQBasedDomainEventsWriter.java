package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import java.util.Set;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsWriter;
import com.learning.ddd.onlinestore.domain.event.pubsub.exception.DomainEventPublishingFailedException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // V.IMP; each Consumer Bean using it must've exclusive copy!
														// otherwise it results into multithreading issues...
public class ActiveMQBasedDomainEventsWriter implements DomainEventsWriter {

	private static final String LISTENER_NAME = ActiveMQBasedDomainEventsWriter.class.getSimpleName();
	
	// FIXME Should session be opened for each new message sending or just one session
	//			to be used for lifetime (like done right now) - which is the correct way?
	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;

	private String serviceAndListerName;

	private String topicName;
	//private String callingServiceName;
	
	
	@Override
	public void connect(String topicName, String callingServiceName) {
		
		this.topicName = topicName;
		//this.callingServiceName = callingServiceName;
		this.serviceAndListerName = callingServiceName + " - " + LISTENER_NAME;
		
		System.out.println(this.serviceAndListerName + ": connect() - started, Topic = " + topicName);
		
		try {
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println(this.serviceAndListerName + ": connect() - ActiveMQConnectionFactory created");

			// Create connection.
			connection = factory.createConnection();
			System.out.println(this.serviceAndListerName + ": Connection created, Topic = " + topicName);

			// Start the connection
			connection.start();
			System.out.println(this.serviceAndListerName + ": connect() - Connection started");

			// Create a session which is non transactional
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println(this.serviceAndListerName + ": connect() - Session created, Topic = " + topicName);

			// Reuse existing Topic or Create a new Destination topic
			
			Topic topic = null; //session.createTopic(topicName);
			
			// first search
			Set<ActiveMQTopic> existingTopics = ((ActiveMQConnection) connection).getDestinationSource().getTopics();
			for (ActiveMQTopic activeMQTopic : existingTopics) {
				if (activeMQTopic.getTopicName().equals(topicName)) {
					topic = activeMQTopic;
					System.out.println(this.serviceAndListerName + ": connect() - Topic exists! - " + topic);
					break;
				}
			}
			
			if (topic == null) { // not found, so create a new one
				topic = session.createTopic(topicName);
				System.out.println(this.serviceAndListerName + ": connect() - Topic not existing so created - " + topic);
			}
			
			// Create a consumer
			producer = session.createProducer(topic);
			System.out.println(this.serviceAndListerName + ": connect() - MessageProducer created, Topic = " + topicName);

		} catch (Exception ex) {
			System.err.println(this.serviceAndListerName + ": connect() - Exception while creating "
				+ "Connection/Session/MessageProducer! Topic = " + topicName);
			ex.printStackTrace();
		}
			
		System.out.println(this.serviceAndListerName + ": connect() - completed, Topic = " + topicName);
	}
	
	@Override
	public void write(DomainEvent domainEvent) throws JMSException {

		System.out.println(this.serviceAndListerName + ": write() - started, Topic = " + topicName);
		
		try { 
			//String msg = "Hello World! Today is " + new Date();
			//TextMessage message = session.createTextMessage(message);

			ObjectMessage message = session.createObjectMessage(domainEvent);
			System.out.println(this.serviceAndListerName + ": write() - "
					+ "MessageProducer is sending a domainEvent to Topic=" + topicName
					+ ", domainEvent=" + domainEvent + ", ObjectMessage=" + message);
			
			producer.send(message);
			
			System.out.println(this.serviceAndListerName + ": write() - "
					+ "MessageProducer has sent a domainEvent to Topic=" + topicName
					+ ", domainEvent=" + domainEvent + ", ObjectMessage=" + message);

		} catch (JMSException ex) {
			System.err.println(this.serviceAndListerName + ": write() - "
					+ "Error while MessageProducer was sending a domainEvent to Topic=" + topicName
					+ ", domainEvent=" + domainEvent);
			ex.printStackTrace();
			throw new DomainEventPublishingFailedException(ex);
		}
		
	}
	
	
	@Override
	public void disconnect() {
	
		System.out.println(this.serviceAndListerName + ": disconnect() - started, Topic = " + topicName);
		
		try {
			
			if (session != null) {
				session.close();
				System.out.println(this.serviceAndListerName + ": disconnect() - Session closed, Topic = " + topicName);
			}
			
		} catch (Exception ex) {
			System.err.println(this.serviceAndListerName + ": disconnect() - Exception while closing Session!"
				+ ", Topic = " + topicName);
			ex.printStackTrace();
		}
		
		try {
			
			if (connection != null) {
				connection.close();
				System.out.println(this.serviceAndListerName + ": disconnect() - Connection closed, Topic = " + topicName);
			}
			
		} catch (Exception ex) {
			System.err.println(this.serviceAndListerName + ": disconnect() - Exception while closing Connection!"
				+ ", Topic = " + topicName);
			ex.printStackTrace();
		}
		
		System.out.println(this.serviceAndListerName + ": disconnect() - completed, Topic = " + topicName);
	} 
	

}
